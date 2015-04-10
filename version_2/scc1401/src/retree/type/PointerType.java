/*
	PointerType.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.type;

public class PointerType extends Type
{
	private Type refType;

	public PointerType(Type refType) 
	{
		super(3);
		
		this.refType = refType;
	}

	public Type getRefType()
	{
		return refType;
	}

	public boolean equals(Type t) 
	{
		if (t instanceof PointerType) 
		{
			if (this.getRefType().equals(((PointerType)t).getRefType()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public String toString() 
	{
		return "*" + refType.toString();
	}
	
	public Boolean isPointerType()
	{
		return true;
	}
}
