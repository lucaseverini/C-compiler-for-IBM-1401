/*
	Expression.java

    Small-C compiler - SJSU
	April-12-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import retree.type.Type;

public abstract class Expression 
{
	private final Type type;
	
	public Expression(Type type) 
	{
		this.type = type;
	}

	public Type getType()
	{
		return type;
	}

	public Expression collapse() 
	{
		return this;
	}
	
	public abstract String generateCode(boolean valueNeeded);
}
