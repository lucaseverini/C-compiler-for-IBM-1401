/*
	PosExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;

// a unary plus sign -- acts as an identity function
public class PosExpression extends Expression 
{
	private final Expression child;

	public PosExpression(Expression child) 
	{
		super(child.getType());
		associativity = false;
		this.child = child;
	}

	@Override
	public Expression collapse() 
	{
		return child.collapse();
	}

	@Override
	public String generateCode(boolean valueNeeded) 
	{
		return COM("PosExpression " + this.toString()) + child.generateCode(valueNeeded);
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
		return "(" + "+" + child + ")";
	}
}
