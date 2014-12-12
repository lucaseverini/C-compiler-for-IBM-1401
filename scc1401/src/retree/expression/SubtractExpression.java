package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class SubtractExpression extends Expression {
	private Expression l, r;
	
	public SubtractExpression(Expression l, Expression r) throws TypeMismatchException {
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
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() - ((ConstantExpression)r2).getValue());
			}
			return new SubtractExpression(l2, r2);
		} catch (TypeMismatchException e) {
			//should never happen
			return null;
		}
	}
	
	public String generateCode(boolean valueNeeded) {
		String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
		if (valueNeeded) {
			if (l.getType() instanceof PointerType) {
				//unimplemented :(
				return null;
			} else {
				code += INS("S", STACK_OFF(0), STACK_OFF(-r.getType().sizeof()));
			}
			code += POP(r.getType().sizeof());
		}
		return code;
	}
}
