public class VariableNode extends SimpleNode {

	private Type t;

	// negative is global positive is local
	private int offset = 0;

	public VariableNode(Type t, int offset) {
		this.t = t;
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public Type getType() {
		return t;
	}
}
