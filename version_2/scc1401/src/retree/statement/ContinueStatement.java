/*
	ContinueStatement.java

    The Small-C cross-compiler for IBM 1401

	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;
import java.util.List;
import static retree.RetreeUtils.*;

public class ContinueStatement implements Statement
{
	private final BlockStatement containerBlock;

	public ContinueStatement(BlockStatement containerBlock) 
	{
		this.containerBlock = containerBlock;
	}
	
	@Override
	public String generateCode() throws Exception
	{
		String code = "";
		
		LoopStatement loop = findEnclosingLoop();
		if(loop == null)
		{
			throw new Exception("Continue outside of loop error");
		}

		int frameOffset = getStackFrameSize(loop);
		if(frameOffset != 0)
		{
			code += POP(frameOffset);
		}

		String label = loop.continueLabel != null ? loop.continueLabel : loop.topLabel;		
		code += INS("Continue in the " + loop.getLoopType() + " loop", null, "B", label);
		
		return code;
	}
	
	@Override
	public String toString()
    {
        return "[continue]";
    }
	
	LoopStatement findEnclosingLoop()
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
					return (LoopStatement)s;
				}
			}
			
			block = parentBlock;
			parentBlock = parentBlock.getContainerBlock();
		}
		
		return null;
	}

	int getStackFrameSize(LoopStatement loop)
	{
		BlockStatement block = containerBlock;
		int frameOffset = block.getStackFrameSize();
		
		BlockStatement parentBlock = block.getContainerBlock();
		while(parentBlock != null)
		{
			List<Statement> statements = parentBlock.getStatements();
			if(statements.contains(loop))
			{
				return frameOffset;
			}
			
			block = parentBlock;
			parentBlock = parentBlock.getContainerBlock();
			
			frameOffset += block.getStackFrameSize();
		}
		
		return 0;
	}
}
