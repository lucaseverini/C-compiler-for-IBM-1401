public  abstract class LValue extends Expression {
	public LValue(Type type) {
		super(type);
	}
	
	public abstract String getLocation();
}
