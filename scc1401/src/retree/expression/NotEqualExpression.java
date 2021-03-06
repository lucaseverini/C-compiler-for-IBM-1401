package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class NotEqualExpression extends Expression {
	private Expression l, r;

	public NotEqualExpression(Expression l, Expression r) throws TypeMismatchException {
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
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() == ((ConstantExpression)r2).getValue() ? 0 : 1);
			}
			return new NotEqualExpression(l2, r2);
		} catch (TypeMismatchException e) {
			//should never happen
			return null;
		}
	}

	public String generateCode(boolean valueNeeded) {
		String labelEqual = label(SmallCC.nextLabelNumber());
		String labelEnd = label(SmallCC.nextLabelNumber());
		String code = l.generateCode(valueNeeded);
		if (valueNeeded && l.getType().equals(Type.intType)) {
			code += SNIP("clean_number");
		}
		code += r.generateCode(valueNeeded);
		if (valueNeeded && r.getType().equals(Type.intType)) {
			code += SNIP("clean_number");
		}
		if (valueNeeded) {
			int size = l.getType().sizeof();

			code += INS("C", STACK_OFF(0), STACK_OFF(-size));
			code += POP(size) + POP(size);
			code += PUSH(Type.intType.sizeof(), NUM_CONST(1));

			code += INS("BE", labelEqual);
			code += INS("B", labelEnd);
			code += LBL_INS(labelEqual, "MCW", NUM_CONST(0), STACK_OFF(0));
			code += LBL_INS(labelEnd, "NOP");

		}
		return code;
	}

	public String toString()
	{
		return "(" + l + " != " + r + ")";
	}

}
