import static RetreeUtils.*;

public class AddressOfExpression extends Expression {
	private LValue child;
	
	public AddressOfExpression(LValue child) {
		super(new PointerType(child.getType()));
		this.child = child;
	}
	
	public String generateCode() {
		return child.generateCode();
	}
	
	public Expression collapse() {
		return new AddressOfExpression(child.collapse);
	}
	
	public abstract String generateValue(String location) {
		return generateCode() + 
			INS("MCW", child.getLocation(), location);
	}
	

}
