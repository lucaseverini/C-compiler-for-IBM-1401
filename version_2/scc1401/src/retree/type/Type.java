/*
	Type.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.type;

public class Type 
{
	private final int size;

	public static final Type intType = new Type(5) 
	{
		@Override
		public String toString() 
		{
			return "int";
		}
	};
	
	public static final Type charType = new Type(1) 
	{
		@Override
		public String toString() 
		{
			return "char";
		}
	};

	public Type(int size)
	{
		this.size = size;
	}

	public int getSize() 
	{
		return size;
	}

	public int sizeof() 
	{
		return size;
	}

	public boolean equals(Type t) 
	{
		return this == t;
	}

	public Boolean isPointerType()
	{
		return false;
	}
}
