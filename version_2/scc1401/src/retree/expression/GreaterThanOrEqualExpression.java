/*
	GreaterThanOrEqualExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.*;
import compiler.SmallCC;

public class GreaterThanOrEqualExpression extends Expression 
{
	private final Expression l, r;

	public GreaterThanOrEqualExpression(Expression l, Expression r) 
	{
		super(Type.intType);
		/*
		if (! l.getType().equals(r.getType())) {
				throw new TypeMismatchException(r, l.getType(), r.getType());
		}*/
		if (! l.getType().equals(Type.intType)) 
		{
			l = new CastExpression(Type.intType, l);
		}

		if (! r.getType().equals(Type.intType)) 
		{
			r = new CastExpression(Type.intType, r);
		}
		
		this.l = l;
		this.r = r;
	}

	public Expression collapse() 
	{
		Expression l2 = l.collapse();
		Expression r2 = r.collapse();
		
		if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression)
		{
			return new ConstantExpression(Type.intType, ((ConstantExpression)l2).getValue() >= ((ConstantExpression)r2).getValue() ? 1 : 0);
		}
		
		return new GreaterThanOrEqualExpression(l2, r2);
	}

    public String generateCode(boolean valueNeeded) 
	{
		if (valueNeeded) 
		{
			String lCode = l.generateCode(valueNeeded);
			String rCode = r.generateCode(valueNeeded);

			String code = COM("GreaterOrEqual (>=) (" + this.toString()) +
			lCode + SNIP("clean_number") + rCode + SNIP("clean_number");

			String labelLessThan = label(SmallCC.nextLabelNumber());
			String labelEnd = label(SmallCC.nextLabelNumber());

			// ###############
			// ## WARNING!! ##
			// ###############
			// IS CORRECT HERE TO USE A FIXED VALUE (5) FOR THE VARIABLE SIZE
			code += INS("Compare", null, "C", STACK_OFF(0), STACK_OFF(-5));
			code += POP(5);
			// code += POP(getType().sizeof());

			code += INS("Set stack location to 1", null, "MCW", NUM_CONST(1, false), STACK_OFF(0));
			code += INS("Jump if greater or equal", null, "BL", labelLessThan);
			code += INS("Jump to End", null, "B", labelEnd);
			code += INS("GreaterOrEqual: Set stack location to 0", labelLessThan, "MCW", NUM_CONST(0, false), STACK_OFF(0));
			code += INS("End of GreaterOrEqual", labelEnd, "NOP");

			return code;
		} 
		else
		{
			return l.generateCode(false) + r.generateCode(false);
		}
	}

	public String toString()
	{
		return "(" + l + " >= " + r + ")";
	}
}
