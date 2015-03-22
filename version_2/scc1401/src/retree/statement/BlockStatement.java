/*
	BlockStatement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import retree.program.*;
import java.util.*;
import retree.intermediate.*;
import static retree.RetreeUtils.*;

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

	public String generateCode() throws Exception
	{
		String code = "\n";
		Optimizer.addInstruction("***************************************","","");
		code += COM("***************************************");
		Optimizer.addInstruction("BeginBlock " + this.toString(),"","");
		code += COM("BeginBlock " + this.toString());

		for (Initializer i : initializers)
		{
			code += i.generateCode();
		}

		if (stackOffset > 0)
		{
			Optimizer.addInstruction("", "", "MA", ADDR_CONST(stackOffset, false), "X2");
			code += INS(null, null, "MA", ADDR_CONST(stackOffset, false), "X2");
		}

		for (Statement s : statements)
		{
			code += s.generateCode();
		}

		// if we call return from the function, we jump here
		if (returnLabel != null)
		{
			Optimizer.addInstruction("Return", returnLabel, "NOP");
			code += INS("Return", returnLabel, "NOP");
		}

		if (stackOffset > 0)
		{
			Optimizer.addInstruction("", "", "MA", ADDR_CONST(-stackOffset, false), "X2");
			code += INS(null, null, "MA", ADDR_CONST(-stackOffset, false), "X2");
		}

		for (Initializer i : initializers)
		{
			code += i.freeCode();
		}

		// if we were returning, and there are more blocks to escape, keep going up
		if (parentReturnLabel != null)
		{
			Optimizer.addInstruction("Jump back to caller", "", "BCE", parentReturnLabel, "RF", "R");
			code += INS("Jump back to caller", null, "BCE", parentReturnLabel, "RF", "R");
		}
		else
		{
			Optimizer.addInstruction("Clear the Return Flag", "", "MCW", "@ @", "RF");
			// if we reached the top, clear our return flag
			code += INS("Clear the Return Flag", null, "MCW", "@ @", "RF");
		}
		Optimizer.addInstruction("EndBlock " + this.toString(),"","");
		code += COM("EndBlock " + this.toString());
		Optimizer.addInstruction("***************************************","","");
		code += COM("***************************************");
		code += "\n";

		return code;
	}

    public String toString()
    {
        return "[Block " + returnLabel + ":" + parentReturnLabel + "]";
    }
}
