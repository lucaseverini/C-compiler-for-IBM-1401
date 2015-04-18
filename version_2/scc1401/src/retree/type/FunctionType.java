/*
	FunctionType.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.type;

import java.util.List;

public class FunctionType extends Type
{
	private final List<Type> paramTypes;
	private final Type returnType;
	private final boolean variadic;

	public FunctionType(Type returnType, List<Type> paramTypes, boolean variadic) 
	{
		super(0); //has no defined size
		
		this.returnType = returnType;
		this.paramTypes = paramTypes;
		this.variadic = variadic;
	}

	public List<Type> getParamTypes()
	{
		return paramTypes;
	}

	public Type getReturnType()
	{
		return returnType;
	}

	public boolean getVariadic()
	{
		return variadic;
	}

	public boolean Equals(Object other) 
	{
		if (!(other instanceof FunctionType))
		{
			return false;
		}
		
		FunctionType f = (FunctionType)other;
		
		if (!f.getReturnType().equals(returnType)) 
		{
			return false;
		}
		
		if (f.getParamTypes().size() != paramTypes.size()) 
		{
			return false;
		}
		
		for (int i = 0; i < paramTypes.size(); ++i) 
		{
			if (!f.getParamTypes().get(i).equals(paramTypes.get(i))) 
			{
				return false;
			}
		}
		
		return variadic == f.variadic;
	}

	@Override
	public String toString() 
	{
		String str = returnType.toString() + " (";
		
		for (Type paramType : paramTypes) 
		{
			str += paramType.toString() + ", ";
		}
		
		if (variadic) 
		{
			str += "...";
		}
		
		return str + ")";
	}

	@Override
	public Boolean isPointerType()
	{
		return true;
	}
}
