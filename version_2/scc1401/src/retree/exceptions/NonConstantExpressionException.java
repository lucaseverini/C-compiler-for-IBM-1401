/*
	NonConstantExpressionException.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.exceptions;

import retree.expression.Expression;

public class NonConstantExpressionException extends Exception 
{
	private static final long serialVersionUID = 1L;
	private final Expression exp;

	public NonConstantExpressionException(Expression exp) 
	{
		this.exp = exp;
	}
	
	@Override
	public String toString()
	{
		return "Expression " + exp + " is not a compile-time constant";
	}
}
