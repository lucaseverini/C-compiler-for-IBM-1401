package retree.expression;
import static retree.RetreeUtils.*;
import retree.expression.LValue;
import retree.expression.Expression;
public class AssignmentExpression extends Expression {
	private LValue l;
	private Expression r;

	public AssignmentExpression(LValue l, Expression r) throws retree.exceptions.TypeMismatchException {
		super(l.getType());
		if (! l.getType().equals(r.getType())) {
			throw new retree.exceptions.TypeMismatchException(r, l.getType(), r.getType());
		}
		this.l = l;
		this.r = r;
	}

	public Expression collapse() {
		try {
			return new AssignmentExpression(l.collapse(), r.collapse());
		} catch (retree.exceptions.TypeMismatchException e){
			return null;
		}
	}

	public String generateCode(boolean valueNeeded) {
		String code = l.generateAddress() +
			POP("X1") +
			r.generateCode(true);
		if (valueNeeded) {
			code += INS("MCW", STACK_REF(1), "0+X1");
		} else {
			code += POP("0+X1");
		}
		return code;
	}


}
