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
				return PUSH(getType().sizeof(), ADDR_LIT(offset));
			} else {
				return PUSH(getType().sizeof(), OFF(offset));
			}
		} else return "";
	}

	public LValue collapse() {
		return this;
	}

	public String generateAddress() {
		if (isStatic) {
			return PUSH(3, ADDR_CONST(offset));
		} else {
			return PUSH(3, ADDR_CONST(offset)) +
				INS("MA", "X3", STACK_OFF(0));
		}
	}

	public String getAddress() {
		if (isStatic) {
			return offset + "";
		} else {
			return OFF(offset);
		}
	}

	public String getWordMarkAddress() {
		if (isStatic) {
			return (offset + 1 - getType().sizeof()) + "";
		} else {
			return OFF(offset + 1 - getType().sizeof());
		}
	}

	public boolean isStatic() {
		return isStatic;
	}

	public int getOffset() {
		return offset;
	}
}
