package retree.type;

public class PointerType extends Type
{
	private Type refType;

	public PointerType(Type refType) 
	{
		super(3);
		
		this.refType = refType;
	}

	public Type getType()
	{
		return refType;
	}

	public boolean equals(Type t) 
	{
		if (t instanceof PointerType) 
		{
			if (this.getType().equals( ((PointerType)t).getType()))
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
}
