package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class SubtractExpression extends Expression {
	private Expression l, r;
	
	public SubtractExpression(Expression l, Expression r) {
		super(strongestType(l.getType(), r.getType()));
		
		if (getType() instanceof PointerType) {
			PointerType ptype = (PointerType) getType();
			try {
				if (! (l.getType() instanceof PointerType)) {
					l = new MultiplyExpression(l, new ConstantExpression(Type.intType, ptype.getType().sizeof()));
				} else if (!(r.getType() instanceof PointerType)) {
					r = new MultiplyExpression(r, new ConstantExpression(Type.intType, ptype.getType().sizeof()));
				}
			} catch (Exception e) {
				//this should never happen, the only exceptions thrown are if we make a multiplyExpression with a pointer type
			}
		}
		
		if (!l.getType().equals(getType())) {
			l = new CastExpression(getType(),l);
		} else if (!r.getType().equals(getType())) {
			r = new CastExpression(getType(),r);
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
		if (getType() instanceof PointerType) {
			Expression r2 = new CastExpression(r.getType(), new SubtractExpression(new ConstantExpression(Type.intType, 16000), new CastExpression(Type.intType, r)));
			return new AddExpression(l, r2).collapse();
		} else {
			Expression l2 = l.collapse();
			Expression r2 = r.collapse();
			if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression) {
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() - ((ConstantExpression)r2).getValue());
			}
			return new SubtractExpression(l2, r2);
		}
	}
	
	public String generateCode(boolean valueNeeded) {
		String code = COM("Subtraction(" + l + "-" + r + ")") +
		l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
		if (valueNeeded) {
			code += INS("S", STACK_OFF(0), STACK_OFF(-r.getType().sizeof()));
			code += POP(r.getType().sizeof());
		}
		return code;
	}
	
	public String toString() {
		return "(" + l + " - " + r + ")";
		
	}
}
