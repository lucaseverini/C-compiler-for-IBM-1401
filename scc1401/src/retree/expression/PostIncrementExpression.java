package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class PostIncrementExpression extends Expression {
	private Expression l;

	public PostIncrementExpression(Expression l) throws TypeMismatchException {
		super(l.getType());
		if (l.getType().equals(Type.charType)) {
			// throw new TypeMismatchException(r, l.getType(), r.getType());
			throw new Exception("You cannot pre inc a char type!");
		}
		this.l = l;
	}

	public Expression collapse() {
		try {
			return new PostIncrementExpression(l.collapse());
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
		code += INS("A", NUM_CONST(1), "0+X1");
		return code;
	}

}
