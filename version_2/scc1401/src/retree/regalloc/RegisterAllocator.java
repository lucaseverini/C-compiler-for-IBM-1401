package retree.regalloc;

import retree.expression.*;
import retree.statement.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mplv on 7/23/15.
 */
public class RegisterAllocator {

	// total number of registers that we are emulating
	private int numberRegisters = 8;
	// height of the statement
	private int level = 0;

	// for use in the algorithm DO NOT MODIFY
	boolean regs[] = new boolean[numberRegisters];
	int numRegsInUse = 0;

	public RegisterAllocator(int numRegs)
	{
		numberRegisters = numRegs;
		regs = new boolean[numberRegisters];
	}

	public int getNumberRegisters(){
		return numberRegisters;
	}

	public int nextFreeRegister()
	{
		for (int i = 0 ; i < regs.length; i++)
		{
			if (!regs[i]) {
				numRegsInUse += 1;
				regs[i] = true;
				return i;
			}
		}
		return -1;
	}

	public void linearScanRegisterAllocation(ArrayList<Expression> parts)
	{
		regs = new boolean[numberRegisters];
		numRegsInUse = 0;
		ScopedIntervalComparator scopedIntervalComparator = new ScopedIntervalComparator();
		ArrayList<ScopeInterval> activeIntervals = new ArrayList<>();
		for(Expression e : parts)
		{
			expireOldIntervals(e.scopeInterval,activeIntervals);
			if (numRegsInUse == numberRegisters) {
				// normally for linear scan register allocation at this point the values would "spill" into memory
				// as this machine has very limited memory we will require the expression to be broken down into
				// multiple lines so that it is easier to parse for use and does not tax the system
				// trying to move around the computations
				// Note: possible bad behavior when assignment happens on a function call as that might
				// cause a register to be used until the function returns which leads to less registers available for
				// the function
				System.out.println("Expression " + e + " exceeds number of available registers for computing it's result.\n"+
						"Please break it up into less steps");
				System.exit(-1);
			} else {
				// add it to the active interval
				activeIntervals.add(e.scopeInterval);
				// assign that expression a register
				int reg = nextFreeRegister();
				if (reg == -1)
				{
					System.out.println("Expression " + e + " exceeds number of available registers for computing it's result.\n"+
							"Please break it up into less steps");
					System.exit(-1);
				}
				e.scopeInterval.setAssignedRegister(reg);
				// sort by increasing end time
				Collections.sort(activeIntervals, scopedIntervalComparator);
			}
		}
	}

	public void expireOldIntervals(ScopeInterval i,ArrayList<ScopeInterval> active)
	{
		for (int index = 0; index < active.size(); index++) {
			ScopeInterval scopeInterval = active.get(index);
			if (scopeInterval.getEnd() >= i.getStart()) {
				return;
			}
			ScopeInterval si = active.remove(index);
			index -= 1;
			regs[si.getAssignedRegister()] = false;
			numRegsInUse -=1 ;
		}
	}

	public void calculatePlacements(Statement s)
	{
		level = 0;
		for (Expression e : s.expressionList)
		{
			e.scopeInterval = new ScopeInterval();
		}
		if (s instanceof BlockStatement)
		{
			handleBlockStatement((BlockStatement)s);
		}
		else if (s instanceof CaseStatement)
		{
			handleCaseStatement();
		}
		else if (s instanceof DoWhileStatement)
		{
			handleDoWhileStatement((DoWhileStatement) s);
		}
		else if (s instanceof ExpressionStatement)
		{
			handleExpressionStatement((ExpressionStatement) s);
		}
		else if (s instanceof ForStatement)
		{
			handleForStatement((ForStatement)s);
		}
		else if (s instanceof IfStatement)
		{
			handleIfStatement((IfStatement)s);
		}
		else if (s instanceof ReturnStatement)
		{
			handleReturnStatement((ReturnStatement)s);
		}
		else if (s instanceof SwitchStatement)
		{
			handleSwitchStatement((SwitchStatement)s);
		}
		else if (s instanceof WhileStatement)
		{
			handleWhileStatement((WhileStatement)s);
		}
	}

	private void lifeScopeEval(Expression e, ArrayList<Expression> expressionList)
	{
		if (e == null) {
			return;
		}

		if (e instanceof FunctionCallExpression)
		{
			FunctionCallExpression functionCallExpression = (FunctionCallExpression)e;
			expressionList.addAll(functionCallExpression.getArguments());
		}

		// if both children of the expression are leaves then generate life values at the current level
		// assuming that they have not existed before hand
		if ((e.getRightExpression() instanceof VariableExpression || e.getRightExpression() instanceof ConstantExpression) &&
				(e.getLeftExpression() instanceof VariableExpression || e.getLeftExpression() instanceof ConstantExpression))
		{
			if (e.getRightExpression().scopeInterval.getStart() == -1)
				e.getRightExpression().scopeInterval.setStart(level);
			if (e.getLeftExpression().scopeInterval.getStart() == -1)
				e.getLeftExpression().scopeInterval.setStart(level);
			e.getRightExpression().scopeInterval.setEnd(level);
			e.getLeftExpression().scopeInterval.setEnd(level);
			level += 1;
			e.scopeInterval.setStart(level);
			e.scopeInterval.setEnd(level);
			if (!expressionList.contains(e.getRightExpression()))
				expressionList.add(e.getRightExpression());
			if (!expressionList.contains(e.getLeftExpression()))
				expressionList.add(e.getLeftExpression());
			if (!expressionList.contains(e))
				expressionList.add(e);
			// If the right side of the tree is a leaf then wait until the left side is evaluated before giving it a lifeScope
		} else if ((e.getRightExpression() instanceof VariableExpression || e.getRightExpression() instanceof ConstantExpression) &&
				!(e.getLeftExpression() instanceof VariableExpression || e.getLeftExpression() instanceof ConstantExpression)) {
			lifeScopeEval(e.getLeftExpression(), expressionList);
			e.getRightExpression().scopeInterval.setStart(level);
			e.getRightExpression().scopeInterval.setEnd(level);
			level += 1;
			e.scopeInterval.setStart(level);
			e.scopeInterval.setEnd(level);
			if (!expressionList.contains(e.getRightExpression()))
				expressionList.add(e.getRightExpression());
			if (!expressionList.contains(e))
				expressionList.add(e);
		// If the left side is a leaf then give it a lifeScope value and then generate life values for the right tree
		} else if (!(e.getRightExpression() instanceof VariableExpression || e.getRightExpression() instanceof ConstantExpression) &&
				(e.getLeftExpression() instanceof VariableExpression || e.getLeftExpression() instanceof ConstantExpression))
		{
			e.getLeftExpression().scopeInterval.setStart(level);
			lifeScopeEval(e.getRightExpression(), expressionList);
			e.getLeftExpression().scopeInterval.setEnd(level);
			level += 1;
			e.scopeInterval.setStart(level);
			e.scopeInterval.setEnd(level);
			if (!expressionList.contains(e.getLeftExpression()))
				expressionList.add(e.getLeftExpression());
			if (!expressionList.contains(e))
				expressionList.add(e);
		// If both sides are trees then evaluate them both then assign this node a value
		// also set both values as they can be overwritten
		} else {
			expressionList.add(e);
			if (e.associativity) {
				lifeScopeEval(e.getLeftExpression(),expressionList);
				lifeScopeEval(e.getRightExpression(),expressionList);
				if (e.getLeftExpression() != null) {
					e.getLeftExpression().scopeInterval.setEnd(e.getRightExpression().scopeInterval.getEnd());
				}
				level += 1;
				e.scopeInterval.setStart(level);
				e.scopeInterval.setEnd(level);
			} else {
				lifeScopeEval(e.getRightExpression(),expressionList);
				lifeScopeEval(e.getLeftExpression(),expressionList);
				if (e.getRightExpression() != null) {
					e.getRightExpression().scopeInterval.setEnd(e.getLeftExpression().scopeInterval.getEnd());
				}
				level += 1;
				e.scopeInterval.setStart(level);
				e.scopeInterval.setEnd(level);
			}
		}
	}

	private void handleIfStatement(IfStatement ifStatement) {
		lifeScopeEval(ifStatement.getCondition(), ifStatement.expressionList);
	}

	private void handleWhileStatement(WhileStatement whileStatement) {
		lifeScopeEval(whileStatement.getCondition(),whileStatement.expressionList);
		calculatePlacements(whileStatement.getBody());
	}

	private void handleSwitchStatement(SwitchStatement switchStatement) {
		lifeScopeEval(switchStatement.getExpression(),switchStatement.expressionList);
	}

	private void handleReturnStatement(ReturnStatement returnStatement) {
		lifeScopeEval(returnStatement.getExpression(),returnStatement.expressionList);
	}

	private void handleForStatement(ForStatement forStatement) {
		lifeScopeEval(forStatement.getInit(),forStatement.initList);
		level = 0;
		lifeScopeEval(forStatement.getCondition(),forStatement.expressionList);
		level = 0;
		lifeScopeEval(forStatement.getPost(),forStatement.postList);
		calculatePlacements(forStatement.getBody());
	}

	private void handleExpressionStatement(ExpressionStatement expressionStatement) {
		lifeScopeEval(expressionStatement.getExpression(),expressionStatement.expressionList);
	}

	private void handleDoWhileStatement(DoWhileStatement doWhileStatement) {
		lifeScopeEval(doWhileStatement.getCondition(),doWhileStatement.expressionList);
		calculatePlacements(doWhileStatement.getBody());
	}

	private void handleCaseStatement() {

	}

	public void handleBlockStatement(BlockStatement blockStatement) {
		for (Statement s : blockStatement.getStatements()){
			calculatePlacements(s);
		}
	}

}
