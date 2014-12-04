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

	public String generateCode() {
		return l.generateLocation(IDX(1)) +
			r.generateValue(RELADDR(1, "0"));
	}

	public String generateValue(String location) {
		return "";
		// return child.generateValue(IDX(1)) +
		// 	INS("MCW", RELADDR(1, "0"), location);
	}


}
