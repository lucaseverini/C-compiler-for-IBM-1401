package retree.program;
import static rewtree.RetreeUtils.*;

public class Initializer {
	private VariableExpression variable;
	private int value;
	public Initializer(VariableExpression var, Expression val) {
		this.variable = variable;
		val = val.collapse();
		if (val instanceof ConstantExpression) {
			this.val = ((ConstantExpression)val).getValue();
		} else {
			throw new NonConstantExpressionException(val);
		}
	}

	public String generateCode() {
		return INS("MCW", COD(value), variable.getLocation());
	}

}
