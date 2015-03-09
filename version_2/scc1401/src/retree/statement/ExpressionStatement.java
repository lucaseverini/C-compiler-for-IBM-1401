/*
	ExpressionStatement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import retree.expression.Expression;

public class ExpressionStatement implements Statement
{
	private final Expression exp;
	
	public ExpressionStatement(Expression exp)
	{
		this.exp = exp.collapse();
	}

	public String generateCode()
	{
		return exp.generateCode(false);
	}
}
