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
	private final String returnLabel;
	private final String parentReturnLabel;
	private final BlockStatement containerBlock;
	private List<Initializer> initializers;
	private List<Statement> statements;
	private int stackFrameSize;
		
	public BlockStatement(String returnLabel, String parentReturnLabel, BlockStatement containerBlock)
	{
		this.returnLabel = returnLabel;
		this.parentReturnLabel = parentReturnLabel;
		this.containerBlock = containerBlock;
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
		
		if (stackFrameSize < 0) 
		{
			throw new Exception("BlockStatement " + this.toString() + " : stack offset can't be negative");
		}
		
		code += PUSH(stackFrameSize);

		for (Statement s : statements) 
		{
			code += s.generateCode();
		}

		// if we call return from the function, we jump here
		if (returnLabel != null) 
		{
			code += INS("Last block instruction", returnLabel, "NOP");
		}
		
		code += POP(stackFrameSize);
		
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
		
	public List<Statement> getStatements()
	{
		return statements;
	}

	public List<Initializer> getInitializers()
	{
		return initializers;
	}

	public int getStackFrameSize()
	{
		return stackFrameSize;
	}

	public BlockStatement getContainerBlock()
	{
		return containerBlock;
	}

	public void setInitializers(List<Initializer> initializers)
    {
        this.initializers = initializers;
    }

	public void setStatements(List<Statement> statements)
    {
        this.statements = statements;
    }

	public void setStackFrameSize(int stackFrameSize)
    {
        this.stackFrameSize = stackFrameSize;
    }
}
