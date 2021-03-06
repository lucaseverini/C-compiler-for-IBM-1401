package retree.type;
public class ArrayType extends Type{
	private int numElements;
	private Type arrayBaseType;

	public ArrayType(int numElements, Type baseType) {
		super(numElements * baseType.sizeof());
		this.numElements = numElements;
		arrayBaseType = baseType;
	}

	public Type getArrayBaseType() {
		return arrayBaseType;
	}
	
	public String toString() {
		return arrayBaseType.toString() + 
			" [" + numElements + "]";
	}
}
