/*
	ForStatement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;

import retree.expression.Expression;
import static retree.RetreeUtils.*;

public class ForStatement implements Statement
{
	private Expression init = null, condition = null, post = null;
	private final Statement body;
	
	private final String topLabel;
	private final String bottomLabel;
	private final String continueLabel;
	
	public ForStatement(Expression init, Expression condition, Expression post, Statement body) 
	{
		if (init != null) 
		{
			this.init = init.collapse();
		}
		
		if (condition != null) 
		{
			this.condition = condition.collapse();
		}
		
		if (post != null) 
		{
			this.post = post.collapse();
		}
		
		this.body = body;
		
		topLabel = label(SmallCC.nextLabelNumber());
		bottomLabel = label(SmallCC.nextLabelNumber());
		continueLabel = label(SmallCC.nextLabelNumber());
	}

	public String generateCode() 
	{
		int size = condition.getType().sizeof();	

		String code = COM("ForStatement " + this.toString());

		if (init!= null) 
		{
			code += init.generateCode(false);
		}
		
		code += LBL_INS(topLabel, "NOP");
		
		if (condition != null) 
		{
			code += condition.generateCode(true);
			code += INS("MCS", STACK_OFF(0), STACK_OFF(0));
			code += POP(size);
			code += INS("BCE", bottomLabel, STACK_OFF(size), " ");
		}
		
		code += body.generateCode();
		code += LBL_INS(continueLabel, "NOP");
		
		if (post != null) 
		{
			code += post.generateCode(false);
		}
		
		code += INS("B", topLabel);
		code += LBL_INS(bottomLabel, "NOP");
		
		return code;
	}
	
    public String toString()
    {
		return "(" + init + "; " + condition + "; " + post + ") " + body + " top:" + topLabel + " bottom:" + bottomLabel + " continue:" + continueLabel + ")";
	}
}

