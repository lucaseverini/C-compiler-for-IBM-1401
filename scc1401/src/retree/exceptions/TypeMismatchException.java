public class TypeMismatchException extends Exception {
	private Expression exp;
	private Type expected, found;
	
	public TypeMismatchException(Expression exp, Type expected, Type found) {
		this.exp = exp;
		this.expected = expected;
		this.found = found;
	}
}
