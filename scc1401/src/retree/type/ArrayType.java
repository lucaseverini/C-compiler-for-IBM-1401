public class ArrayType {
	private int sizeOfArray;
	private Type arrayBaseType;
	private int width;

	public ArrayType(int size, Type baseType) {
		sizeOfArray = size;
		arrayBaseType = baseType;
		width = size * baseType.getWidth();
	}

	public int getWidth() {
		return width;
	}

	public int getSizeOfArray() {
		return sizeOfArray;
	}

	public Type getArrayBaseType() {
		return arrayBaseType;
	}
}
