/*
	LoopStatement.java

    The Small-C cross-compiler for IBM 1401

	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import static retree.RetreeUtils.label;
import retree.expression.Expression;

public abstract class LoopStatement implements Statement
{
	protected Expression condition = null;
	protected Statement body = null;
	protected String topLabel = null;
	protected String bottomLabel = null;
	protected String continueLabel = null;
	protected int size = 0;
	
	public LoopStatement(Expression condition, Statement body)
	{
		this.condition = condition != null ? condition.collapse() : null;
		this.body = body;
		this.size = condition != null ? condition.getType().sizeof() : 0;
		
		this.topLabel = label(SmallCC.nextLabelNumber());
		this.bottomLabel = label(SmallCC.nextLabelNumber());
	}

	@Override
	public String generateCode() throws Exception
	{
		return "";
	}
	
	Statement getBody()
	{
		return body;
	}
	
	String getLoopType()
	{
		String type = "";
		
		if(this instanceof WhileStatement)
		{
			type = "While";
		}
		else if(this instanceof DoWhileStatement)
		{
			type = "Do-While";
		}
		else if(this instanceof ForStatement)
		{
			type = "For";
		}
		
		return type;
	}

	String getBottomLabel()
	{
		return bottomLabel;
	}
}
