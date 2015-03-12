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
		String code = "\n" + 
			COM("********************************************************************************") +
			COM("FunctionDefinition(" + SmallCC.getFunctionNameFromExpression(declaration) + ")") +
			COM("********************************************************************************") +
			INS(null, label(declaration.getValue()), "SBR", "3+X3") +
			INS(null, null, "SW", "1+X3") +
			INS(null, null, "CW", "2+X3") +
			INS(null, null, "CW", "3+X3") +
			block.generateCode() +
			INS(null, null, "LCA", "3+X3", "X1") +
			INS("Jump back to caller", null, "B", "0+X1");
		
		return code;
	}

	public String toString()
	{
		String name = SmallCC.getFunctionNameFromExpression(declaration);
		return name;
	}
}
