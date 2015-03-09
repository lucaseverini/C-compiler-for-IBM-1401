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

	public String generateCode() 
	{		
		String code = COM("IfStatement " + this.toString());
		
		code += condition.generateCode(true);
		
		code += INS("MCS", STACK_OFF(0), STACK_OFF(0)); // this removes the word mark
		code += POP(size);
		code += INS("BCE", falseLabel, STACK_OFF(size), " ");
		
		code += ifClause.generateCode();
		
		if (elseClause != null) 
		{
			code += INS("B", trueLabel);
			code += LBL_INS(falseLabel, "NOP");
			code += elseClause.generateCode();
			code += LBL_INS(trueLabel, "NOP");
		} 
		else 
		{
			code += LBL_INS(falseLabel, "NOP");
		}
		
		return code;
	}

    public String toString()
    {
        return "if(" + condition + " then " + ifClause + (elseClause != null ? (" else " + elseClause) : "") + ")";
        // return "if(" + condition + " then " + trueLabel + (elseClause != null ? (" else " + falseLabel) : "") + ")";
    }
}

