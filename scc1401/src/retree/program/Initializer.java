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
		} else if (value.getType().equals(Type.intType)) {
			return code + INS("MCW", NUM_CONST(value.getValue()), variable.getAddress());
		} else if (value.getType().equals(Type.charType)) {
			return code + INS("MCW", CHAR_CONST(value.getValue()), variable.getAddress());
		} else {
			//oops
			return null;
		}
	}
	
	//this generates code that occurs when the initialized
	//variable goes out of scope
	//mainly just clears the word marker
	public String freeCode() {
		return INS("CW", variable.getWordMarkAddress());
	}

}
