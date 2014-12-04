package retree.type;

public class PointerType extends Type{
	private Type refType;

	public PointerType(Type refType) {
		super(2);
		this.refType = refType;
	}

	public Type getType(){
		return refType;
	}

	public int getWidth() {
		return 2;
	}

	public boolean equals(Type t) {
		if (t instanceof PointerType) {
			if (this.getType().equals( ((PointerType)t).getType())) {
				return true;
			}
		}
		return false;
	}

}
