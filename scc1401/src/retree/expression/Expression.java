package retree.expression;
import retree.type.Type;
public abstract class Expression {
	private Type type;

	public Expression(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public Expression collapse() {
		return this;
	}

	public abstract String generateCode();

	//remember this should always call generateCode as well.
	public abstract String generateValue(String location);
}
