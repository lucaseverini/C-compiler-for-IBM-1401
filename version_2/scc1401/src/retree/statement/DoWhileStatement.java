/*
	DoWhileStatement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import retree.intermediate.*;
import retree.expression.Expression;
import static retree.RetreeUtils.*;

public class DoWhileStatement implements Statement
{
	private final Expression condition;
	private final Statement body;
	private final String topLabel, bottomLabel;
	private final int size;

	public DoWhileStatement(Expression condition, Statement body)
	{
		this.condition = condition.collapse();
		this.body = body;
		this.size = condition.getType().sizeof();

		// the single label jumped to if the condition is false
		this.topLabel = label(SmallCC.nextLabelNumber());

		// the single label jumped to if the condition is true
		this.bottomLabel = label(SmallCC.nextLabelNumber());
	}

	public String generateCode() throws Exception
	{
		Optimizer.addInstruction("Do-While " + toString(),"","");
		String code = COM("Do-While " + toString());
		Optimizer.addInstruction("Top of the loop", topLabel, "NOP");
		code += INS("Top of the loop", topLabel, "NOP");

		code += body.generateCode();
		code += condition.generateCode(true);
		Optimizer.addInstruction("Clear WM", "", "MCS", STACK_OFF(0), STACK_OFF(0));
		code += INS("Clear WM", null, "MCS", STACK_OFF(0), STACK_OFF(0));
		code += POP(size);
		Optimizer.addInstruction("Jump to bottom", "", "BCE", bottomLabel, STACK_OFF(size), " ");
		code += INS("Jump to bottom", null, "BCE", bottomLabel, STACK_OFF(size), " ");
		Optimizer.addInstruction("Jump to top", "", "B", topLabel);
		code += INS("Jump to top", null, "B", topLabel);
		Optimizer.addInstruction("Bottom of the do-while loop", bottomLabel, "NOP");
		code += INS("Bottom of the do-while loop", bottomLabel, "NOP");
		code += "\n";

		return code;
	}

    public String toString()
    {
		return "[do-while (" + condition + ") " + body + " top:" + topLabel + " bottom:" + bottomLabel + "]";
	}
}
