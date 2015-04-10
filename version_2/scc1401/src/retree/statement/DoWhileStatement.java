/*
	DoWhileStatement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
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
		String code = COM("Do-While " + toString());
		
		code += INS("Top of Do-While", topLabel, "NOP");
		
		code += body.generateCode();	
		code += condition.generateCode(true);
		
		code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0));
		
		code += POP(size);
		code += INS("Jump to bottom of Do-While", null, "BCE", bottomLabel, STACK_OFF(size), " ");
		code += "\n";
		
		code += INS("Jump to top of Do-While", null, "B", topLabel);
		code += "\n";
		
		code += INS("Bottom of Do-While", bottomLabel, "NOP");
		
		code += COM("End Do-While " + this.toString());
		code += "\n";
		
		return code;
	}

    public String toString()
    {
		return "[do-while (" + condition + ") " + body + " top:" + topLabel + " bottom:" + bottomLabel + "]";
	}
}

