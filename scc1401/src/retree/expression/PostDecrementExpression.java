package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class PostDecrementExpression extends Expression {
	private LValue l;

	public PostDecrementExpression(LValue l) throws TypeMismatchException {
		super(l.getType());
		this.l = l;
	}

	public Expression collapse() {
		try {
			return new PostDecrementExpression(l.collapse());
		} catch (retree.exceptions.TypeMismatchException e){
			return null;
		}
	}

	public String generateCode(boolean valueNeeded) {
		String code = l.generateAddress();
		// String code = l.generateCode(valueNeeded);
		code += POP(3, "X1");
		if (valueNeeded) {
			code += PUSH(l.getType().sizeof(),"0+X1");
		}
		code += INS("S", NUM_CONST(1), "0+X1");
		return code;
	}

}
