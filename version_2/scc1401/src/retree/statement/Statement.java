/*
	Statement.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import retree.expression.Expression;
import retree.regalloc.RegisterAllocator;

import java.util.ArrayList;

public abstract class Statement implements IStatement
{
	public ArrayList<Expression> expressionList = new ArrayList<>();
//	public void registerAlloc() throws RegisterAllocationException;
}
