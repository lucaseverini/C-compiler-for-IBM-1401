package retree.program;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.expression.*;
import retree.type.*;

public class Initializer {
	private VariableExpression variable;
	private ConstantExpression value;
	public Initializer(VariableExpression variable, Expression value)  throws NonConstantExpressionException {
		this.variable = variable;
		value = value.collapse();
		if (value instanceof ConstantExpression) {
			this.value = (ConstantExpression) value;
		} else {
			throw new NonConstantExpressionException(value);
		}
	}

	public String generateCode() {
		//System.out.println(variable);
		//System.out.println(value);
		if (value.getType() instanceof PointerType) {
			return INS("MCW", ADDR_CONST(value.getValue()), variable.getAddress());
		} else {
			return INS("MCW", CONST(value.getValue()), variable.getAddress());
		}
	}

}
