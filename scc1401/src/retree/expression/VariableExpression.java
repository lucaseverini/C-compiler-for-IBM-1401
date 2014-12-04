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
			PUSH(OFF(offset));
		} else return "";
	}

	public LValue collapse() {
		return this;
	}

	public String generateLocation() {
		PUSH("X3"); //to be continued...
	}
}
