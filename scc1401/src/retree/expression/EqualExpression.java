package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class EqualExpression extends Expression {
	private Expression l, r;

	public EqualExpression(Expression l, Expression r) throws TypeMismatchException {
		super(Type.intType);
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
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() == ((ConstantExpression)r2).getValue() ? 1 : 0);
			}
			return new EqualExpression(l2, r2);
		} catch (TypeMismatchException e) {
			//should never happen
			return null;
		}
	}

	public String generateCode(boolean valueNeeded) {
		String labelEqual = label(SmallCC.nextLabelNumber());
		String labelEnd = label(SmallCC.nextLabelNumber());
		String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
		if (valueNeeded) {
			int size = l.getType().sizeof();
			
			code += INS("C", STACK_OFF(0), STACK_OFF(-size));
			code += POP(size) + POP(size);
			code += PUSH(Type.intType.sizeof(), NUM_CONST(0));
			
			if (l.getType().equals(Type.intType)) {				
				//branch to end if less than or greater than
				code += INS("B", labelEnd, "T");
				code += INS("B", labelEnd, "U");
				code += INS("B", labelEqual);
			} else {
				code += INS("B", labelEqual, "S");
			}

			code += INS("B", labelEnd);
			code += LBL_INS(labelEqual, "MCW", NUM_CONST(1), STACK_OFF(0));
			code += LBL_INS(labelEnd, "NOP");

		}
		return code;
	}

}
