/*
	FunctionDefinition.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.program;

import compiler.SmallCC;
import java.util.Objects;
import static retree.RetreeUtils.*;
import retree.statement.*;
import retree.expression.*;

public class FunctionDefinition implements Comparable<FunctionDefinition>
{
	private final ConstantExpression declaration;
	private final BlockStatement block;
	
	public FunctionDefinition(ConstantExpression declaration, BlockStatement block)
	{
		this.declaration = declaration;
		this.block = block;
	}

	public ConstantExpression getDeclaration() 
	{
		return declaration;
	}

	// As per our calling conventions, the caller is responsible for pushing
	// the new stack frame down.  We are simply responsible for our return address
	public String generateCode() throws Exception
	{
		String code = "\n" + 
			COM("********************************************************************************") +
			COM("Function : " + SmallCC.getFunctionNameFromExpression(declaration)) +
			COM("********************************************************************************") +
			INS("Save return address in register B in stack frame (X3)", label(declaration.getValue()), "SBR", "3+X3") +
			COM("Set the right WM and clear the wrong ones") + 
			INS("Set WM at 1+X3", null, "SW", "1+X3") +
			INS("Clear WM at 2+X3", null, "CW", "2+X3") +
			INS("Clear WM at 3+X3", null, "CW", "3+X3");
					  
		code += block.generateCode();
					  
		code += INS("Load return address in X1", null, "LCA", "3+X3", "X1") +
			INS("Jump back to caller in X1", null, "B", "0+X1");

		code += "\n";
		code += COM("********************************************************************************");
		code += COM("End Function : " + SmallCC.getFunctionNameFromExpression(declaration));
		code += COM("********************************************************************************");
		
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
