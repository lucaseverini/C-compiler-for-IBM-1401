/*
	PointerType.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.type;

public class PointerType extends Type
{
	private final Type refType;

	public PointerType(Type refType) 
	{
		super(3);
		
		this.refType = refType;
	}

	public Type getRefType()
	{
		return refType;
	}

	@Override
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
	
	@Override
	public String toString() 
	{
		return "*" + refType.toString();
	}
	
	@Override
	public Boolean isPointerType()
	{
		return true;
	}
}
