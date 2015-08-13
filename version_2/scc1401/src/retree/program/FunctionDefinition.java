/*
	FunctionDefinition.java

	The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.program;

import compiler.SmallCC;

import java.util.ArrayList;
import java.util.Objects;
import static retree.RetreeUtils.*;

import retree.regalloc.RegisterAllocator;
import retree.statement.*;
import retree.expression.*;
import retree.type.ArrayType;
import retree.type.FunctionType;
import retree.type.Type;

public class FunctionDefinition implements Comparable<FunctionDefinition>
{
	private final ConstantExpression declaration;
	private final BlockStatement block;
	private ArrayList<Initializer> allVariables = new ArrayList<>();
	private VariableExpression retVarExpression = null;

	public FunctionDefinition(ConstantExpression declaration, BlockStatement block)
	{
		this.declaration = declaration;
		this.block = block;
	}

	public ConstantExpression getDeclaration() 
	{
		return declaration;
	}

	// Has side effect of getting the return variable location so we can set position later
	public void getInitalizers(Statement s)
	{
		if (s instanceof BlockStatement)
		{
			BlockStatement blockStatement = (BlockStatement)s;
			allVariables.addAll(blockStatement.getInitializers());
			for (Statement statement: blockStatement.getStatements())
			{
				getInitalizers(statement);
			}
		}

		if (s instanceof LoopStatement)
		{
			LoopStatement loopStatement = (LoopStatement)s;
			getInitalizers(loopStatement.getBody());
		}

		if (s instanceof IfStatement)
		{
			IfStatement ifStatement = (IfStatement)s;
			getInitalizers(ifStatement.getIfClause());
			getInitalizers(ifStatement.getElseClause());
		}
	}

	public static String getFunctionLabel(String funcName) {
		String funcLbl = "F";
		for (int i = 0; i < 5; i++)
		{
			if (funcName.length() - 1 - i > 0)
				funcLbl += funcName.charAt(funcName.length() - 1 - i);
			else
				funcLbl += "A";
		}
		return funcLbl;
	}

	// As per our calling conventions, the caller is responsible for pushing
	// the new stack frame down.  We are simply responsible for our return address
	public String generateCode(RegisterAllocator registerAllocator) throws Exception
	{
		String code = "\n";
		if (SmallCC.nostack)
		{
			getInitalizers(this.getBlock());
			String funcName = this.toString().toUpperCase();
			String funcLbl = "F";
			int returnSize = 0;
			int offsetToLocals = 15;
			int numRegs = registerAllocator.getNumberRegisters() - 1;
			FunctionType functionType = (FunctionType)declaration.getType();
			funcLbl = getFunctionLabel(funcName);
			String retSize = "";
			for (int i = 0; i < functionType.getReturnType().getSize(); i++)
			{
				retSize += "0";
			}
			code += INS("Ret addr", funcLbl, "DSA", "000");
			code += INS("Previous function function header", "", "DCW", "000");
			if (functionType.getReturnType().getSize() > 0) {
				returnSize = functionType.getReturnType().getSize();
			}
			offsetToLocals += returnSize;
			code += INS("Offset to REGS", "", "DCW", ADDR_CONST(offsetToLocals, false));
			offsetToLocals += (16 * Type.intType.getSize());
			code += INS("Offset to Params", "", "DCW", ADDR_CONST(offsetToLocals, false));
			for (Type t : functionType.getParamTypes()) {
				offsetToLocals += t.getSize();
			}
			code += INS("Offset to LocalVars", "", "DCW", ADDR_CONST(offsetToLocals, false));
			code += INS("Ret value", null, "DCW", retSize);
			for(int i = 0 ; i< registerAllocator.getNumberRegisters(); i ++)
			{
				code += INS("Register"+ i + " save pos", null, "DCW", "00000");
			}

			for (Type t : functionType.getParamTypes())
			{
				String size = "";
				for (int i = 0; i < t.getSize(); i++)
				{
					size += "0";
				}
//				offsetToLocals += t.getSize();
				code += INS("Arg " + t, null, "DCW", size);
			}

			for (Initializer i : allVariables)
			{
				if (!i.getVariable().getName().equals("")) {
					String space = "";
					if (i.getVariable().getType() instanceof ArrayType) {
						for (int k = 0; k < ((ArrayType) i.getVariable().getType()).getArrayBaseType().getSize(); k++) {
							space += "0";
						}
						for (int j = 0; j < ((ArrayType) i.getVariable().getType()).getNumElements(); j ++)
						{
							code += INS("Room for vars", null, "DCW", space);
						}
					} else {
						for (int j = 0; j < i.getVariable().getType().getSize(); j++) {
							space += "0";
						}
						code += INS("Room for vars", null, "DCW", space);
					}
				}
			}

			code += COM("********************************************************************************") +
					COM("Function : " + SmallCC.getFunctionNameFromExpression(declaration)) +
					COM("********************************************************************************");
			code += INS("Save return address in "+funcLbl, label(declaration.getValue()), "SBR", funcLbl);
			code += INS("Save "+funcLbl+ " to X3", null, "SAR", "X3");
			code += INS("Save X2 to "+funcLbl + "+3", null, "MCW", "X2", funcLbl + "+3");

			code += INS("Save REG" + numRegs, null, "LCA", "REG" + numRegs, funcLbl + "+" + (92 + returnSize));
			for (int i = numRegs-1; i > -1; i--)
			{
				code += INS("Save REG" + i, null, "LCA");
			}
//			code += INS("Move X3 to local vars",null,"MA","@"+ADDR_COD(offsetToLocals)+"@","X3");
			code += block.generateCode(registerAllocator);
			code += INS("Restore REG" + numRegs, null, "LCA", funcLbl + "+" + (92 + returnSize), "REG" + numRegs);
			for (int i = numRegs-1; i > -1; i--)
			{
				code += INS("Restore REG" + i, null, "LCA");
			}
			code += INS("Load return address in X1", null, "LCA", funcLbl, "X1") +
					INS("Jump back to caller in X1", null, "B", "0+X1");
			code += "\n";
			code += COM("********************************************************************************");
			code += COM("End Function : " + SmallCC.getFunctionNameFromExpression(declaration));
			code += COM("********************************************************************************");
		} else {
			code +=
					COM("********************************************************************************") +
					COM("Function : " + SmallCC.getFunctionNameFromExpression(declaration)) +
					COM("********************************************************************************") +
					INS("Save return address in register B in stack frame (X3)", label(declaration.getValue()), "SBR", "3+X3") +
					COM("Set the right WM and clear the wrong ones") +
					INS("Set WM at 1+X3", null, "SW", "1+X3") +
					INS("Clear WM at 2+X3", null, "CW", "2+X3") +
					INS("Clear WM at 3+X3", null, "CW", "3+X3");

			code += block.generateCode(registerAllocator);

			code += INS("Load return address in X1", null, "LCA", "3+X3", "X1") +
					INS("Jump back to caller in X1", null, "B", "0+X1");

			code += "\n";
			code += COM("********************************************************************************");
			code += COM("End Function : " + SmallCC.getFunctionNameFromExpression(declaration));
			code += COM("********************************************************************************");

		}
		return code;
	}

	@Override
	public String toString()
	{
		String name = SmallCC.getFunctionNameFromExpression(declaration);
		return name;
	}
	
	@Override
	public boolean equals(Object o) 
	{
		if (!(o instanceof FunctionDefinition))
		{
			return false;
		}
		
		FunctionDefinition f = (FunctionDefinition)o;
		return f.toString().equals(toString());
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.declaration);
	}

	@Override
	public int compareTo(FunctionDefinition f)
	{
		int lastCmp = toString().compareTo(f.toString());
		return (lastCmp != 0 ? lastCmp : toString().compareTo(f.toString()));
	}

	public BlockStatement getBlock()
	{
		return block;
	}
}
