/*
	Statement.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

public interface Statement 
{
	public String generateCode() throws Exception;
}
