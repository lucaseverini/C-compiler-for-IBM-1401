package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class MultiplyExpression extends Expression {
	private Expression l, r;
	
	public MultiplyExpression(Expression l, Expression r) throws TypeMismatchException {
		super(l.getType());
		if (! l.getType().equals(r.getType())) {
			throw new TypeMismatchException(r, l.getType(), r.getType());
		}
		this.l = l;
		this.r = r;
	}
	
	public Expression collapse() {
		try {
			Expression l2 = l.collapse();
			Expression r2 = r.collapse();
			if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression) {
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() + ((ConstantExpression)r2).getValue());
			}
			return new AddExpression(l2, r2);
		} catch (TypeMismatchException e) {
			//should never happen
			return null;
		}
	}
	
	public String generateCode(boolean valueNeeded) {
		String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
		if (valueNeeded) {
			if (l.getType() instanceof PointerType) {
				code += INS("MA", STACK_REF(1), STACK_REF(2));
			} else {
				code += INS("A", STACK_REF(1), STACK_REF(2));
			}
			code += POP();
		}
		return code;
	}	

}
