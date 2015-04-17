/*
	IdentifierStatement.java

    Small-C compiler - SJSU
	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import static retree.RetreeUtils.*;

public class IdentifierStatement implements Statement
{
	private final String identifier;
	private final String label;
	
	public IdentifierStatement(String identifier) 
	{
		this.identifier = identifier;
		this.label = label(SmallCC.nextLabelNumber());
	}
	
	@Override
	public String generateCode() throws Exception
	{
		String code = "";	
		
		code += COM("Label: " + identifier);
		code += INS(null, label, "NOP");
		code += "\r";
		
		return code;
	}
	
	@Override
	public String toString()
    {
        return "[identifier: " + label + "]";
    }
}
