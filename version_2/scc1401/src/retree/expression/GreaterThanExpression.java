package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.*;
import compiler.SmallCC;

public class GreaterThanExpression extends Expression 
{
	private final Expression l, r;

	public GreaterThanExpression(Expression l, Expression r) 
	{
		super(Type.intType);
		
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
			return new ConstantExpression(Type.intType, ((ConstantExpression)l2).getValue() > ((ConstantExpression)r2).getValue() ? 1 : 0);
		}
		
		return new GreaterThanExpression(l2, r2);
	}

    public String generateCode(boolean valueNeeded)
	{
		if (valueNeeded) 
		{
			String code = COM("GreaterThanExpression(" + l + ":" + r + ")") +
			l.generateCode(valueNeeded) + SNIP("clean_number") + r.generateCode(valueNeeded) + SNIP("clean_number");
			
			String labelLessThan = label(SmallCC.nextLabelNumber());
			String labelEnd = label(SmallCC.nextLabelNumber());
			
			code += INS("C", STACK_OFF(0), STACK_OFF(-5));
			code += POP(5);
			code += INS("MCW", NUM_CONST(0, false), STACK_OFF(0));
			code += INS("BH", labelLessThan);
			code += INS("B", labelEnd);
			code += LBL_INS(labelLessThan, "MCW", NUM_CONST(1, false), STACK_OFF(0));
			code += LBL_INS(labelEnd, "NOP");
			
			return code;
		} 
		else 
		{
			return l.generateCode(false) + r.generateCode(false);
		}
	}

	public String toString()
	{
		return "(" + l + " > " + r +")";
	}
}
