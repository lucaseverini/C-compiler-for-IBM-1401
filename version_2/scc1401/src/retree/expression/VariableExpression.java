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
		String code = "";
		
		if (valueNeeded) 
		{
			if (isStatic)
			{
				code = COM("StaticVariableExpression (" + name + " : " + ADDR_LIT(offset) + ")"); 
				code += PUSH(getType().sizeof(), ADDR_LIT(offset));
			} 
			else
			{
				code = COM("VariableExpression (" + name + " : " + OFF(offset) + ")"); 
				code += PUSH(getType().sizeof(), OFF(offset));
			}
		} 
		
		return code;
	}

	public LValue collapse() 
	{
		return this;
	}

	public String generateAddress() 
	{
		String code = "";
		
		if (isStatic) 
		{
			code = PUSH(3, ADDR_CONST(offset, false));
		} 
		else 
		{
			code = PUSH(3, ADDR_CONST(offset, false)) + INS("MA", "X3", STACK_OFF(0));
		}
		
		return code;
	}

	public String getAddress() 
	{
		String code = "";
		
		if (isStatic) 
		{
			code = offset + "";
		} 
		else 
		{
			code =  OFF(offset);
		}
		
		return code;
	}

	public String getWordMarkAddress() 
	{
		String code = "";

		if (isStatic) 
		{
			code = (offset + 1 - getType().sizeof()) + "";
		} 
		else 
		{
			code = OFF(offset + 1 - getType().sizeof());
		}
		
		return code;
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
