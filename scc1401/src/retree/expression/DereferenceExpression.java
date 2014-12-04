import static RetreeUtils.*;

public class DereferenceExpression extends LValue {
	private Expression child;
	
	public DereferenceExpression(Expression child) throws TypeMismatchException {
		super(getReferenceType(child));
		this.child = child;
	}
	
	public String generateCode() {
		return child.generateCode();
	}
	
	private static Type getReferenceType(Expression exp) throws TypeMismatchException {
		if (! exp.getType() instanceof PointerType) {
			throw new TypeMismatchException(exp, new PointerType(epx.getType()), exp.getType());
		}
		return ((PointerType) exp.getType()).getType();
	}
	
	public Expression collapse() {
		return new AddressOfExpression(child.collapse());
	}
	
	public String generateCode() {
		return child.generateCode();
	}
	
	public String generateValue(String location) {
		return child.generateValue(IDX(1)) + 
			INS("MCW", RELADDR(1, "0"), location);
	}
	
	public String generateLocation(String location) {
		return child.generateValue(location);
	}
	

}
