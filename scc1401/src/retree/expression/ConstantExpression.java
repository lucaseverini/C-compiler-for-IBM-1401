import static RetreeUtils.*;

public class ConstantExpression extends Expression{
	private int val; //everything should fit into an int.
	
	public ConstantExpression(Type type, int val) {
		super(type);
		this.val = val;
	}

	public String generateCode() {
		return "";
	}
	
	public String generateValue(String location) {
		return INS("MCW", CONST(val), location); 
	}

}
