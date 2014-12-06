package retree.expression;
import static retree.RetreeUtils.*;
import retree.expression.Expression;
import retree.type.PointerType;
import retree.type.Type;

public Class CastExpression extends Expression {
	private Expression child;
	
	public CastExpression(Type castType, Expression child) {
		super(castType);
		this.child = child;
	}
	
	public Expression collapse() {
		Expression collapsedChild = child.collapse();
		if (collapseedChild instanceof ConstantExpression) {
			//this should handle casting that can be done at compile time
			return new ConstantExpression(getType(), collapsedChild);
		} else {
			return new CastExpression(getType(), collapsedChild);
		}
	}
	
	public String generateCode(boolean valueNeeded) {
		String code = child.generateCode(valueNeeded);
		if (valueNeeded) {
			if (getType() instanceof PointerType && !child.getType() instanceof PointerType()) {
				//Cast to pointer - todo
			} else if (!getType() instanceof PointerType && child.getType() instanceof PointerType()) {
				//Cast from pointer - todo
			}
			//otherwise we don't need to do anything and our value is already on the stack.
		}
		return code;
	}

}
