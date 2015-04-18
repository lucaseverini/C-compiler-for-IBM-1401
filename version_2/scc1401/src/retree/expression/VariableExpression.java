/*
	VariableExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.PointerType;
import retree.type.Type;

public class VariableExpression extends LValue 
{
	private final int offset;
	private final boolean isStatic;
	private final boolean isParam;
	private final String name;
	public boolean inAsm = false;

	public VariableExpression(Type type, int offset, boolean isStatic, boolean isParam, String name)
	{
		super(type);
		
		this.offset = offset;
		this.isStatic = isStatic;
		this.isParam = isParam;
		this.name = name;
	}

	public String generateCodeForConstSubscript(boolean valueNeeded, int constSubscript)
	{
		String code = "";
		
		if (valueNeeded) 
		{
			if (isStatic)
			{
				Type type = getType();
				Type refType = ((PointerType)type).getRefType();
				int typeSize = type.getSize();
				int refTypeSize = refType.getSize();
				
				int addr = offset + (typeSize - 1);
				code += COM("Static Variable (" + name + " : " + ADDR_LIT(addr) + ")"); 
				code += PUSH(getType().sizeof(), ADDR_LIT(addr));
				
				int off = (refTypeSize - 1) + (refTypeSize * constSubscript);
				code += INS("Add offset " + off + " to point element " + constSubscript, null, "A", NUM_CONST(off, false), "0+X2");
			} 
			else
			{
				if (isParam)
				{
					Type type = getType();
					Type refType = ((PointerType)type).getRefType();
					int typeSize = type.getSize();
					int refTypeSize = refType.getSize();

					int off = offset;
					code += COM("Parameter Variable (" + name + " : " + OFF(off) + ")"); 
					code += PUSH(getType().sizeof(), OFF(off));

					if(constSubscript > 0)
					{
						off = (refTypeSize * constSubscript);
						code += INS("Add offset " + off + " to point element " + constSubscript, null, "A", NUM_CONST(off, false), "0+X2");
					}
				}
				else
				{
					Type type = getType();
					Type refType = ((PointerType)type).getRefType();
					int typeSize = type.getSize();
					int refTypeSize = refType.getSize();

					int off = offset + typeSize;
					code += COM("Local Variable (" + name + " : " + OFF(off) + ")"); 
					code += PUSH(getType().sizeof(), OFF(off));

					off = (refTypeSize - 1) + (refTypeSize * constSubscript);
					code += INS("Add offset " + off + " to point element " + constSubscript, null, "A", NUM_CONST(off, false), "0+X2");
				}
			}
		} 
		
		return code;
	}
		
	@Override
	public String generateCode(boolean valueNeeded)
	{
		String code = "";
		
		if (valueNeeded) 
		{
			if (isStatic)
			{
				int addr = offset + getType().getSize() - 1;
				code += COM("Static Variable (" + name + " : " + ADDR_LIT(addr) + ")"); 
				code += PUSH(getType().sizeof(), ADDR_LIT(addr));
			} 
			else
			{
				if (isParam)
				{
					int off = offset;
					code += COM("Parameter Variable (" + name + " : " + OFF(off) + ")"); 
					code += PUSH(getType().sizeof(), OFF(off));
				}
				else
				{
					int off = offset + getType().getSize();
					code += COM("Local Variable (" + name + " : " + OFF(off) + ")"); 
					code += PUSH(getType().sizeof(), OFF(off));
				}
			}
		} 
		
		return code;
	}

	@Override
	public LValue collapse() 
	{
		return this;
	}

	@Override
	public String generateAddress() 
	{
		String code = "";
		
		if (isStatic) 
		{
			int addr = offset + getType().getSize() - 1;
			code += PUSH(3, ADDR_CONST(addr, false));
		} 
		else 
		{
			if (isParam)
			{
				int addr = offset;
				code += PUSH(3, ADDR_CONST(addr, false));
				code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
			}
			else
			{	
				int addr = offset + getType().getSize();
				code += PUSH(3, ADDR_CONST(addr, false));
				code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
			}
		}

		return code;
	}

	public String getAddress() 
	{
		String code = "";
		
		if (isStatic) 
		{
			code += (offset + getType().getSize() - 1);
		} 
		else 
		{
			if (isParam)
			{
				code += OFF(offset);
			}
			else
			{
				int off = (offset + getType().getSize());
				code += OFF(off);
			}
		}
		
		return code;
	}

	public String getWordMarkAddress() 
	{
		String code = "";

		if (isStatic) 
		{
			code += (offset + 1);
		} 
		else 
		{
			if (isParam)
			{
				code = OFF(offset + 1 - getType().sizeof());
			}
			else
			{
				code += OFF(offset + 1);
			}
		}
		
		return code;
	}

	public boolean isStatic() 
	{
		return isStatic;
	}

	public boolean isParameter() 
	{
		return isParam;
	}

	public int getOffset() 
	{
		return offset;
	}
	
	public String getName() 
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
