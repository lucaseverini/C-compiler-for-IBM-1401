/*
	FunctionDefinition.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.program;

import compiler.SmallCC;
import java.util.Iterator;
import static retree.RetreeUtils.*;
import retree.statement.*;
import retree.intermediate.*;
import retree.expression.*;

public class FunctionDefinition
{
	private ConstantExpression declaration;
	private BlockStatement block;

	public FunctionDefinition(ConstantExpression declaration, BlockStatement block)
	{
		this.declaration = declaration;
		this.block = block;
	}

	public ConstantExpression getDeclaration()
	{
		return declaration;
	}

	//As per our calling conventions, the caller is responsible for pushing
	//the new stack frame down.  We are simply responsible for our return
	//address.
	public String generateCode() throws Exception
	{
		Optimizer.addInstruction("********************************************************************************","","");
		Optimizer.addInstruction("Function : " + SmallCC.getFunctionNameFromExpression(declaration),"","");
		Optimizer.addInstruction("********************************************************************************","","");
		Optimizer.addInstruction("Save return address in register B to local frame", label(declaration.getValue()), "SBR", "3+X3");
		Optimizer.addInstruction("Set the WM", "", "SW", "1+X3");
		Optimizer.addInstruction("Clean WM", "", "CW", "2+X3");
		Optimizer.addInstruction("Clean WM", "", "CW", "3+X3");
		String code = "\n" +
			COM("********************************************************************************") +
			COM("Function : " + SmallCC.getFunctionNameFromExpression(declaration)) +
			COM("********************************************************************************") +
			INS("Save return address in register B to local frame", label(declaration.getValue()), "SBR", "3+X3") +
			INS("Set the WM", null, "SW", "1+X3") +
			INS("Clean WM", null, "CW", "2+X3") +
			INS("Clean WM", null, "CW", "3+X3");

		code += block.generateCode();
		Optimizer.addInstruction("Load return address to X1", "", "LCA", "3+X3", "X1");
		Optimizer.addInstruction("Jump back to caller", "", "B", "0+X1");
		code += INS("Load return address to X1", null, "LCA", "3+X3", "X1") +
			INS("Jump back to caller", null, "B", "0+X1");
		Optimizer.addInstruction("********************************************************************************","","");
		Optimizer.addInstruction("End Function : " + SmallCC.getFunctionNameFromExpression(declaration),"","");
		Optimizer.addInstruction("********************************************************************************","","");
		code += "\n";
		code += COM("********************************************************************************");
		code += COM("End Function : " + SmallCC.getFunctionNameFromExpression(declaration));
		code += COM("********************************************************************************");

		return code;
	}

	public String toString()
	{
		String name = SmallCC.getFunctionNameFromExpression(declaration);
		return name;
	}
}
