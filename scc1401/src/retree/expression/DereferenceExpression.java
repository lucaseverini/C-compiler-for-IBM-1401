package retree.expression;
import retree.exceptions.TypeMismatchException;
import static retree.RetreeUtils.*;
import retree.expression.Expression;
import retree.expression.LValue;
import retree.type.PointerType;
import retree.type.Type;
public class DereferenceExpression extends LValue {
	private Expression child;

	public DereferenceExpression(Expression child) throws retree.exceptions.TypeMismatchException {
		super(getReferenceType(child));
		this.child = child;
	}

	private static Type getReferenceType(Expression exp) throws retree.exceptions.TypeMismatchException {
		if (!(exp.getType() instanceof PointerType)) {
			throw new retree.exceptions.TypeMismatchException(exp, new PointerType(exp.getType()), exp.getType());
		}
		return ((PointerType) exp.getType()).getType();
	}

	public LValue collapse() {
		return new DereferenceExpression(child.collapse());
	}

	public String generateCode(boolean valueNeeded) {
		if (!valueNeeded) {
			return child.generateCode(false);
		} else {
			return child.generateCode(true) + 
				POP("X1") +
				PUSH("0+X1");
		}
	}
	
	public String generateAddress() {
		return child.generateCode(true);
	}

}
