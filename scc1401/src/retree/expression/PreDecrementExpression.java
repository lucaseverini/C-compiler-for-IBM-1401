package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class PreDecrementExpression extends Expression {
	private LValue l;

	public PreDecrementExpression(LValue l) throws TypeMismatchException {
		super(l.getType());
		this.l = l;
	}

	public Expression collapse() {
		try {
			return new PreDecrementExpression(l.collapse());
		} catch (retree.exceptions.TypeMismatchException e){
			return null;
		}
	}

	public String generateCode(boolean valueNeeded) {
		String code = l.generateAddress();
		// String code = l.generateCode(valueNeeded);
		code += POP(3, "X1");
		if (getType() instanceof PointerType) {
			PointerType pt = (PointerType) getType();
			code += INS("MA", ADDR_CONST(-pt.getType().sizeof()), "0+X1");
		} else {
			code += INS("S", NUM_CONST(1), "0+X1");
		}
		if (valueNeeded) {
			code += PUSH(l.getType().sizeof(),"0+X1");
		}
		return code;
	}

	public String toString()
	{
		return "(" + "--" + l + ")";
	}

}
