public  abstract class LValue extends Expression {
	public LValue(Type type) {
		super(type);
	}
	
	@Override
	public abstract LValue collapse();
	
	//remember this needs to generate side effects.	
	public abstract String generateLocation(String location);
}
