/*
	ExpressionStatement.java

    The Small-C cross-compiler for IBM 1401

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

	public Expression getExpression(){return exp;}

	@Override
	public String generateCode()
	{
		return exp.generateCode(false);
	}

	@Override
	public String toString()
    {
       return exp.toString();
    }
}
