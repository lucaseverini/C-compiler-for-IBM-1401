/*
	ReturnStatement.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import retree.expression.*;
import retree.regalloc.RegisterAllocator;

import static retree.RetreeUtils.*;

public class ReturnStatement extends Statement
{
	private final Expression exp;
	private final VariableExpression returnLocation;
	private final String returnLabel;
	
	public ReturnStatement(Expression exp, VariableExpression returnLocation, String returnLabel) 
	{
		this.exp = exp;
		this.returnLocation = returnLocation;
		this.returnLabel = returnLabel;
	}

	// TODO reg alloc stuff
	@Override
	public String generateCode(RegisterAllocator registerAllocator)
	{
		String code = "";
		registerAllocator.linearScanRegisterAllocation(expressionList);
		
		if(returnLabel != null)
		{
			if(exp != null)
			{
				code += COM("Return to " + returnLabel + " with return value " + exp);
			}
			else
			{
				code += COM("Return to " + returnLabel + " with no return value");
			}
		}
		
		// If we have a real return value, put it on stack
		if (exp != null && returnLocation != null)
		{
			code += COM("Put on stack return value (" + exp + ")");

			int offset = returnLocation.getOffset();
			code += exp.generateCode(true);
			if (SmallCC.nostack)
			{
				code += INS("Move X3 back to return addr", null, "MA", "@"+ADDR_COD(offset)+"@", "X3");
				code += INS("Copy ret value to return value location",null,"LCA", REG(exp), "0+X3");
			} else {
				code += POP(exp.getType().sizeof(), OFF(offset));
			}
		}
		
		//code += COM("Set the return flag, so we know to deallocate our stack");
		//code += INS("PUT @R@ into location RF", null, "MCW", "@R@", "RF");
		
		if(returnLabel != null)
		{
			code += INS("Jump to end of function block", null, "B", returnLabel);
		}
		
		code += "\n";
		
		return code;
	}

	public VariableExpression getReturnLocation() {
		return returnLocation;
	}

	public Expression getExpression() {
		return exp;
	}
}
