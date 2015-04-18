/*
	FunctionDeclaration.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

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
