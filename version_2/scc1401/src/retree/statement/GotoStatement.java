/*
	GotoStatement.java

    Small-C compiler - SJSU
	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import static retree.RetreeUtils.*;

public class GotoStatement implements Statement
{
	private final String identifier;
	private final String label;
	private final BlockStatement containerBlock;
	
	public GotoStatement(String identifier, BlockStatement containerBlock) 
	{
		this.identifier = identifier;
		this.containerBlock = containerBlock;
		this.label = label(SmallCC.nextLabelNumber());
	}
	
	@Override
	public String generateCode() throws Exception
	{
		String code = "";	
		
		code += COM("Goto: " + identifier);
		code += INS(null, null, "NOP");
		code += "\r";
		
		return code;
	}
	
	@Override
	public String toString()
    {
        return "[Goto: " + label + "]";
    }
}
