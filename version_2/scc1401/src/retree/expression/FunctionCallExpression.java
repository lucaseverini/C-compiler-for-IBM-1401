/*
	FunctionCallExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import compiler.SmallCC;
import retree.RetreeUtils;
import retree.exceptions.*;
import java.util.*;

import retree.program.FunctionDefinition;
import retree.type.*;
import static retree.RetreeUtils.*;

public class FunctionCallExpression extends Expression 
{

	private static HashMap<String,Boolean> functionCalls = new HashMap<String,Boolean>();
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


	public static boolean isCalledAndNotProcessed(String name)
	{
		if (functionCalls.keySet().contains(name))
			return !functionCalls.get(name);
		return false;
	}

	public static boolean finishedProcessing() {
		for (String s : functionCalls.keySet())
		{
			if (functionCalls.get(s) == false)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public String generateCode(boolean valueNeeded) 
	{

		if (!functionCalls.keySet().contains(SmallCC.getFunctionNameFromExpression(function)))
		{
			functionCalls.put(SmallCC.getFunctionNameFromExpression(function),false);
		}

		String name = SmallCC.getFunctionNameFromExpression(function);

		String code = COM("Function Call " + this.toString());

		// move x3 forward to x2 position?
//		code += INS("Move X2 to X3", null, "MCW", "X2", "X3");

		// First, push room for our return address to the stack.
		FunctionType functionType = (FunctionType)function.getType();
		if (SmallCC.nostack) {
			// put args inorder
			// get label for function
			String lbl = FunctionDefinition.getFunctionLabel(name);
			int offset = functionType.getReturnType().getSize() + (16 * Type.intType.getSize());
			int i = 0;
			while (i < arguments.size()) {
				code += arguments.get(i).generateCode(true);
				code += INS("", null, "MCW", REG(arguments.get(i)), lbl + "+" + (offset + arguments.get(i).getType().getSize()));
				if (arguments.get(i) instanceof VariableExpression)
				{
					VariableExpression variableExpression = (VariableExpression)arguments.get(i);
					variableExpression.setOffset(offset + variableExpression.getType().getSize());
				}
				offset += arguments.get(i++).getType().getSize();
			}
			code += INS("Jump to function " + name, null, "B", label(function.getValue()));
		} else {
			code += PUSH(functionType.getReturnType().sizeof());
			// Push all our parameters in reverse order
			int i = arguments.size();
			while (i-- > 0) {
				code += arguments.get(i).generateCode(true);
			}
			// Make a new frame
			code += COM("Create a stack frame with X3 pointer to it");
			code += PUSH(3, "X3");
			code += INS("Move X2 in X3", null, "MCW", "X2", "X3");
			code += "\n";
			code += INS("Jump to function " + name, null, "B", label(function.getValue()));
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
		}
		


		code += COM("End Function Call " + this.toString());
		code += "\n";

		return code;
	}

	@Override
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

	@Override
	public Expression getLeftExpression() {
		return null;
	}

	@Override
	public Expression getRightExpression() {
		return null;
	}

	public List<Expression> getArguments()
	{
		return arguments;
	}

	public static void removeCall(String s) {
		functionCalls.put(s,true);
	}
}
