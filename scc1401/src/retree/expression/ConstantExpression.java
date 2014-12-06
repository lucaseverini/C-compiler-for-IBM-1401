package retree.expression;
import static retree.RetreeUtils.*;
import retree.type.Type;
public class ConstantExpression extends Expression{
	private int val; //everything should fit into an int.

	public ConstantExpression(Type type, int val) {
		super(type);
		this.val = val;
	}

	public String generateCode(boolean valueNeeded) {
		if (valueNeeded) {
			return PUSH(CONST(val));
		} else {
			return "";
		}
	}

	public int getValue() {
		return val;
	}

}
