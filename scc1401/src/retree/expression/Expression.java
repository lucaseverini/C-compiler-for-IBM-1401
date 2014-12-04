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
	
	public abstract String generateCode(boolean valueNeeded);
}
