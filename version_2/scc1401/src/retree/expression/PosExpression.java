/*
	PosExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import retree.exceptions.TypeMismatchException;
import static retree.RetreeUtils.*;
import retree.expression.Expression;
import retree.expression.LValue;
import retree.type.PointerType;
import retree.type.Type;

// a unary plus sign -- acts as an identity function
public class PosExpression extends Expression 
{
	private final Expression child;

	public PosExpression(Expression child) 
	{
		super(child.getType());
		this.child = child;
	}

	public Expression collapse() 
	{
		return child.collapse();
	}

	public String generateCode(boolean valueNeeded) 
	{
		return COM("PosExpression " + this.toString())
					+ child.generateCode(valueNeeded);
	}

	public String toString()
	{
		return "(" + "+" + child + ")";
	}
}
