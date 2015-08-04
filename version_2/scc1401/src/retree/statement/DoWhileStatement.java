/*
	DoWhileStatement.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import retree.expression.Expression;
import retree.regalloc.RegisterAllocator;

import java.util.ArrayList;

import static retree.RetreeUtils.*;

public class DoWhileStatement extends LoopStatement 
{	
	public DoWhileStatement(Expression condition, Statement body)
	{
		super(condition, body);
	}

	@Override
	public String generateCode(RegisterAllocator registerAllocator) throws Exception
	{		
		String code = COM("Do-While " + toString());
		
		code += INS("Top of Do-While", topLabel, "NOP");
		
		code += body.generateCode(registerAllocator);
		registerAllocator.linearScanRegisterAllocation(expressionList);
		code += condition.generateCode(true);
		
		code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0));
		
		code += POP(size);
		code += INS("Jump to bottom of Do-While", null, "BCE", bottomLabel, STACK_OFF(size), " ");
		code += "\n";
		
		code += INS("Jump to top of Do-While", null, "B", topLabel);
		code += "\n";
		
		code += INS("Bottom of Do-While", bottomLabel, "NOP");
		
		code += COM("End Do-While " + this.toString());
		code += "\n";
		
		return code;
	}

	@Override
    public String toString()
    {
		return "[do-while (" + condition + ") " + body + " top:" + topLabel + " bottom:" + bottomLabel + "]";
	}

	public Expression getCondition() {
		return condition;
	}
}

