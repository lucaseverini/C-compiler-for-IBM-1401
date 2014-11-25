public class Type {
	
	public static final Type intType = new Type(2);
	public static final Type charType = new Type(1);

	private int width = 0;

	public type(int width){
		this.width = width;
	}

	public boolean equals(Type t) {
		return this == t;
	}

	public int getWidth() {
		return width;
	}
}
