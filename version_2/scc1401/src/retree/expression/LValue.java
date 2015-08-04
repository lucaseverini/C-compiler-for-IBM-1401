/*
	LValue.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import retree.type.Type;

public abstract class LValue extends Expression 
{
	public LValue(Type type) 
	{
		super(type);
	}

	@Override
	public abstract LValue collapse();
	
	// like generateCode, but pushes the memory location to the stack instead
	// side effects should still occur
	public abstract String generateAddress();

	public abstract void setOffset(int offset);

}
