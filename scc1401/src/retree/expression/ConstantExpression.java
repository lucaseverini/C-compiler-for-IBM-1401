package retree.expression;
import static retree.RetreeUtils.*;
import retree.type.*;
public class ConstantExpression extends Expression{
	private int val; //everything should fit into an int.

	public ConstantExpression(Type type, int val) {
		super(type);
		this.val = val;
	}

	public String generateCode(boolean valueNeeded) {
		if (valueNeeded) {
			if (getType() instanceof PointerType) {
				return PUSH(getType().sizeof(), ADDR_CONST(val));
			} else if (getType().equals(Type.intType)) {
				return PUSH(getType().sizeof(), NUM_CONST(val));
			} else if (getType().equals(Type.charType)) {
				return PUSH(getType().sizeof(), CHAR_CONST(val));
			} else {
				//oops
				return null;
			}
		} else {
			return "";
		}
	}

	public int getValue() {
		return val;
	}

}
