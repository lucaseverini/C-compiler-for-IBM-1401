/*
	BreakStatement.java

    Small-C compiler - SJSU
	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;
import java.util.List;
import static retree.RetreeUtils.*;

public class BreakStatement implements Statement
{
	private final BlockStatement containerBlock;

	public BreakStatement(BlockStatement containerBlock) 
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
			throw new Exception("Break outside of loop error");
		}
				
		int frameSize = getStackFrameSize(loop);
		if(frameSize != 0)
		{
			code += POP(frameSize);
		}
		
		String label = loop.bottomLabel;
		code += INS("Break out of the loop", null, "B", label);
		
		return code;
	}
	
	@Override
	public String toString()
    {
        return "[break]";
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
