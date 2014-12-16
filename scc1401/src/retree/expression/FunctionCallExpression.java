package retree.expression;

import retree.exceptions.*;
import java.util.*;
import retree.type.*;
import retree.program.*;
import static retree.RetreeUtils.*;

public class FunctionCallExpression extends Expression {
	private ConstantExpression function;
	private List<Expression> arguments;

	public FunctionCallExpression(Expression function, List<Expression> arguments) throws Exception{
		super(((FunctionType)function.getType()).getReturnType());
		List<Type> paramTypes = ((FunctionType)function.getType()).getParamTypes();
		if (!((FunctionType)function.getType()).getVariadic()) {
			if (arguments.size() != paramTypes.size()) {
				throw new Exception("Arity Mismatch");
			}
			for (int i = 0; i < arguments.size(); ++i) {
				if (!arguments.get(i).getType().equals(paramTypes.get(i))) {
					throw new TypeMismatchException(this, paramTypes.get(i), arguments.get(i).getType());
				}
			}
		}
		this.function = (ConstantExpression) function;
		this.arguments = arguments;
	}

	public String generateCode(boolean valueNeeded) {
		String code = "";
		// Note we assume that the asm function is number 0
		if (function.getValue() == 0)
		{
			code += COM("Inserting ASM snippet from code");
			code += function.getASM();
			code += COM("Finish inserting ASM snippet from code");
			return code;
		}

		FunctionType functionType = (FunctionType)function.getType();
		//first, push room for our return address to the stack.
		code += PUSH(functionType.getReturnType().sizeof());
		int i = arguments.size();
		//push all our parameters in reverse order
		while (i --> 0) {
			code += arguments.get(i).generateCode(true);
		}
		//make a new frame
		code += PUSH(3, "X3");
		code += INS("MCW", "X2", "X3");
		//branch
		code += INS("B", label(function.getValue()));
		//AFTER THE CALL:
		//restore our frame
		code += POP(3, "X3");
		//pop off all the arguments
		for (Expression e: arguments) {
			code += POP(e.getType().sizeof());
		}
		//now our return address should be at the top of the stack.
		//if we don't want a value, pop it
		if (!valueNeeded) {
			code += POP(functionType.getReturnType().sizeof());
		}
		return code;
	}

	public String toString() {
		String s = "";
		for(Expression e : arguments)
		{
			s += (" " + e.toString());
		}
		return "(" + function.getValue() + "(" + s + ")" + ")";
	}
}
