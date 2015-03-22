/*
	PostDecrementExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.intermediate.*;
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
		Optimizer.addInstruction("PostDecrement "+ this.toString(),"","");
		String code = COM("PostDecrement "+ this.toString());
		code += l.generateAddress();
		code += POP(3, "X1");

		if (valueNeeded)
		{
			code += PUSH(l.getType().sizeof(), "0+X1");
		}

		if (getType() instanceof PointerType)
		{
			PointerType pt = (PointerType) getType();
			Optimizer.addInstruction("PostDecrement pointer pointed by X1", "", "MA", ADDR_CONST(-pt.getType().sizeof(), false), "0+X1");
			code += INS("PostDecrement pointer pointed by X1", null, "MA", ADDR_CONST(-pt.getType().sizeof(), false), "0+X1");
		}
		else
		{
			Optimizer.addInstruction("PostDecrement variable pointed by X1", "", "S", NUM_CONST(1, false), "0+X1");
			code += INS("PostDecrement variable pointed by X1", null, "S", NUM_CONST(1, false), "0+X1");
		}

		return code;
	}

	@Override
	public String toString()
	{
		return "(" + l + "--" + ")";
	}
}
