/*
	IfStatement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import retree.expression.Expression;
import static retree.RetreeUtils.*;

public class IfStatement implements Statement 
{
	private final Expression condition;
	private final Statement ifClause, elseClause;
	private final String trueLabel, falseLabel;
	private final int size;
	
	public IfStatement(Expression condition, Statement ifClause, Statement elseClause) 
	{
		this.condition = condition.collapse();
		this.ifClause = ifClause;
		this.elseClause = elseClause;
		this.size = condition.getType().sizeof();
		
		// the single label jumped to if the condition is false
		falseLabel = label(SmallCC.nextLabelNumber());
		
		// the single label jumped to if the condition is true	
		if (elseClause != null) 
		{
			trueLabel = label(SmallCC.nextLabelNumber());
		}
		else
		{
			trueLabel = null;
		}
	}

	public String generateCode() throws Exception 
	{		
		String code = COM("If " + this.toString());
		
		code += condition.generateCode(true);
		
		code += INS("Clear WM", null, "MCS", STACK_OFF(0), STACK_OFF(0)); // this removes the word mark
		code += POP(size);
		code += INS("Jump when False", null, "BCE", falseLabel, STACK_OFF(size), " ");
		
		code += ifClause.generateCode();
		
		if (elseClause != null) 
		{
			code += INS("Jump when true", null, "B", trueLabel);
			code += INS("Executed when False", falseLabel, "NOP");
			code += elseClause.generateCode();
			code += INS("Executed when True", trueLabel, "NOP");
		} 
		else 
		{
			code += INS("Executed when False", falseLabel, "NOP");
		}
		
		return code;
	}

    public String toString()
    {
        return "[if (" + condition + " then " + ifClause + (elseClause != null ? (" else " + elseClause) : "") + "]";
    }
}

