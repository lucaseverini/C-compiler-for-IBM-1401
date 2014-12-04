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
<<<<<<< HEAD
	
	public abstract String generateCode(boolean valueNeeded);
=======

	public abstract String generateCode();

	//remember this should always call generateCode as well.
	public abstract String generateValue(String location);
>>>>>>> 7e197e91f458d23062ce2b98e54af40772fcb605
}
