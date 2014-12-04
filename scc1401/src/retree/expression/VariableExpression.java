import static RetreeUtils.*;

public class VariableExpression extends LValue {
	private int offset; //negative means absolute location / global
	
	public VariableExpression(Type type, int offset) {
		super(type);
		this.offset = offset;
	}

	public String generateCode() {
		return "";
	}
	//remember this should always call generateCode as well.
	public String generateValue(String location) {
		return INS("MCW", OFF(offset), location);
	}
	
	public String generateLocation(String location) {
		if (offset >= 0) {
			return INS("MCW", "SP", location) +
				INS("A", CONST(offset), location);
		} else {
			return INS("MCW", CONST(-offset), location);
		} 
	}
}
