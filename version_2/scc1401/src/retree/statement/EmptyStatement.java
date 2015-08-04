/*
	EmptyStatement.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import retree.regalloc.RegisterAllocator;

public class EmptyStatement extends Statement
{
	@Override
	public String generateCode(RegisterAllocator registerAllocator)
	{
		return "";
	}
	
	@Override
	public String toString()
    {
        return "[]";
    }
}
