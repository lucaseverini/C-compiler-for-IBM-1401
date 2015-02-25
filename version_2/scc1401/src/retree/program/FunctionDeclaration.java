package retree.program;

import retree.type.*;

public class FunctionDeclaration 
{
	private final String label;
	private final FunctionType type;
	
	public FunctionDeclaration(String label, FunctionType type) 
	{
		this.label = label;
		this.type = type;
	}
	
	public String getLabel() 
	{
		return label;
	}
	
	public FunctionType getType() 
	{
		return type;
	}
}
