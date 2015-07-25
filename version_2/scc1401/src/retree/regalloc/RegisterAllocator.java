package retree.regalloc;

import retree.expression.*;
import retree.statement.*;

/**
 * Created by mplv on 7/23/15.
 */
public class RegisterAllocator {

	// total number of registers that we are emulating
	private int numberRegisters = 0;
	// height of the statement
	private int level = 0;

	public RegisterAllocator(int numRegs)
	{
		numberRegisters = numRegs;
	}



	public void calculatePlacements(Statement s)
	{
		level = 0;
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
			handleDoWhileStatement((DoWhileStatement)s);
		}
		else if (s instanceof ExpressionStatement)
		{
			handleExpressionStatement((ExpressionStatement)s);
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

	private void handleFunctionCallExpression(FunctionCallExpression functionCallExpression) {

	}

	private void lifeScopeEval(Expression e)
	{
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
		// If the right side of the tree is a leaf then wait until the left side is evaluated before giving it a lifeScope
		} else if ((e.getRightExpression() instanceof VariableExpression || e.getRightExpression() instanceof ConstantExpression) &&
				!(e.getLeftExpression() instanceof VariableExpression || e.getLeftExpression() instanceof ConstantExpression))
		{
			lifeScopeEval(e.getLeftExpression());
			e.getRightExpression().scopeInterval.setStart(level);
			e.getRightExpression().scopeInterval.setEnd(level);
			level += 1;
			e.scopeInterval.setStart(level);
			e.scopeInterval.setEnd(level);
		// If the left side is a leaf then give it a lifeScope value and then generate life values for the right tree
		} else if (!(e.getRightExpression() instanceof VariableExpression || e.getRightExpression() instanceof ConstantExpression) &&
				(e.getLeftExpression() instanceof VariableExpression || e.getLeftExpression() instanceof ConstantExpression))
		{
			e.getLeftExpression().scopeInterval.setStart(level);
			lifeScopeEval(e.getRightExpression());
			e.getLeftExpression().scopeInterval.setEnd(level);
			level += 1;
			e.scopeInterval.setStart(level);
			e.scopeInterval.setEnd(level);
		// If both sides are trees then evaluate them both then assign this node a value
		// also set both values as they can be overwritten
		} else {
			if (e.associativity) {
				lifeScopeEval(e.getLeftExpression());
				lifeScopeEval(e.getRightExpression());
				e.getLeftExpression().scopeInterval.setEnd(e.getRightExpression().scopeInterval.getEnd());
				level += 1;
				e.scopeInterval.setStart(level);
				e.scopeInterval.setEnd(level);
			} else {
				lifeScopeEval(e.getRightExpression());
				lifeScopeEval(e.getLeftExpression());
				e.getRightExpression().scopeInterval.setEnd(e.getLeftExpression().scopeInterval.getEnd());
				level += 1;
				e.scopeInterval.setStart(level);
				e.scopeInterval.setEnd(level);
			}
		}
	}

	private void handleIfStatement(IfStatement ifStatement) {
		lifeScopeEval(ifStatement.getCondition());
	}

	private void handleWhileStatement(WhileStatement whileStatement) {
		lifeScopeEval(whileStatement.getCondition());
		calculatePlacements(whileStatement.getBody());
	}

	private void handleSwitchStatement(SwitchStatement switchStatement) {
		lifeScopeEval(switchStatement.getExpression());
	}

	private void handleReturnStatement(ReturnStatement returnStatement) {
		lifeScopeEval(returnStatement.getExpression());
	}

	private void handleForStatement(ForStatement forStatement) {
		lifeScopeEval(forStatement.getInit());
		level = 0;
		lifeScopeEval(forStatement.getCondition());
		level = 0;
		lifeScopeEval(forStatement.getPost());
		calculatePlacements(forStatement.getBody());
	}

	private void handleExpressionStatement(ExpressionStatement expressionStatement) {
		lifeScopeEval(expressionStatement.getExpression());
	}

	private void handleDoWhileStatement(DoWhileStatement doWhileStatement) {
		lifeScopeEval(doWhileStatement.getCondition());
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
