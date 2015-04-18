/*
	CastExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.PointerType;
import retree.type.Type;

public class CastExpression extends Expression 
{
	private final Expression child;

	public CastExpression(Type castType, Expression child) 
	{
		super(castType);
		this.child = child;
	}

	@Override
	public Expression collapse() 
	{
		if (child.getType().equals(getType())) 
		{
			return child.collapse();
		}
		
		Expression collapsedChild = child.collapse();
		
		if (collapsedChild instanceof ConstantExpression) 
		{
			// this should handle casting that can be done at compile time
			return new ConstantExpression(getType(), ((ConstantExpression)collapsedChild).getValue());
		} 
		else 
		{
			return new CastExpression(getType(), collapsedChild);
		}
	}

	@Override
	public String generateCode(boolean valueNeeded) 
	{
		String code = child.generateCode(valueNeeded);
		if (valueNeeded) 
		{
			if (getType() instanceof PointerType && (!(child.getType() instanceof PointerType) && child.getType().equals(Type.intType))) 
			{
				code += COM("Cast Number(" + child.toString() + ") to Pointer"); 

				code += SNIP("number_to_pointer");
			} 
			else if ((!(getType() instanceof PointerType) && getType().equals(Type.charType)) && child.getType().equals(Type.intType))
			{
				code += COM("Cast Number(" + child.toString() + ") to Char"); 

				code += SNIP("number_to_char");
			} 
			else if ((!(getType() instanceof PointerType) && getType().equals(Type.intType)) && child.getType() instanceof PointerType)
			{
				code += COM("Cast Pointer(" + child.toString() + ") to Number"); 

				code += SNIP("pointer_to_number");
			} 
			else if ((!(getType() instanceof PointerType) && getType().equals(Type.charType)) && child.getType() instanceof PointerType) 
			{
				code += COM("Cast Pointer(" + child.toString() + ") to Char"); 

				code += SNIP("pointer_to_char");
			}
			else if (getType() instanceof PointerType && (!(child.getType() instanceof PointerType) && child.getType().equals(Type.charType)))
			{
				code += COM("Cast Char(" + child.toString() + ") to Pointer"); 

				code += SNIP("char_to_pointer");
			}
			else if ((!(getType() instanceof PointerType) && getType().equals(Type.intType)) && child.getType().equals(Type.charType))
			{
				code += COM("Cast Char(" + child.toString() + ") to Number"); 

				code += SNIP("char_to_number");
			} 
			else
			{
				// Otherwise we don't need to do anything and our value is already on the stack
			}
		}
		
		return code;
	}
	
	@Override
	public String toString() 
	{
		return "((" + getType() + ") "  + child + ")";	
	}	
}
