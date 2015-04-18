/*
	IdentifierStatement.java

    The Small-C cross-compiler for IBM 1401

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
		this.label = SmallCC.getLabelForIdentifier(identifier);
	}
	
	@Override
	public String generateCode() throws Exception
	{
		String code = "";	
		
		code += INS(identifier + ":", label, "NOP");
		code += "\r";
		
		return code;
	}
	
	@Override
	public String toString()
    {
        return "[identifier: " + label + "]";
    }
	
	public String getLabel()
    {
        return label;
    }

	public String getIdentifier()
    {
        return identifier;
    }
}
