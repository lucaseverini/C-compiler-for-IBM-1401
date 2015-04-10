/*
	ConstantExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.*;

public class ConstantExpression extends Expression
{
	private final int val; // everything should fit into an int.
	private Boolean blank;

	public ConstantExpression(Type type) 
	{
		super(type);
		
		this.blank = true;
		this.val = 0;
	}

	public ConstantExpression(Type type, int val) 
	{
		super(type);
		
		this.val = val;
		this.blank = false;
	}

	public String generateCode(boolean valueNeeded) 
	{
		if (valueNeeded) 
		{
			if (getType() instanceof PointerType) 
			{
				String constVal = ADDR_CONST(val, false);					
				String code = COM("Constant (" + this.toString() + " : " + constVal + ")"); 
				code += PUSH(getType().sizeof(), constVal);
				
				return code;
			} 
			else if (getType().equals(Type.intType)) 
			{
				String constVal = NUM_CONST(val, false);
				String code = COM("Constant (" + this.toString() + " : " + constVal + ")"); 
				code += PUSH(getType().sizeof(), constVal);
				
				return code;
			} 
			else if (getType().equals(Type.charType)) 
			{
				String constVal = CHAR_CONST(val, false);
				String code = COM("Constant (" + this.toString() + " : " + constVal + ")"); 
				code += PUSH(getType().sizeof(), constVal);
				
				return code;
			} 
			else 
			{
				// oops!
				return null;
			}
		} 
		else 
		{
			return "";
		}
	}

	public Boolean isBlank() 
	{
		return blank;
	}

	public int getValue() 
	{
		return val;
	}
	
	public String toString() 
	{
		if (getType().equals(Type.intType)) 
		{
			return Integer.toString(val);
		}	
		else if (getType().equals(Type.charType)) 
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
			return Integer.toString(val);
		}
	}
}
