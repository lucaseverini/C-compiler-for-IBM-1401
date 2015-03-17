/*
	ArrayNameExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import retree.type.*;
import static retree.RetreeUtils.*;

public class ArrayNameExpression extends Expression
{
	private final VariableExpression array;
	private final ArrayType arrayType;

	public ArrayNameExpression(VariableExpression array) 
	{
		super(new PointerType(((ArrayType) array.getType()).getArrayBaseType()));
		this.array = array;
		this.arrayType = (ArrayType) array.getType();
	}

	// For stack management reasons, address of a pointer variable is at the end
	// of that variable we need to get a pointer to the beginning.
	public String generateCode(boolean valueNeeded) 
	{
		String code = "";
		
		if (!valueNeeded) 
		{
			return code;
		}
		
		if (array.isStatic()) 
		{
			code += COM("Static Array (" + array + ":" + arrayType + ")");
			code += PUSH(3, ADDR_CONST(array.getOffset(), false));
		} 
		else 
		{
			if (array.isParameter())
			{
				code += COM("Parameter Array (" + array + ":" + arrayType + ")");
				code += PUSH(3, ADDR_CONST(array.getOffset() + arrayType.getArrayBaseType().sizeof() - arrayType.sizeof(), false));
				code += INS(null, null, "MA", "X3", STACK_OFF(0));
			}
			else
			{
				code += COM("Local Array (" + array + ":" + arrayType + ")");
				code += PUSH(3, ADDR_CONST(array.getOffset() + arrayType.getArrayBaseType().sizeof(), false));
				code += INS(null, null, "MA", "X3", STACK_OFF(0));
			}
		}
		
		return code;
	}

	public VariableExpression getArray()
	{
		return array;
	}

	public Expression collapse() 
	{
		if (this.array.isStatic())
		{
			int value = array.getOffset();
			return new ConstantExpression(new PointerType(((ArrayType) array.getType()).getArrayBaseType()), value);
		}
		
		return this;
	}

	public String toString() 
	{
		return array.toString();
	}
}
