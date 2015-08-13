/*
	VariableExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;

import compiler.SmallCC;
import retree.type.PointerType;
import retree.type.Type;

public class VariableExpression extends LValue 
{
	private int offset;
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
				if (SmallCC.nostack) {

					code += COM("Static Variable (" + name + " : " + ADDR_LIT(addr) + ")");
					code += INS("Move addr lit to " + REG(this), null, "LCA", ADDR_LIT(addr), REG(this));
					int off = (refTypeSize - 1) + (refTypeSize * constSubscript);
					code += INS("Add offset " + off + " to point element " + constSubscript, null, "A", NUM_CONST(off, false), REG(this));
				} else {
					code += COM("Static Variable (" + name + " : " + ADDR_LIT(addr) + ")");
					code += PUSH(getType().sizeof(), ADDR_LIT(addr));
					int off = (refTypeSize - 1) + (refTypeSize * constSubscript);
					code += INS("Add offset " + off + " to point element " + constSubscript, null, "A", NUM_CONST(off, false), "0+X2");
				}
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
					if (SmallCC.nostack)
					{
						code += COM("Parameter Variable (" + name + ")");
						code += this.getAddress();
						code += INS("", null, "LCA", "0+X1", REG(this));
						code += INS("", null, "MCW", REG(this), "X1");

					} else {
						code += COM("Parameter Variable (" + name + " : " + OFF(off) + ")");
						code += PUSH(getType().sizeof(), OFF(off));
					}

					if(constSubscript > 0)
					{
						off = (refTypeSize * constSubscript);
						if (SmallCC.nostack) {
							code += INS("Add offset " + off + " to point element " + constSubscript, null, "MA", NUM_CONST(off, false), "X1");
							code += INS("", null, "LCA", "X1", REG(this));
						} else {
							code += INS("Add offset " + off + " to point element " + constSubscript, null, "A", NUM_CONST(off, false), "0+X2");
						}
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
					if (SmallCC.nostack) {
						code += this.getAddress();
						code += INS("", null, "LCA", "0+X1", REG(this));
						code += INS("", null, "MCW", REG(this), "X1");
						off = (refTypeSize - 1) + (refTypeSize * constSubscript);
						code += INS("Add offset " + off + " to point element " + constSubscript, null, "MA", NUM_CONST(off, false), "X1");
						code += INS("", null, "LCA", "X1", REG(this));
					} else {
						code += PUSH(getType().sizeof(), OFF(off));
						off = (refTypeSize - 1) + (refTypeSize * constSubscript);
						code += INS("Add offset " + off + " to point element " + constSubscript, null, "A", NUM_CONST(off, false), "0+X2");
					}
				}
			}
		} 
		
		return code;
	}
		
	@Override
	public String generateCode(boolean valueNeeded)
	{
		String code = COM("Generate value for " + this);
		
		if (valueNeeded) 
		{
			if (isStatic)
			{
				int addr = offset + getType().getSize() - 1;
				code += COM("Static Variable (" + name + " : " + ADDR_LIT(addr) + ")");
				if (SmallCC.nostack) {
					code += INS("", "", "LCA", ADDR_LIT(addr), REG(this));
				} else {
					code += PUSH(getType().sizeof(), ADDR_LIT(addr));
				}
			}
			else
			{
				if (isParam)
				{
					int off = offset;
					code += COM("Parameter Variable (" + name + " : " + OFF(off) + ")");
					if (SmallCC.nostack) {
						code += INS("Copy address to X1", null, "MCW", "X3", "X1");
						code += INS("Modify X1 to point to params", null, "MA", "12+X3", "X1");
						code += INS("Modify X1", null, "MA", "@"+ADDR_COD(((offset + 3) * -1) + getType().getSize())+"@", "X1");
						code += INS("Move val to "+REG(this), "", "LCA", "0+X1", REG(this));
					} else {
						code += PUSH(getType().sizeof(), OFF(off));
					}
				}
				else
				{
					int off = offset + getType().getSize();
					if(SmallCC.nostack) {
						code += INS("Copy address to X1", null, "MCW", "X3", "X1");
						code += INS("Modify X1 to point to var", null, "MA", "15+X3", "X1");
						code += INS("Modify X1", null, "MA", "@"+ADDR_COD(off - 3)+"@", "X1");
						code += INS("Copy value to " + REG(this), null, "LCA", "0+X1", REG(this));
					} else {
						code += PUSH(getType().sizeof(), OFF(off));
					}
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
		String code = COM("Generating address for " + this);
		if (isStatic)
		{
			int addr = offset + getType().getSize() - 1;
			if(SmallCC.nostack) {
				code += INS("Move addr to " + REG(this), null, "LCA", ADDR_CONST(addr, false), REG(this));
			} else {
				code += PUSH(3, ADDR_CONST(addr, false));
			}
		}
		else
		{
			if (isParam)
			{
				int addr = offset;
				if(SmallCC.nostack) {
					code += INS("Copy address to X1", null, "MCW", "X3", "X1");
					code += INS("Modify X1 to point to params", null, "MA", "12+X3", "X1");
					code += INS("Modify X1", null, "MA", "@"+ADDR_COD(((offset + 3) * -1) + getType().getSize())+"@", "X1");
					code += INS("Copy addr to " + REG(this), null, "MCW", "X1", REG(this));
				} else {
					code += PUSH(3, ADDR_CONST(addr, false));
					code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
				}
			}
			else
			{
				int addr = offset + getType().getSize();
				code += COM("Generating address for variable: " + this);
				if(SmallCC.nostack) {
					code += INS("Copy address to X1", null, "MCW", "X3", "X1");
					code += INS("Modify X1 to point to var", null, "MA", "15+X3", "X1");
					code += INS("Modify X1", null, "MA", "@"+ADDR_COD(addr - 3)+"@", "X1");
					code += INS("Copy addr to " + REG(this), null, "MCW", "X1", REG(this));
				} else {
					code += PUSH(3, ADDR_CONST(addr, false));
					code += INS("Add X3 to stack", null, "MA", "X3", STACK_OFF(0));
				}
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
				if (SmallCC.nostack)
				{
					code += INS("Copy address to X1", null, "MCW", "X3", "X1");
					code += INS("Modify X1 to point to params", null, "MA", "12+X3", "X1");
					code += INS("Modify X1", null, "MA", "@"+ADDR_COD(offset + 3  + getType().getSize())+"@", "X1");
//					code += INS("Copy addr to REG0", null, "MCW", "X1", "REG0");
				} else {
					code += OFF(offset);
				}
			}
			else
			{
				if (SmallCC.nostack)
				{
					int addr = offset + getType().getSize();
					code += INS("Copy address to X1", null, "MCW", "X3", "X1");
					code += INS("Modify X1 to point to var", null, "MA", "15+X3", "X1");
					code += INS("Modify X1", null, "MA", "@"+ADDR_COD(addr - 3)+"@", "X1");
//					code += INS("Copy addr to REG0", null, "MCW", "X1", "REG0");
				}
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

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public Expression getLeftExpression() {
		return null;
	}

	@Override
	public Expression getRightExpression() {
		return null;
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
