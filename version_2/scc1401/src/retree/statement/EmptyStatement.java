/*
	EmptyStatement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

public class EmptyStatement implements Statement
{
	public String generateCode() 
	{
		return "";
	}
}
