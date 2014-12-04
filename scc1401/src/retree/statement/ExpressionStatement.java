package retree.statement;

import retree.expression.Expression;

public class ExpressionStatement implements Statement {
	private Expression exp;
	public ExpressionStatement(Expression exp) {
		this.exp = exp.collapse();
	}

	public String generateCode() {
		return exp.generateCode();
	}
}
