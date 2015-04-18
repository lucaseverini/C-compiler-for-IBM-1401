/*
	EmptyStatement.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

public class EmptyStatement implements Statement
{
	@Override
	public String generateCode() 
	{
		return "";
	}
	
	@Override
	public String toString()
    {
        return "[]";
    }
}
