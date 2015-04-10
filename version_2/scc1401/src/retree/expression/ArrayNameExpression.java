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

	public String generateCodeForConstSubscript(boolean valueNeeded, int constSubscript)
	{
		String code = "";
		
		if (!valueNeeded) 
		{
			return code;
		}
		
		if (array.isStatic()) 
		{
			code += COM("Static Array (" + array + ":" + arrayType + ")");
			
			int offset = array.getOffset();
			int baseTypeSize = arrayType.getArrayBaseType().sizeof();
			int arrayOffset = offset + (baseTypeSize - 1) + (baseTypeSize * constSubscript);
			
			code += PUSH(3, ADDR_CONST(arrayOffset, false));
		} 
		else 
		{
			if (array.isParameter())
			{
				code += COM("Parameter Array (" + array + ":" + arrayType + ")");
				
				int offset = array.getOffset();
				int baseTypeSize = arrayType.getArrayBaseType().sizeof();
				int arrayTypeSize = arrayType.sizeof();
				int arrayOffset = offset + baseTypeSize - arrayTypeSize + (baseTypeSize * constSubscript);
				
				code += PUSH(3, ADDR_CONST(arrayOffset, false));
				code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
			}
			else
			{
				code += COM("Local Array (" + array + ":" + arrayType + ")");

				int offset = array.getOffset();
				int baseTypeSize = arrayType.getArrayBaseType().sizeof();
				int arrayOffset = offset + baseTypeSize + (baseTypeSize * constSubscript);

				code += PUSH(3, ADDR_CONST(arrayOffset, false));
				code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
			}
		}
		
		return code;
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

			int offset = array.getOffset();
			int baseTypeSize = arrayType.getArrayBaseType().sizeof();
			int arrayOffset = offset + (baseTypeSize - 1);

			code += PUSH(3, ADDR_CONST(arrayOffset, false));
		} 
		else 
		{
			if (array.isParameter())
			{
				code += COM("Parameter Array (" + array + ":" + arrayType + ")");
				
				int offset = array.getOffset();
				int baseTypeSize = arrayType.getArrayBaseType().sizeof();
				int arrayTypeSize = arrayType.sizeof();
				int arrayOffset = offset + baseTypeSize - arrayTypeSize;

				code += PUSH(3, ADDR_CONST(arrayOffset, false));
				
				code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
			}
			else
			{
				code += COM("Local Array (" + array + ":" + arrayType + ")");
				
				int offset = array.getOffset();
				int baseTypeSize = arrayType.getArrayBaseType().sizeof();
				int arrayOffset = offset + baseTypeSize;

				code += PUSH(3, ADDR_CONST(arrayOffset, false));
				
				code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
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
