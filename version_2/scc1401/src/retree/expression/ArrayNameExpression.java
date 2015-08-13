/*
	ArrayNameExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import compiler.SmallCC;
import retree.regalloc.ScopeInterval;
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
			if (SmallCC.nostack) {
				code += INS("", null, "MCW", getArray().getAddress(), "CAST");
				code += SNIP("number_to_pointer");
				code += INS("Move result to " + REG(this), null, "MCW", "CSTRES", "X1");
				code += INS("", null, "MA", NUM_CONST(arrayOffset, false), "X1");
				code += INS("", null, "MCW", "X1", REG(this));
			} else {
				code += PUSH(3, ADDR_CONST(arrayOffset, false));
			}
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
				if (SmallCC.nostack)
				{
					code += getArray().generateAddress();
					code += INS("Add X3 to stack", null, "MCW", REG(array), "X1");
					code += INS("Add X3 to stack", null, "MA" , ADDR_CONST(arrayOffset, false), "X1");
					code += INS("Add X3 to stack", null, "MCW", "X1", REG(this));
				} else {
					code += PUSH(3, ADDR_CONST(arrayOffset, false));
					code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
				}
			}
			else
			{
				code += COM("Local Array (" + array + ":" + arrayType + ")");

				int offset = array.getOffset();
				int baseTypeSize = arrayType.getArrayBaseType().sizeof();
				int arrayOffset = offset + baseTypeSize + (baseTypeSize * constSubscript);

				if (SmallCC.nostack)
				{
					code += getArray().generateAddress();
					code += INS("Add X3 to stack", null, "MCW", REG(array), "X1");
					code += INS("Add X3 to stack", null, "MA" , ADDR_CONST(arrayOffset, false), "X1");
					code += INS("Add X3 to stack", null, "MCW", "X1", REG(this));
				} else {
					code += PUSH(3, ADDR_CONST(arrayOffset, false));
					code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
				}
			}
		}
		
		return code;
	}
	
	// For stack management reasons, address of a pointer variable is at the end
	// of that variable we need to get a pointer to the beginning.
	@Override
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
			if (SmallCC.nostack)
			{
				code += INS("", null, "MCW" , ADDR_CONST(arrayOffset, false), "X1");
				code += INS("", null, "MCW", "X1", REG(this));
			} else {
				code += PUSH(3, ADDR_CONST(arrayOffset, false));
			}
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

				if (SmallCC.nostack)
				{
					code += getArray().generateAddress();
					code += INS("", null, "MCW", REG(array), "X1");
					code += INS("", null, "MA" , ADDR_CONST(arrayOffset, false), "X1");
					code += INS("", null, "MCW", "X1", REG(this));
				} else {
					code += PUSH(3, ADDR_CONST(arrayOffset, false));
					code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
				}
			}
			else
			{
				code += COM("Local Array (" + array + ":" + arrayType + ")");
				
				int offset = array.getOffset();
				int baseTypeSize = arrayType.getArrayBaseType().sizeof();
				int arrayOffset = offset + baseTypeSize;

				if (SmallCC.nostack)
				{
					code += getArray().generateAddress();
					code += INS("", null, "MCW", REG(array), "X1");
					code += INS("", null, "MA" , ADDR_CONST(arrayOffset, false), "X1");
					code += INS("", null, "MCW", "X1", REG(this));
				} else {
					code += PUSH(3, ADDR_CONST(arrayOffset, false));
					code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
				}
			}
		}
		
		return code;
	}

	public VariableExpression getArray()
	{
		return array;
	}

	@Override
	public Expression collapse() 
	{
		if (this.array.isStatic())
		{
			int value = array.getOffset();
			return new ConstantExpression(new PointerType(((ArrayType) array.getType()).getArrayBaseType()), value);
		}
		
		return this;
	}

	@Override
	public Expression getLeftExpression() {
		return array;
	}

	@Override
	public Expression getRightExpression() {
		return null;
	}

	@Override
	public String toString() 
	{
		return array.toString();
	}
}
