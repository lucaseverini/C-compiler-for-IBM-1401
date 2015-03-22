/*
	EqualExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import retree.intermediate.*;
import compiler.SmallCC;

public class EqualExpression extends Expression
{
	private Expression l, r;

	public EqualExpression(Expression l, Expression r) throws TypeMismatchException
	{
		super(Type.intType);

		if (! l.getType().equals(r.getType()))
		{
			throw new TypeMismatchException(r, l.getType(), r.getType());
		}

		this.l = l;
		this.r = r;
	}

	public Expression collapse()
	{
		try
		{
			Expression l2 = l.collapse();
			Expression r2 = r.collapse();

			if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression)
			{
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() == ((ConstantExpression)r2).getValue() ? 1 : 0);
			}

			return new EqualExpression(l2, r2);
		}
		catch (TypeMismatchException e)
		{
			//should never happen
			return null;
		}
	}

	public String generateCode(boolean valueNeeded)
	{
		String labelEqual = label(SmallCC.nextLabelNumber());
		String labelEnd = label(SmallCC.nextLabelNumber());

		Optimizer.addInstruction("Equal (==) " + this.toString(),"","");
		String code = COM("Equal (==) " + this.toString());
		code += l.generateCode(valueNeeded);

		if (valueNeeded && l.getType().equals(Type.intType))
		{
			code += SNIP("clean_number");
		}

		code += r.generateCode(valueNeeded);

		if (valueNeeded && r.getType().equals(Type.intType))
		{
			code += SNIP("clean_number");
		}

		if (valueNeeded)
		{
			int size = l.getType().sizeof();
			Optimizer.addInstruction("Compare", "", "C", STACK_OFF(0), STACK_OFF(-size));
			code += INS("Compare", null, "C", STACK_OFF(0), STACK_OFF(-size));
			code += POP(size) + POP(size);
			code += PUSH(Type.intType.sizeof(), NUM_CONST(0, false));
			Optimizer.addInstruction("Jump if equal", "", "BE", labelEqual);
			code += INS("Jump if equal", null, "BE", labelEqual);
			Optimizer.addInstruction("Jump to End", "", "B", labelEnd);
			code += INS("Jump to End", null, "B", labelEnd);
			Optimizer.addInstruction("Equal", labelEqual, "MCW", NUM_CONST(1, false), STACK_OFF(0));
			code += INS("Equal", labelEqual, "MCW", NUM_CONST(1, false), STACK_OFF(0));
			Optimizer.addInstruction("End of Equal", labelEnd, "NOP");
			code += INS("End of Equal", labelEnd, "NOP");
		}

		return code;
	}

	public String toString()
	{
		return "(" + l +" == "+ r + ")";
	}
}
