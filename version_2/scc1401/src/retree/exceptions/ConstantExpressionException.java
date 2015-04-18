/*
	ConstantExpressionException.java

    The Small-C cross-compiler for IBM 1401

	March-31-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.exceptions;

import retree.expression.Expression;

public class ConstantExpressionException extends Exception 
{
	private static final long serialVersionUID = 1L;
	private final Expression exp;

	public ConstantExpressionException(Expression exp) 
	{
		this.exp = exp;
	}
	
	@Override
	public String toString()
	{
		return "Expression " + exp + " is a constant";
	}
}
