package retree.exceptions;
import retree.expression.Expression;
public class NonConstantExpressionException extends Exception {
	private Expression exp;

	public NonConstantExpressionException(Expression exp) {
		this.exp = exp;
	}

}
