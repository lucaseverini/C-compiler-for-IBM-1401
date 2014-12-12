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
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() * ((ConstantExpression)r2).getValue());
			}
			return new MultiplyExpression(l2, r2);
		} catch (TypeMismatchException e) {
			//should never happen
			return null;
		}
	}
	
	public String generateCode(boolean valueNeeded) {
		int size = r.getType().sizeof();
		String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
		if (valueNeeded) {
			if (l.getType() instanceof PointerType) {
				//TODO
				//code += INS("MA", STACK_REF(1), STACK_REF(2));
			} else {
				code += INS("M", STACK_OFF(-size), STACK_OFF(size+1));
				//this puts the product at size + 1 bits above the stack
				code += INS("SW", STACK_OFF(2));
				code += INS("MCW", STACK_OFF(size+1), STACK_OFF(-size));
				code += INS("CW", STACK_OFF(2));
			}
			code += POP(size);
		}
		return code;
	}

}
