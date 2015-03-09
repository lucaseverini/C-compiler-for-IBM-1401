/*
	Type.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.type;

public class Type 
{
	public static final Type intType = new Type(5) 
	{
		public String toString() 
		{
			return "int";
		}
	};
	
	public static final Type charType = new Type(1) 
	{
		public String toString() 
		{
			return "char";
		}
	};

	private int size;

	public Type(int size)
	{
		this.size = size;
	}

	public boolean equals(Type t) 
	{
		return this == t;
	}

	public int sizeof() 
	{
		return size;
	}
}
