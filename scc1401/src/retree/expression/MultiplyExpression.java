package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class MultiplyExpression extends Expression {
	private Expression l, r;

	public MultiplyExpression(Expression l, Expression r) throws Exception {
		super(strongestType(l.getType(), r.getType()));
		if (getType() instanceof PointerType) {
			throw new Exception("Invalid types for operator *: " + l.getType() + " and " + r.getType());
		}
		if (!l.getType().equals(getType())) {
			l = new CastExpression(getType(), l);
		} else if (!r.getType().equals(getType())) {
			l = new CastExpression(getType(), r);
		}
		this.l = l;
		this.r = r;
	}

	private static Type strongestType(Type a, Type b) {
		if (a instanceof PointerType) return a;
		if (b instanceof PointerType) return b;
		if (a.equals(Type.intType) || b.equals(Type.intType)) return Type.intType;
		return a;
	}

	public Expression collapse() {
		try {
			Expression l2 = l.collapse();
			Expression r2 = r.collapse();
			if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression) {
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() * ((ConstantExpression)r2).getValue());
			}
			return new MultiplyExpression(l2, r2);
		} catch (Exception e) {
			//should never happen
			return null;
		}
	}

	public String generateCode(boolean valueNeeded) {
		int size = r.getType().sizeof();
		String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
		if (valueNeeded) {
			code += INS("M", STACK_OFF(-size), STACK_OFF(size+1));
			//this puts the product at size + 1 bits above the stack
			code += INS("SW", STACK_OFF(2));
			code += INS("MCW", STACK_OFF(size+1), STACK_OFF(-size));
			code += INS("CW", STACK_OFF(2));
			code += POP(size);
		}
		return code;
	}

	public String toString()
	{
		return "(" + l + " * " + r + ")";
	}

}
