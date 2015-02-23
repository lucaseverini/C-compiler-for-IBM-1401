package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.*;

public class ConstantExpression extends Expression
{
	private final int val; // everything should fit into an int.

	public ConstantExpression(Type type, int val) 
	{
		super(type);
		this.val = val;
	}

	public String generateCode(boolean valueNeeded) 
	{
		if (valueNeeded) 
		{
			if (getType() instanceof PointerType) 
			{
				return COM("ConstantExpression(" + val + ")") + PUSH(getType().sizeof(), ADDR_CONST(val, false));
			} 
			else if (getType().equals(Type.intType)) 
			{
				return COM("ConstantExpression(" + val + ")") + PUSH(getType().sizeof(), NUM_CONST(val, false));
			} 
			else if (getType().equals(Type.charType)) 
			{
				return COM("ConstantExpression(" + val + ")") + PUSH(getType().sizeof(), CHAR_CONST(val, false));
			} 
			else 
			{
				//oops
				return null;
			}
		} 
		else 
		{
			return "";
		}
	}

	public int getValue() 
	{
		return val;
	}
	
	public String toString() 
	{
		if (getType().equals(Type.intType)) 
		{
			return val + "";
		}
		
		if (getType().equals(Type.charType)) 
		{
			switch(val) 
			{
				case 0:
					return "'\\0'";
					
				case '\n':
					return "'\\n'";
					
				default:
					return "'" + ((char)val) + "'";
			}
		}
		else 
		{
			return "(" + val + ")";
		}
	}
}
