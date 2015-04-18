/*
	NegExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
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
				code += INS(null, null, "ZS", STACK_OFF(0));
				code += SNIP("clean_number");
			}
		}
		
		return code;
	}

	@Override
	public String toString()
	{
		return "(" + "-" + child + ")";
	}
}
