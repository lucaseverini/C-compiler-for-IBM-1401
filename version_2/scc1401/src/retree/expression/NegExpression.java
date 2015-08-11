/*
	NegExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;

import compiler.SmallCC;
import retree.type.PointerType;

public class NegExpression extends Expression 
{
	private final Expression child;

	public NegExpression(Expression child) 
	{
		super(child.getType());
		
		this.child = child;
	}

	@Override
	public Expression collapse()
	{
		Expression collapsedChild = child.collapse();
		
		if (collapsedChild instanceof ConstantExpression)
		{
			return new ConstantExpression(collapsedChild.getType(), -((ConstantExpression)collapsedChild).getValue());
		}
		else
		{
			return new NegExpression(collapsedChild);
		}
	}

	@Override
	public String generateCode(boolean valueNeeded)
	{
		String code = COM("Negate " + this.toString());
		code += child.generateCode(valueNeeded);
		
		if (valueNeeded) 
		{
			if (child.getType() instanceof PointerType) 
			{
				COM("// Check for PointerType");
				INS("HALT!", null, "H");
			}
			else 
			{
				if (SmallCC.nostack)
				{
					code += INS(null, null, "ZS", REG(this));
					if (SmallCC.nostack)
					{
						code += INS("Move child val to CAST reg", null, "MCW", REG(this), "CAST");
					}
					code += SNIP("clean_number");
					if (SmallCC.nostack)
					{
						code += INS("Move result to " + REG(this), null, "LCA", "CAST", REG(this));
					}
				} else {
					code += INS(null, null, "ZS", STACK_OFF(0));
					code += SNIP("clean_number");
				}
			}
		}
		
		return code;
	}

	@Override
	public Expression getLeftExpression() {
		return child;
	}

	@Override
	public Expression getRightExpression() {
		return null;
	}

	@Override
	public String toString()
	{
		return "(" + "-" + child + ")";
	}
}
