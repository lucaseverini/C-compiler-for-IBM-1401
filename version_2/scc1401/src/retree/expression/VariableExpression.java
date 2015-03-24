/*
	VariableExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.Type;
import retree.intermediate.*;

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

	public String generateCode(boolean valueNeeded)
	{
		String code = "";

		if (valueNeeded)
		{
			if (isStatic)
			{
				int addr = offset + getType().getSize() - 1;
				Optimizer.addInstruction("Static Variable (" + name + " : " + ADDR_LIT(addr) + ")","","");
				code += COM("Static Variable (" + name + " : " + ADDR_LIT(addr) + ")");
				code += PUSH(getType().sizeof(), ADDR_LIT(addr));
			}
			else
			{
				if (isParam)
				{
					int off = offset;
					Optimizer.addInstruction("Parameter Variable (" + name + " : " + OFF(off) + ")","","");
					code += COM("Parameter Variable (" + name + " : " + OFF(off) + ")");
					code += PUSH(getType().sizeof(), OFF(off));
				}
				else
				{
					int off = offset + getType().getSize();
					Optimizer.addInstruction("Local Variable (" + name + " : " + OFF(off) + ")","","");
					code += COM("Local Variable (" + name + " : " + OFF(off) + ")");
					code += PUSH(getType().sizeof(), OFF(off));
				}
			}
		}

		// System.out.println("CODE: " + name + (isStatic ? " static " : "") + " : " + offset + " : " + code);

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
			int addr = offset + getType().getSize() - 1;
			code += PUSH(3, ADDR_CONST(addr, false));
		}
		else
		{
			if (isParam)
			{
				int addr = offset;
				code += PUSH(3, ADDR_CONST(addr, false));
				Optimizer.addInstruction("", "", "MA", "X3", STACK_OFF(0));
				code += INS(null, null, "MA", "X3", STACK_OFF(0));
			}
			else
			{
				int addr = offset + getType().getSize();
				code += PUSH(3, ADDR_CONST(addr, false));
				Optimizer.addInstruction("", "", "MA", "X3", STACK_OFF(0));
				code += INS(null, null, "MA", "X3", STACK_OFF(0));
			}
		}

		// System.out.println("ADDRESS: " + name + (isStatic ? " static " : "") + " : " + offset + " : " + code);

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

		// System.out.println("ADDRESS-2: " + name + (isStatic ? " static " : "") + " : " + offset + " : " + code);

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

	public String toString()
	{
		return name;
	}
}
