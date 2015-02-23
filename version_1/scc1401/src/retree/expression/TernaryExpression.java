package retree.expression;

import retree.exceptions.TypeMismatchException;
import compiler.SmallCC;
import static retree.RetreeUtils.*;


public class TernaryExpression extends Expression {
	private Expression condition, positive, negative;


	public TernaryExpression(Expression condition, Expression positive, Expression negative) throws TypeMismatchException {
		super(positive.getType());
		if (!positive.getType().equals(negative.getType())) throw new TypeMismatchException(negative, positive.getType(), negative.getType());
		this.condition = condition;
		this.positive = positive;
		this.negative = negative;
	}
	
	
	public Expression collapse() {
		Expression c2 = condition.collapse();
		if (c2 instanceof ConstantExpression) {
			ConstantExpression c = (ConstantExpression)c2;
			return (c.getValue() == 0) ? negative.collapse() : positive.collapse();
		}
		try {
			return new TernaryExpression(c2, positive.collapse(), negative.collapse());
		} catch(TypeMismatchException e) {
			return null;
		}
	}
	
	public String generateCode(boolean valueNeeded) {
		String negLabel = label(SmallCC.nextLabelNumber());
		String endLabel = label(SmallCC.nextLabelNumber());
		String code = condition.generateCode(true);
		code += INS("MCS", STACK_OFF(0), STACK_OFF(0));
		code += POP(condition.getType().sizeof());
		code += INS("BCE", negLabel, STACK_OFF(condition.getType().sizeof()), " ");
		code += positive.generateCode(valueNeeded);
		code += INS("B", endLabel);
		code += LBL_INS(negLabel, "NOP");
		code += negative.generateCode(valueNeeded);
		code += LBL_INS(endLabel, "NOP");
		return code;
		
	}
}
