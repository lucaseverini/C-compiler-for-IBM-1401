package retree.expression;
import static retree.RetreeUtils.*;
import retree.type.Type;

public class VariableExpression extends LValue {
	private int offset;
	private boolean isStatic;

	public VariableExpression(Type type, int offset, boolean isStatic) {
		super(type);
		this.offset = offset;
		this.isStatic = isStatic;
	}

	public String generateCode(boolean valueNeeded) {
		if (valueNeeded) {
			if (isStatic) {
				return PUSH(ADDR_CONST(offset));
			} else {
				return PUSH(OFF(offset));
			}
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

	public String getAddress() {
		if (isStatic) {
			return offset + "";
		} else {
			return OFF(offset);
		}
	}
}
