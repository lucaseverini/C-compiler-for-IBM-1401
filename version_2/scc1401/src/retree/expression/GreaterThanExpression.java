/*
	GreaterThanExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

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

	@Override
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

	@Override
    public String generateCode(boolean valueNeeded)
	{
		if (valueNeeded) 
		{
			String code = COM("Greater " + this.toString()) +
								l.generateCode(valueNeeded);
			if (SmallCC.nostack)
			{
				code += INS("Move child val to CAST reg", null, "MCW", REG(l), "CAST");
			}
			code += SNIP("clean_number");
			if (SmallCC.nostack)
			{
				code += INS("Move result to " + REG(l), null, "LCA", "CAST", REG(l));
			}
			code += r.generateCode(valueNeeded);

			if (SmallCC.nostack)
			{
				code += INS("Move child val to CAST reg", null, "MCW", REG(r), "CAST");
			}
			code += SNIP("clean_number");
			if (SmallCC.nostack)
			{
				code += INS("Move result to " + REG(r), null, "LCA", "0+CAST", REG(r));
			}
			
			String labelMoreThan = label(SmallCC.nextLabelNumber());
			String labelEnd = label(SmallCC.nextLabelNumber());
			if (SmallCC.nostack)
			{
				code += INS("Compare "+REG(l)+ " to " + REG(r), null, "C", REG(r), REG(l));
				code += INS("Move 0 in " + REG(this), null, "LCA", NUM_CONST(0, false), REG(this));
				code += INS("Jump if greater", null, "BH", labelMoreThan);
				code += INS("Jump to End", null, "B", labelEnd);
				code += INS("Move 1 in " + REG(this), labelMoreThan, "LCA", NUM_CONST(1, false), REG(this));
				code += INS("End of Greater", labelEnd, "NOP");
			} else {
				// ###############
				// ## WARNING!! ##
				// ###############
				// IS CORRECT HERE TO USE A FIXED VALUE (5) FOR THE VARIABLE SIZE

				code += INS("Compare stack to stack at -5", null, "C", STACK_OFF(0), STACK_OFF(-5));
				code += POP(5);

				code += INS("Move 0 in stack", null, "MCW", NUM_CONST(0, false), STACK_OFF(0));
				code += INS("Jump if greater", null, "BH", labelMoreThan);
				code += INS("Jump to End", null, "B", labelEnd);
				code += INS("Move 1 in stack", labelMoreThan, "MCW", NUM_CONST(1, false), STACK_OFF(0));
				code += INS("End of Greater", labelEnd, "NOP");
			}
			return code;
		} 
		else 
		{
			return l.generateCode(false) + r.generateCode(false);
		}
	}

	@Override
	public Expression getLeftExpression() {
		return l;
	}

	@Override
	public Expression getRightExpression() {
		return r;
	}

	@Override
	public String toString()
	{
		return "(" + l + " > " + r +")";
	}
}
