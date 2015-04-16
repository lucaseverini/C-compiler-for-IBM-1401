/*
	BlockStatement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import retree.program.*;
import java.util.*;
import static retree.RetreeUtils.*;
import retree.expression.VariableExpression;

public class BlockStatement implements Statement 
{
	private final List<Initializer> initializers;
	private final List<Statement> statements;
	private final int stackOffset;
	private final String returnLabel, parentReturnLabel;
		
	public BlockStatement(List<Initializer> initializers, List<Statement> statements, int stackOffset, String returnLabel, String parentReturnLabel)
	{
		this.initializers = initializers;
		this.statements = statements;
		this.stackOffset = stackOffset;
		this.returnLabel = returnLabel;
		this.parentReturnLabel = parentReturnLabel;
	}
	
	@Override
	public String generateCode() throws Exception
	{
		String code = "\n";
		
		code += COM("***************************************");
		code += COM("Begin " + this.toString());

		for (Initializer i : initializers) 
		{
			VariableExpression v = i.getVariable();
			String name = v.getName();
			if(name != null && name.length() > 0)
			{
				code += COM(name + " size:" + v.getType().getSize() + " offset:" + v.getOffset());
			}
		}

		code += "\n";
		
		for (Initializer i : initializers) 
		{
			code += i.generateCode();
		}
		
		if (stackOffset < 0) 
		{
			throw new Exception("BlockStatement " + this.toString() + " : stack offset can't be negative");
		}
		
		code += PUSH(stackOffset);
		
		for (Statement s : statements) 
		{
			code += s.generateCode();
		}
		
		// if we call return from the function, we jump here
		if (returnLabel != null) 
		{
			code += INS("Last block instruction", returnLabel, "NOP");
		}
		
		code += POP(stackOffset);
		
		for (Initializer i : initializers)
		{
			code += i.freeCode();
		}
		
		if (parentReturnLabel != null) 
		{
			// if we were returning, and there are more blocks to escape, keep going up
			//code += INS("Jump back to caller", null, "BCE", parentReturnLabel, "RF", "R");
			//code += INS("Jump back to caller", null, "B", parentReturnLabel);
		} 
		else 
		{
			// if we reached the top, clear our return flag
			// code += INS("Clear the Return Flag", null, "MCW", "@ @", "RF");
		}
		
		code += COM("End " + this.toString());
		code += COM("***************************************");
		code += "\n";
		
		return code;
	}

	@Override
    public String toString()
    {
        return "[Block ending at " + returnLabel + "]";
    }
}
