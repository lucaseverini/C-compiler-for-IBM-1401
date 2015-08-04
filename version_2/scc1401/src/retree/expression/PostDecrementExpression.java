/*
	PostDecrementExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;

import compiler.SmallCC;
import retree.exceptions.*;
import retree.type.*;

public class PostDecrementExpression extends Expression
{
	private final LValue l;

	public PostDecrementExpression(LValue l) throws TypeMismatchException
	{
		super(l.getType());
		
		this.l = l;
	}

	@Override
	public Expression collapse() 
	{
		try
		{
			return new PostDecrementExpression(l.collapse());
		} 
		catch (retree.exceptions.TypeMismatchException e)
		{
			return null;
		}
	}

	@Override
	public String generateCode(boolean valueNeeded)
	{
		String code = COM("PostDecrement "+ this.toString());
		code += l.generateAddress();
		if ( ! SmallCC.nostack) {
			code += POP(3, "X1");
		}
		if (SmallCC.nostack) {
			code += INS("Move addr to X1", null, "MCW", REG(l), "X1");
			if (getType() instanceof PointerType)
			{
				PointerType pt = (PointerType) getType();
				code += INS("Postdecrement pointer at X1", null, "MA", ADDR_CONST(-pt.getRefType().sizeof(), false), "0+X1");
				code += INS("Move result to " + REG(this), null, "MCW", "X1", REG(this));
			}
			else
			{
				code += INS("Postdecrement memory at X1", null, "S", NUM_CONST(1, false), "0+X1");
			}
		} else {
			if (valueNeeded)
			{
				code += PUSH(l.getType().sizeof(), "0+X1");
			}
			if (getType() instanceof PointerType)
			{
				PointerType pt = (PointerType) getType();
				code += INS("Postdecrement pointer at X1", null, "MA", ADDR_CONST(-pt.getRefType().sizeof(), false), "0+X1");
			}
			else
			{
				code += INS("Postdecrement memory at X1", null, "S", NUM_CONST(1, false), "0+X1");
			}

		}

		return code;
	}

	@Override
	public Expression getLeftExpression() {
		return l;
	}

	@Override
	public Expression getRightExpression() {
		return null;
	}

	@Override
	public String toString()
	{
		return "(" + l + "--" + ")";
	}
}
