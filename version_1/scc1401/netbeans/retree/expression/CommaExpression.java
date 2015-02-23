package retree.expression;

public class CommaExpression extends Expression {
	
	private Expression l, r;
	
	public CommaExpression(Expression l, Expression r) {
		super(r.getType());
		this.l = l;
		this.r = r;
	}
	
	
	public String generateCode(boolean valueNeeded) {
		return l.generateCode(false) + r.generateCode(valueNeeded);
	}


}
