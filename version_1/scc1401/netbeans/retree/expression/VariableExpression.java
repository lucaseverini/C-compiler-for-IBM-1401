package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.Type;

public class VariableExpression extends LValue 
{
	private int offset;
	private boolean isStatic;
	private String name;
	private String label;

	public VariableExpression(Type type, int offset, boolean isStatic, String name)
	{
		super(type);
		
		this.offset = offset;
		this.isStatic = isStatic;
		this.name = name;
		this.label = null;
	}

	public String generateCode(boolean valueNeeded)
	{
		String code = "";
		
		if (valueNeeded) 
		{
			if (isStatic)
			{
				// Generare una label per VAR_CONST a cui verra' associato un valore (indirizzo) solo dopo la generazione dei DCW della variabile
				if(label == null)
				{
					label = retree.RetreeUtils.VAR_CONST(this, false);
				}
				
				code = COM("VariableExpression(" + name + ":" + offset + ":" + isStatic + ")") 
					   + PUSH(getType().sizeof(), label);

				//code = COM("VariableExpression(" + name + ":" + offset + ":" + isStatic + ")") 
				//	   + PUSH(getType().sizeof(), ADDR_LIT(offset));
			} 
			else
			{
				code = COM("VariableExpression(" + name + ":" + offset + ":" + isStatic + ")") 
					   + PUSH(getType().sizeof(), OFF(offset));
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
		String code;
		
		if (isStatic) 
		{
			// Generare una label per VAR_CONST a cui verra' associato un valore (indirizzo) solo dopo la generazione dei DCW della variabile
			if(label == null)
			{
				label = retree.RetreeUtils.VAR_CONST(this, false);
			}

			code = PUSH(3, label);
		} 
		else 
		{
			code = PUSH(3, ADDR_CONST(offset, false)) + INS("MA", "X3", STACK_OFF(0));
		}
		
		return code;
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
