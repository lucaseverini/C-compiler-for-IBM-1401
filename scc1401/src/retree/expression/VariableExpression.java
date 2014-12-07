package retree.expression;
import static retree.RetreeUtils.*;
import retree.type.Type;

public class VariableExpression extends LValue {
	private int offset; //negative means absolute location / global

	public VariableExpression(Type type, int offset) {
		super(type);
		this.offset = offset;
	}

	public String generateCode() {
		return "";
	}

	public LValue collapse() {
		return null;
	}

	//remember this should always call generateCode as well.
	public String generateValue(String location) {
		return INS("MCW", RELADDR("X3", offset), location);
	}

	public String generateLocation(String location) {
		if (offset >= 0) {
			return INS("MCW", "X3", location) +
				INS("A", CONST(offset), location);
		} else {
			return INS("MCW", CONST(-offset), location);
		}
	}
}
