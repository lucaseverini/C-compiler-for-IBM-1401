/*
	ForStatement.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import retree.expression.Expression;
import static retree.RetreeUtils.*;

public class ForStatement extends LoopStatement
{
	private Expression init = null;
	private Expression post = null;
	
	public ForStatement(Expression init, Expression condition, Expression post, Statement body) 
	{
		super(condition, body);
		
		if (init != null) 
		{
			this.init = init.collapse();
		}
				
		if (post != null) 
		{
			this.post = post.collapse();
		}
		
		continueLabel = label(SmallCC.nextLabelNumber());
	}

	@Override
	public String generateCode() throws Exception
	{
		int typeSize = condition.getType().sizeof();	

		String code = COM("For " + this.toString());

		if (init!= null) 
		{
			code += init.generateCode(false);
		}
		
		code += INS("Top of For", topLabel, "NOP");
		
		if (condition != null) 
		{
			code += condition.generateCode(true);
			code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0));
			code += POP(typeSize);
			code += INS("Jump to bottom of For", null, "BCE", bottomLabel, STACK_OFF(typeSize), " ");
			code += "\n";
		}
		
		code += body.generateCode();
		code += INS("Continue of For", continueLabel, "NOP");
		
		if (post != null) 
		{
			code += post.generateCode(false);
		}
		
		code += INS("Jump to top of For", null, "B", topLabel);
		code += "\n";
		
		code += INS("Bottom of For", bottomLabel, "NOP");
		
		code += COM("End For " + this.toString());
		code += "\n";
		
		return code;
	}
	
	@Override
    public String toString()
    {
		return "[for (" + init + "; " + condition + "; " + post + ") " +
				body + " top:" + topLabel + " bottom:" + bottomLabel + 
				" continue:" + continueLabel + "]";
	}

	public Expression getPost() {
		return post;
	}

	public Expression getCondition() {
		return condition;
	}

	public Expression getInit() {
		return init;
	}
}

