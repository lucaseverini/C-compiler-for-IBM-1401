package retree.expression;
import static retree.RetreeUtils.*;
import retree.expression.Expression;
import retree.type.PointerType;
import retree.type.Type;

public class CastExpression extends Expression {
	private Expression child;

	public CastExpression(Type castType, Expression child) {
		super(castType);
		this.child = child;
	}

	public Expression collapse() {
		if (child.getType().equals(getType())) return child.collapse();
		Expression collapsedChild = child.collapse();
		if (collapsedChild instanceof ConstantExpression) {
			//this should handle casting that can be done at compile time
			return new ConstantExpression(getType(), ((ConstantExpression)collapsedChild).getValue());
		} else {
			return new CastExpression(getType(), collapsedChild);
		}
	}

	public String generateCode(boolean valueNeeded) {
		String code = child.generateCode(valueNeeded);
		if (valueNeeded) {
			if (getType() instanceof PointerType && (!(child.getType() instanceof PointerType) && child.getType().equals(Type.intType))) {
				code += SNIP("number_to_pointer");
			} else if ((!(getType() instanceof PointerType) && getType().equals(Type.intType)) && child.getType() instanceof PointerType) {
				code += SNIP("pointer_to_number");
			} else if (getType() instanceof PointerType && (!(child.getType() instanceof PointerType) && child.getType().equals(Type.charType))) {
				code += SNIP("char_to_pointer");
			} else if ((!(getType() instanceof PointerType) && getType().equals(Type.charType)) && child.getType() instanceof PointerType) {
				code += SNIP("pointer_to_char");
			}
			//otherwise we don't need to do anything and our value is already on the stack.
		}
		return code;
	}
	
	public String toString() {
		return "((" + getType() + ") "  + child + ")";
		
	}
	
}
