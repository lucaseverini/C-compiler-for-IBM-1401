package retree.expression;

import static compiler.SmallCC.*;
import retree.type.*;
import static retree.RetreeUtils.*;

public class ArrayNameExpression extends Expression
{
	private final VariableExpression array;
	private final ArrayType arrayType;
	private String label;

	public ArrayNameExpression(VariableExpression array) 
	{
		super(new PointerType(((ArrayType) array.getType()).getArrayBaseType()));
		
		this.array = array;
		this.arrayType = (ArrayType) array.getType();
	}

	//for stack management reasons, address of a pointer variable is at the end
	//of that variable
	//we need to get a pointer to the beginning.
	public String generateCode(boolean valueNeeded) 
	{
		String code = "";
		
		if (!valueNeeded) 
		{
			return code;
		}
				
		if (array.isStatic()) 
		{
			// Generare una label per ADDR_CONST a cui verra' associato un valore (indirizzo) solo dopo la generazione dei DCW dell'array
			if(label == null)
			{
				label = retree.RetreeUtils.ARRAY_CONST(this, false);
			}
			
			// int addr = array.getOffset() + arrayType.getArrayBaseType().sizeof() - arrayType.sizeof();
			// code = code + COM("ArrayNameExpresssion(" + array + ":" + arrayType + ":" + addr + ":" + label + ")");
			// code = code + PUSH(3, ADDR_CONST(addr, false));
			code = code + COM("ArrayNameExpresssion(" + array + ":" + arrayType + ":" + label + ":static)");
			code = code + PUSH(3, label);
		} 
		else 
		{
			// Generate a label for ADDR_CONST which is the offset to the array into the local stack frame
			int addr = array.getOffset() + arrayType.getArrayBaseType().sizeof() - arrayType.sizeof();
			label = ADDR_CONST(addr, false);
			
			code = code + COM("ArrayNameExpresssion(" + array + ":" + arrayType + ":" + label + ":local)");
			code = code + PUSH(3, label);
			code = code + INS("MA", "X3", STACK_OFF(0));
		}
		
		return code;
	}

	public VariableExpression getArray()
	{
		return array;
	}

	public ArrayType getArrayType()
	{
		return arrayType;
	}

	public Expression collapse() 
	{
		if (this.array.isStatic())
		{
			return new ConstantExpression(new PointerType(((ArrayType) array.getType()).getArrayBaseType()),
						array.getOffset() + arrayType.getArrayBaseType().sizeof() - arrayType.sizeof());
		}
		
		return this;
	}

	public String toString() 
	{
		return array.toString();
	}
}
