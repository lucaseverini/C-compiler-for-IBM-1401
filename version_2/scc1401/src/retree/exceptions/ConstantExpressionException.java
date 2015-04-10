/*
	ConstantExpressionException.java

    Small-C compiler - SJSU
	March-31-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.exceptions;

import retree.expression.Expression;

public class ConstantExpressionException extends Exception 
{
	private Expression exp;

	public ConstantExpressionException(Expression exp) 
	{
		this.exp = exp;
	}
	
	public String toString()
	{
		return "Expression " + exp + " is a constant";
	}
}
