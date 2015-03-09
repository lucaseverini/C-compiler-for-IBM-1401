/*
	ArrayType.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.type;

public class ArrayType extends Type
{
	private final int numElements;
	private final Type arrayBaseType;

	public ArrayType(int numElements, Type baseType) 
	{
		super(numElements * baseType.sizeof());
		
		this.numElements = numElements;
		arrayBaseType = baseType;
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
