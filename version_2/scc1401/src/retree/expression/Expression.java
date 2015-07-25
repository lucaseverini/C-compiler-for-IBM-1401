/*
	Expression.java

    The Small-C cross-compiler for IBM 1401

	April-12-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import retree.regalloc.ScopeInterval;
import retree.type.Type;

public abstract class Expression 
{
	private final Type type;

	public ScopeInterval scopeInterval = new ScopeInterval();


	// defaults to being left to right
	// Used for register allocator to tell which side of an expression to look at
	public boolean associativity = true;
	
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

	public abstract Expression getLeftExpression();
	public abstract Expression getRightExpression();
}
