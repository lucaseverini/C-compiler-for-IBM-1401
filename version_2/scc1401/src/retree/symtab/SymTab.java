/*
	SymTab.java

	The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.symtab;

import compiler.SmallCC;
import java.util.HashMap;
import retree.expression.VariableExpression;
import retree.type.Type;

public class SymTab 
{
	private final HashMap<String, VariableExpression> table = new HashMap<String, VariableExpression>();
	private int topOffset;
	private int localOffset;
	private int paramOffset;
	private boolean isStatic = false;

	public SymTab(int offset, boolean isStatic) 
	{
		topOffset = localOffset = offset;
		paramOffset = -3;
		this.isStatic = isStatic;
	}

	public SymTab(int offset, int spaceToReserve, boolean isStatic) 
	{
		topOffset = offset;
		localOffset = topOffset + spaceToReserve;
		paramOffset = -3;
		this.isStatic = isStatic;
	}

	public VariableExpression get(String identifier) 
	{
		return table.get(identifier) == null ? null : table.get(identifier);
	}

	public VariableExpression put(String identifier, Type t, boolean isParam)
	{
		if (isParam) 
		{
			VariableExpression varExp = new VariableExpression(t, paramOffset, false, true, identifier);
			paramOffset -= t.sizeof();
			table.put(identifier, varExp);
			return varExp;
		} 
		else 
		{
			Boolean inAsm = SmallCC.inAsmFunc;
			
			if(!inAsm)
			{
				// localOffset += t.sizeof();
			}

			VariableExpression varExp = new VariableExpression(t, localOffset, isStatic, false, identifier);
			varExp.inAsm = inAsm;
			
			// If the expression is used in ASM pseudo-function, is not present in the code and is not added to the table
			if(!inAsm)
			{
				localOffset += t.sizeof();
				table.put(identifier, varExp);
			}
			
			return varExp;
		}
	}

	public int getLocalSize() 
	{
		return localOffset - topOffset;
	}

	public int getLocalOffset() 
	{
		return localOffset;
	}

	public boolean isStatic()
	{
		return isStatic;
	}
}
