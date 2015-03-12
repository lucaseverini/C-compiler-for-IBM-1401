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
		String code = COM("DoWhileStatement " + toString());
		code += LBL_INS(topLabel, "NOP");
		
		code += body.generateCode();	
		code += condition.generateCode(true);
		
		code += INS("MCS", STACK_OFF(0), STACK_OFF(0));
		code += POP(size);
		code += INS("BCE", bottomLabel, STACK_OFF(size), " ");
		code += INS("B", topLabel);
		code += LBL_INS(bottomLabel, "NOP");
		
		return code;
	}

    public String toString()
    {
		return "(" + condition + ") " + body + " top:" + topLabel + " bottom:" + bottomLabel + ")";
	}
}

