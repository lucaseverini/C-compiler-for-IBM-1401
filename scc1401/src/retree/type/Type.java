package retree.type;
public class Type {

	public static final Type intType = new Type(5) {
			public String toString() {
				return "int";
			}
	};
	public static final Type charType = new Type(5) {
		public String toString() {
			return "char";
		}
	};

	private int width = 5;

	public Type(int width){
		this.width = width;
	}

	public boolean equals(Type t) {
		return this == t;
	}

	public int getWidth() {
		return width;
	}
}
