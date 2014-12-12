package retree.expression;
import retree.exceptions.TypeMismatchException;
import static retree.RetreeUtils.*;
import retree.expression.Expression;
import retree.expression.LValue;
import retree.type.PointerType;
import retree.type.Type;

public class NegExpression extends Expression {
	private Expression child;

	public NegExpression(Expression child) {
		super(child.getType());
		this.child = child;
	}
	
	public Expression collapse() {
		Expression collapsedChild = child.collapse();
		if (collapsedChild instanceof ConstantExpression)
			return new ConstantExpression(collapsedChild.getType(), -((ConstantExpression)collapsedChild).getValue());
		else
			return new NegExpression(collapsedChild);
	}

	public String generateCode(boolean valueNeeded) {
		String code = child.generateCode(valueNeeded);
		if (valueNeeded) {
			if (child.getType() instanceof PointerType) {
				//todo
			} else {
				code += INS("ZS", STACK_OFF(0));
			}
		}
		return code;
	}

}
