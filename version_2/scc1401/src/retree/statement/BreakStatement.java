/*
	BreakStatement.java

    The Small-C cross-compiler for IBM 1401

	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import retree.regalloc.RegisterAllocator;

import java.util.List;
import static retree.RetreeUtils.*;

public class BreakStatement extends Statement
{
	private final BlockStatement containerBlock;

	public BreakStatement(BlockStatement containerBlock) 
	{
		this.containerBlock = containerBlock;
	}
	
	@Override
	public String generateCode(RegisterAllocator registerAllocator) throws Exception
	{
		String code = "";
		String bottomlabel;
		
		Statement stat = findEnclosingStatement();
		if(stat == null)
		{
			throw new Exception("Break outside of valid statement body error");
		}
		
		if(stat instanceof LoopStatement)
		{
			bottomlabel = ((LoopStatement)stat).bottomLabel;
		}
		else if(stat instanceof SwitchStatement)
		{
			bottomlabel = ((SwitchStatement)stat).getBottomLabel();
		}
		else
		{
			throw new Exception("Break outside of valid statement body error");
		}
				
		int frameSize = getStackFrameSize(stat);
		if(frameSize != 0)
		{
			code += POP(frameSize);
		}
		
		if(stat instanceof SwitchStatement)
		{
			code += INS("Break out of the switch block", null, "B", bottomlabel);
		}
		else if(stat instanceof LoopStatement)
		{
			code += INS("Break out of the loop block", null, "B", bottomlabel);
		}
		
		return code;
	}
	
	@Override
	public String toString()
    {
        return "[break]";
    }
	
	Statement findEnclosingStatement()
	{
		BlockStatement block = containerBlock;
		BlockStatement parentBlock = block.getContainerBlock();

		while(parentBlock != null)
		{
			List<Statement> statements = parentBlock.getStatements();
			for(Statement s : statements)
			{
				if(s instanceof LoopStatement && ((LoopStatement)s).getBody() == block)
				{
					return s;
				}
				
				if(s instanceof SwitchStatement && ((SwitchStatement)s).getBody() == block)
				{
					return s;
				}
			}
			
			block = parentBlock;
			parentBlock = parentBlock.getContainerBlock();
		}
		
		return null;
	}
	
	int getStackFrameSize(Statement loop)
	{
		BlockStatement block = containerBlock;
		int frameSize = block.getStackFrameSize();
		
		BlockStatement parentBlock = block.getContainerBlock();
		while(parentBlock != null)
		{
			List<Statement> statements = parentBlock.getStatements();
			if(statements.contains(loop))
			{
				return frameSize;
			}
			
			block = parentBlock;
			parentBlock = parentBlock.getContainerBlock();
			
			frameSize += block.getStackFrameSize();
		}
		
		return 0;
	}
}
