package retree.type;
public class ArrayType extends Type{
	private int sizeOfArray;
	private Type arrayBaseType;
	private int width;

	public ArrayType(int size, Type baseType) {
		super(size * baseType.getWidth());
		sizeOfArray = size;
		arrayBaseType = baseType;
	}

	public int getSizeOfArray() {
		return sizeOfArray;
	}

	public Type getArrayBaseType() {
		return arrayBaseType;
	}
}
