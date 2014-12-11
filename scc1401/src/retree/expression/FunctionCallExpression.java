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
		if (arguments.size() != paramTypes.size()) {
			throw new Exception("Arity Mismatch");
		}
		for (int i = 0; i < arguments.size(); ++i) {
			if (!arguments.get(i).getType().equals(paramTypes.get(i))) {
				throw new TypeMismatchException(this, paramTypes.get(i), arguments.get(i).getType());
			}
		}
		this.function = (ConstantExpression) function;
		this.arguments = arguments;
	}
	
	public String generateCode(boolean valueNeeded) {
		String code = "";
		int i = arguments.size();
		//push all our parameters in reverse order
		while (i --> 0) {
			code += arguments.get(i).generateCode(true);
		}
		//make a new frame
		code += PUSH("X3");
		code += INS("MCW", "X2", "X3");
		//branch
		code += INS("B", label(function.getValue()));
		//restore our frame
		POP("X3");
		//pop all the parameters except the first we pushed
		//the return value should be stored there
		if (arguments.size() > 1) {
			code += INS("MA", ADDR_CONST(1 - arguments.size()), "X2");
		}
		return code;
	}
}

