/*
	IfStatement.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import retree.expression.Expression;
import retree.regalloc.RegisterAllocator;

import static retree.RetreeUtils.*;

public class IfStatement extends Statement
{
	private final Expression condition;
	private final Statement ifClause, elseClause;
	private final String trueLabel, falseLabel;
	private final int size;
	
	public IfStatement(Expression condition, Statement ifClause, Statement elseClause) 
	{
		this.condition = condition.collapse();
		this.ifClause = ifClause;
		this.elseClause = elseClause;
		this.size = condition.getType().sizeof();
		
		// the single label jumped to if the condition is false
		falseLabel = label(SmallCC.nextLabelNumber());
		
		// the single label jumped to if the condition is true	
		if (elseClause != null) 
		{
			trueLabel = label(SmallCC.nextLabelNumber());
		}
		else
		{
			trueLabel = null;
		}
	}

	public Expression getCondition()
	{
		return condition;
	}

	public Statement getIfClause() {
		return ifClause;
	}

	public Statement getElseClause() {
		return elseClause;
	}

	// TODO reg alloc stuff
	public String generateCode(RegisterAllocator registerAllocator) throws Exception
	{		
		String code = COM("If " + this.toString());
		registerAllocator.linearScanRegisterAllocation(expressionList);
		code += condition.generateCode(true);

		if (SmallCC.nostack) {
			code += INS("Jump when False", null, "BCE", falseLabel, REG(condition), "0");
			code += INS("Jump when False", null, "BCE", falseLabel, REG(condition), "?");
			code += INS("Jump when False", null, "BCE", falseLabel, REG(condition), "!");
		} else {
			code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0)); // this removes the word mark
			code += POP(size);
			code += INS("Jump when False", null, "BCE", falseLabel, STACK_OFF(size), " ");
		}
		code += ifClause.generateCode(registerAllocator);
		
		if (elseClause != null) 
		{
			code += INS("Jump when true", null, "B", trueLabel);
			code += INS("Executed when False", falseLabel, "NOP");
			code += elseClause.generateCode(registerAllocator);
			code += INS("Executed when True", trueLabel, "NOP");
		} 
		else 
		{
			code += INS("Executed when False", falseLabel, "NOP");
		}
		
		code += COM("End If " + this.toString());
		code += "\n";
		
		return code;
	}

    public String toString()
    {
        return "[if (" + condition + " then " + ifClause + (elseClause != null ? (" else " + elseClause) : "") + "]";
    }
}

