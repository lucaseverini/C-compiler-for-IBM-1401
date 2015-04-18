/*
	SwitchStatement.java

    The Small-C cross-compiler for IBM 1401

	April-17-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import static retree.RetreeUtils.COM;
import retree.expression.Expression;

public class SwitchStatement implements Statement
{
	private final Expression expression;
	private final Statement body;
	
	public SwitchStatement(Expression expression, Statement body) 
	{
		this.expression = expression;
		this.body = body;
	}
	
	@Override
	public String generateCode() throws Exception
	{
		String code = COM("##### Switch (" + expression + ") #####");
		return code;
	}
	
	@Override
	public String toString()
    {
        return "[switch]";
    }
}
