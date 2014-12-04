import static RetreeUtils.*;

public class AssignmentExpression extends Expression {
	private LValue l;
	private Expression r;
	
	public AssignmentExpression(LValue l, Expression r) throws TypeMismatchException {
		super(l.getType());
		if (! l.getType().equals(r.getType)) {
			throw new TypeMismatchException(r, l.getType(), r.getType());
		}
		this.l = l;
		this.r = r;
	}
	
	public Expression collapse() {
		return new AssignmentExpression(r.collapse(), l.collapse);
	}
	
	public String generateCode() {
		return l.generateLocation(IDX(1)) + 
			r.generateValue(RELADDR(1, 0)); 
	}
	
	public String generateValue(String location) {
		
		return child.generateValue(IDX(1)) + 
			INS("MCW", RELADDR(1, "0"), location);
	}
	

}
