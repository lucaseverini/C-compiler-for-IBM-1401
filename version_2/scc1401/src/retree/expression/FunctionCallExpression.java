/*
	FunctionCallExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import compiler.SmallCC;
import retree.exceptions.*;
import java.util.*;
import retree.type.*;
import retree.program.*;
import static retree.RetreeUtils.*;
import wci.intermediate.SymTabEntry;

public class FunctionCallExpression extends Expression 
{
	private final ConstantExpression function;
	private final List<Expression> arguments;

	public FunctionCallExpression(Expression function, List<Expression> arguments) throws Exception
	{
		super(((FunctionType)function.getType()).getReturnType());
		
		List<Type> paramTypes = ((FunctionType)function.getType()).getParamTypes();
		if (!((FunctionType)function.getType()).getVariadic()) 
		{
			if (arguments.size() != paramTypes.size()) 
			{
				throw new Exception("Parity Mismatch");
			}
			for (int i = 0; i < arguments.size(); ++i) 
			{
				if (!arguments.get(i).getType().equals(paramTypes.get(i))) 
				{
					throw new TypeMismatchException(this, paramTypes.get(i), arguments.get(i).getType());
				}
			}
		}
		
		this.function = (ConstantExpression) function;
		this.arguments = arguments;
	}

	public String generateCode(boolean valueNeeded) 
	{
		String code = COM("Function Call " + this.toString());
		
		// First, push room for our return address to the stack.
		FunctionType functionType = (FunctionType)function.getType();
		code += PUSH(functionType.getReturnType().sizeof());
		
		// Push all our parameters in reverse order
		int i = arguments.size();
		while (i --> 0) 
		{
			code += arguments.get(i).generateCode(true);
		}
		
		// Make a new frame
		code += PUSH(3, "X3");
		code += INS("Copy X2 into X3", null, "MCW", "X2", "X3");
		
		// Branch
		String name = SmallCC.getFunctionNameFromExpression(function);
		code += INS("Jump to function " + name, null, "B", label(function.getValue()));
		
		// AFTER THE CALL:
		// Restore our frame
		code += POP(3, "X3");
		
		// Pop off all the arguments
		for (Expression e: arguments) 
		{
			code += POP(e.getType().sizeof());
		}
		
		// Now our return address should be at the top of the stack.
		// If we don't want a value, pop it
		if (!valueNeeded) 
		{
			code += POP(functionType.getReturnType().sizeof());
		}
		
		return code;
	}

	public String toString()
	{
		String s = "";
		
		String name = SmallCC.getFunctionNameFromExpression(function);
		
		Iterator<Expression> iter = arguments.iterator();
		while(iter.hasNext())
		{
			s += iter.next();
			
			if(iter.hasNext())
			{
				s = s + ", ";
			}
		}
		
		return name + "(" + s + ")";
	}
}
