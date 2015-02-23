package retree.type;

public class ArrayType extends Type
{
	private final int numElements;
	private final Type arrayBaseType;
	public String lastElementLabel;

	public ArrayType(int numElements, Type baseType) 
	{
		super(numElements * baseType.sizeof());
		
		this.numElements = numElements;
		arrayBaseType = baseType;
		lastElementLabel = null;
	}

	public Type getArrayBaseType()
	{
		return arrayBaseType;
	}
	
	public String toString() 
	{
		return arrayBaseType.toString() + " [" + numElements + "]";
	}
}
