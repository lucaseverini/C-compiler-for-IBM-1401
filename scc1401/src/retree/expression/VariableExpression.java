package retree.expression;
import static retree.RetreeUtils.*;
import retree.type.Type;

public class VariableExpression extends LValue {
	private int offset; //negative means absolute location / global

	public VariableExpression(Type type, int offset) {
		super(type);
		this.offset = offset;
	}

	public String generateCode(boolean valueNeeded) {
		if (valueNeeded) {
			return PUSH(OFF(offset));
		} else return "";
	}

	public LValue collapse() {
		return this;
	}

	public String generateAddress() {
		if (offset < 0) {
			offset = -offset;
			return PUSH(ADDR_CONST(offset));
		} else {
			return PUSH(ADDR_CONST(offset)) +
				INS("MA", "X3", STACK_REF(1));
		}
	}
}
