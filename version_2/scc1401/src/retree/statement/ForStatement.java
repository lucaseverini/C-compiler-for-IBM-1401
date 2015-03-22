/*
	ForStatement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import retree.expression.Expression;
import retree.intermediate.*;
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

	public String generateCode() throws Exception
	{
		int size = condition.getType().sizeof();
		Optimizer.addInstruction("For " + this.toString(),"","");
		String code = COM("For " + this.toString());

		if (init!= null)
		{
			code += init.generateCode(false);
		}
		Optimizer.addInstruction("Top of the loop", topLabel, "NOP");
		code += INS("Top of the loop", topLabel, "NOP");

		if (condition != null)
		{
			code += condition.generateCode(true);
			Optimizer.addInstruction("Clear WM", "", "MCS", STACK_OFF(0), STACK_OFF(0));
			code += INS("Clear WM", null, "MCS", STACK_OFF(0), STACK_OFF(0));
			code += POP(size);
			Optimizer.addInstruction("Jump to bottom", "", "BCE", bottomLabel, STACK_OFF(size), " ");
			code += INS("Jump to bottom", null, "BCE", bottomLabel, STACK_OFF(size), " ");
		}

		code += body.generateCode();
		Optimizer.addInstruction("Continue of the loop", continueLabel, "NOP");
		code += INS("Continue of the loop", continueLabel, "NOP");

		if (post != null)
		{
			code += post.generateCode(false);
		}
		Optimizer.addInstruction("Jump to top", "", "B", topLabel);
		code += INS("Jump to top", null, "B", topLabel);
		Optimizer.addInstruction("Bottom of the for loop", bottomLabel, "NOP");
		code += INS("Bottom of the for loop", bottomLabel, "NOP");
		code += "\n";

		return code;
	}

    public String toString()
    {
		return "[for (" + init + "; " + condition + "; " + post + ") " +
				body + " top:" + topLabel + " bottom:" + bottomLabel +
				" continue:" + continueLabel + "]";
	}
}
