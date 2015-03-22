/*
	SubscriptExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;

import retree.expression.Expression;
import retree.intermediate.*;
import static retree.RetreeUtils.*;

public class WhileStatement implements Statement
{
	private final Expression condition;
	private final Statement body;
	private final String topLabel, bottomLabel;
	private final int size;

	public WhileStatement(Expression condition, Statement body)
	{
		this.condition = condition.collapse();
		this.body = body;
		// the single label jumped to if the condition is false
		this.topLabel = label(SmallCC.nextLabelNumber());
		// the single label jumped to if the condition is true
		this.bottomLabel = label(SmallCC.nextLabelNumber());
		this.size = condition.getType().sizeof();
	}

	public String generateCode() throws Exception
	{
		Optimizer.addInstruction("While " + this.toString(),"","");
		String code = COM("While " + this.toString());
		Optimizer.addInstruction("Top of the loop", topLabel, "NOP");
		code += INS("Top of the loop", topLabel, "NOP");

		code += condition.generateCode(true);
		Optimizer.addInstruction("Clear WM", "", "MCS", STACK_OFF(0), STACK_OFF(0));
		code += INS("Clear WM", null, "MCS", STACK_OFF(0), STACK_OFF(0)); // this removes the word mark
		code += POP(size);
		Optimizer.addInstruction("Jump to bottom", "", "BCE", bottomLabel, STACK_OFF(size), " ");
		code += INS("Jump to bottom", null, "BCE", bottomLabel, STACK_OFF(size), " ");

		code += body.generateCode();
		Optimizer.addInstruction("Jump to top", "", "B", topLabel);
		code += INS("Jump to top", null, "B", topLabel);
		Optimizer.addInstruction("Bottom of the while loop", bottomLabel, "NOP");
		code += INS("Bottom of the while loop", bottomLabel, "NOP");
		code += "\n";

		return code;
	}

    public String toString()
    {
		return "[while (" + condition + ") " + body + " top:" + topLabel + " bottom:" + bottomLabel + "]";
	}
}
