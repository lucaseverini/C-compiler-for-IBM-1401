public class ConstantExpression extends Expression{
	private int val; //everything should fit into an int.
	
	public ConstantExpression(Type type, int val) {
		super(type);
		this.val = val;
	}

	public String generateCode() {
		return "";
	}
	//remember this should always call generateCode as well.
	public String generateValue(int offset) {
		
	}

}
