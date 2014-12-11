package retree.program;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.expression.*;
import retree.type.*;

public class Initializer {
	private VariableExpression variable;
	private ConstantExpression value;
	private boolean isStatic;
	public Initializer(VariableExpression variable, Expression value)  throws NonConstantExpressionException {
		this.variable = variable;
		this.value = null;
		if (value != null) {
			value = value.collapse();
			if (value instanceof ConstantExpression) {
				this.value = (ConstantExpression) value;
			} else {
				throw new NonConstantExpressionException(value);
			}
		}
		this.isStatic = isStatic;
	}

	public String generateCode() {
		String code = INS("SW", variable.getWordMarkAddress());
		if (value == null) {
			return code;
		}
		if (value.getType() instanceof PointerType) {
			return code + INS("MCW", ADDR_CONST(value.getValue()), variable.getAddress());
		} else {
			return code + INS("MCW", CONST(value.getValue()), variable.getAddress());
		}
	}

}
