/*
	CaseStatement.java

    The Small-C cross-compiler for IBM 1401

	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import static retree.RetreeUtils.*;
import retree.expression.Expression;

public class CaseStatement implements Statement
{
	private final Expression expression;
	private final boolean defaultExpression;
	private final BlockStatement containerBlock;
	private String label;

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
		
		// System.out.println("Case expression: " + (defaultExpression ? "default" : expression));
	}
	
	@Override
	public String generateCode() throws Exception
	{
		String code = "";
			
		if(defaultExpression)
		{
			code += COM("Default");
			code += INS(null, label, "NOP");
		}
		else
		{
			code += COM("Case: " + expression);
			code += INS(null, label, "NOP");
		}

		return code;
	}
	
	@Override
	public String toString()
    {
        return "[case + " + expression + "]";
    }
	
	public boolean isDefault()
	{
		return defaultExpression;
	}

	public Expression getExpression()
	{
		return expression;
	}
	
	public String getLabel()
	{
		if(label == null)
		{
			label = label(SmallCC.nextLabelNumber());
		}
		
		return label;
	}
}
