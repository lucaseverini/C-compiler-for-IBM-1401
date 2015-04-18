/*
	CaseStatement.java

    The Small-C cross-compiler for IBM 1401

	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import retree.expression.Expression;

public class CaseStatement implements Statement
{
	private final Expression expression;
	private final boolean defaultExpression;
	private final BlockStatement containerBlock;

	public CaseStatement(Expression expression, BlockStatement containerBlock) 
	{
		this.containerBlock = containerBlock;

		if(expression != null)
		{
			this.expression = expression;
			this.defaultExpression = false;
		}
		else
		{
			this.expression = null;
			this.defaultExpression = true;
		}
		
		System.out.println("Case expression: " + (defaultExpression ? "default" : expression));
	}
	
	@Override
	public String generateCode() throws Exception
	{
		String code = "";
		return code;
	}
	
	@Override
	public String toString()
    {
        return "[case + " + expression + "]";
    }
}
