/*
	GotoStatement.java

    The Small-C cross-compiler for IBM 1401

	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import retree.regalloc.RegisterAllocator;

import java.util.List;
import static retree.RetreeUtils.*;

public class GotoStatement extends Statement
{
	private final String identifier;
	private final String label;
	private final BlockStatement containerBlock;
	
	public GotoStatement(String identifier, BlockStatement containerBlock) 
	{
		this.identifier = identifier;
		this.containerBlock = containerBlock;
		this.label = SmallCC.getLabelForIdentifier(identifier);
	}
	
	@Override
	public String generateCode(RegisterAllocator registerAllocator) throws Exception
	{
		String code = "";
		
		IdentifierStatement ident = findIdentifier();
		if(ident == null)
		{
			throw new Exception("Identifier " + identifier + " not found");
		}
				
		int frameSize = getStackFrameSize(ident);
		if(frameSize != 0)
		{
			code += POP(frameSize);
		}
		
		code += INS("Goto " + identifier, null, "B", label);
		
		return code;
	}
	
	@Override
	public String toString()
    {
        return "[Goto: " + label + "]";
    }

	public String getLabel()
    {
        return label;
    }

	public String getIdentifier()
    {
        return identifier;
    }

	IdentifierStatement findIdentifier()
	{
		BlockStatement block = containerBlock;
		while(block != null)
		{			
			List<Statement> statements = block.getStatements();
			for(Statement s : statements)
			{
				if(s instanceof IdentifierStatement && ((IdentifierStatement)s).getIdentifier().equals(identifier))
				{
					return (IdentifierStatement)s;
				}
			}
			
			block = block.getContainerBlock();
		}
		
		return null;
	}

	int getStackFrameSize(IdentifierStatement identifier)
	{
		int frameSize = 0;

		BlockStatement block = containerBlock;		
		while(block != null)
		{
			List<Statement> statements = block.getStatements();
			if(statements.contains(identifier))
			{
				return frameSize;
			}
						
			frameSize += block.getStackFrameSize();
			
			block = block.getContainerBlock();
		}
		
		return 0;
	}
}
