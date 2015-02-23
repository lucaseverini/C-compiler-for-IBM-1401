package retree.expression;
import retree.type.*;
import static retree.RetreeUtils.*;

public class ArrayNameExpression extends Expression{
	private VariableExpression array;
	private ArrayType arrayType;

	public ArrayNameExpression(VariableExpression array) {
		super(new PointerType(((ArrayType) array.getType()).getArrayBaseType()));
		this.array = array;
		this.arrayType = (ArrayType) array.getType();
	}

	//for stack management reasons, address of a pointer variable is at the end
	//of that variable
	//we need to get a pointer to the beginning.
	public String generateCode(boolean valueNeeded) {
		if (!valueNeeded) return "";
		if (array.isStatic()) {
			return PUSH(3, ADDR_CONST(array.getOffset() + arrayType.getArrayBaseType().sizeof() - arrayType.sizeof()));
		} else {
				return PUSH(3, ADDR_CONST(array.getOffset() + arrayType.getArrayBaseType().sizeof() - arrayType.sizeof())) +
				INS("MA", "X3", STACK_OFF(0));
		}
	}

	public VariableExpression getArray() {
		return array;
	}

	public Expression collapse() {
		if (this.array.isStatic())
		{
			return new ConstantExpression(new PointerType(((ArrayType) array.getType()).getArrayBaseType()),
						array.getOffset() + arrayType.getArrayBaseType().sizeof() - arrayType.sizeof());
		}
		return this;
	}

	public String toString() {
		return array.toString();
	}
}
