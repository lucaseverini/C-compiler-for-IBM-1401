/*
	VariableExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.Type;

public class VariableExpression extends LValue 
{
	private final int offset;
	private final boolean isStatic;
	private final String name;

	public VariableExpression(Type type, int offset, boolean isStatic, String name)
	{
		super(type);
		this.offset = offset;
		this.isStatic = isStatic;
		this.name = name;
	}

	public String generateCode(boolean valueNeeded)
	{
		if (valueNeeded) 
		{
			if (isStatic)
			{
				return COM("StaticVariableExpression (" + name + " : " + ADDR_LIT(offset) + ")") 
							+ PUSH(getType().sizeof(), ADDR_LIT(offset));
			} 
			else
			{
				return COM("VariableExpression (" + name + " : " + OFF(offset) + ")") 
							+ PUSH(getType().sizeof(), OFF(offset));
			}
		} 
		else
		{
			return "";
		}
	}

	public LValue collapse() 
	{
		return this;
	}

	public String generateAddress() 
	{
		if (isStatic) 
		{
			return PUSH(3, ADDR_CONST(offset, false));
		} 
		else 
		{
			return PUSH(3, ADDR_CONST(offset, false)) + INS("MA", "X3", STACK_OFF(0));
		}
	}

	public String getAddress() 
	{
		if (isStatic) 
		{
			return offset + "";
		} 
		else 
		{
			return OFF(offset);
		}
	}

	public String getWordMarkAddress() 
	{
		if (isStatic) 
		{
			return (offset + 1 - getType().sizeof()) + "";
		} 
		else 
		{
			return OFF(offset + 1 - getType().sizeof());
		}
	}

	public boolean isStatic() 
	{
		return isStatic;
	}

	public int getOffset() 
	{
		return offset;
	}
	
	public String toString()
	{
		return name;
	}
}
