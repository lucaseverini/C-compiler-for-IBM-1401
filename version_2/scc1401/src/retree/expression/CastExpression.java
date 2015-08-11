/*
	CastExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;

import compiler.SmallCC;
import retree.type.PointerType;
import retree.type.Type;

public class CastExpression extends Expression 
{
	private final Expression child;

	public CastExpression(Type castType, Expression child) 
	{
		super(castType);
		associativity = false;
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
				if (SmallCC.nostack)
				{
					code += INS("Move child val to CAST reg", null, "MCW", REG(child), "CAST");
				}
				code += SNIP("number_to_pointer");
				if (SmallCC.nostack)
				{
					code += INS("Move result to " + REG(this), null, "LCA", "CSTRES", REG(this));
				}
			} 
			else if ((!(getType() instanceof PointerType) && getType().equals(Type.charType)) && child.getType().equals(Type.intType))
			{
				code += COM("Cast Number(" + child.toString() + ") to Char");
				if (SmallCC.nostack)
				{
					code += INS("Move child val to CAST reg", null, "MCW", REG(child), "CAST");
				}
				code += SNIP("number_to_char");
				if (SmallCC.nostack)
				{
					code += INS("Move result to " + REG(this), null, "LCA", "CAST", REG(this));
				}
			} 
			else if ((!(getType() instanceof PointerType) && getType().equals(Type.intType)) && child.getType() instanceof PointerType)
			{
				code += COM("Cast Pointer(" + child.toString() + ") to Number"); 
				if (SmallCC.nostack)
				{
					code += INS("Move child val to CSTRES reg", null, "MCW", REG(child), "CSTRES");
				}
				code += SNIP("pointer_to_number");
				if (SmallCC.nostack)
				{
					code += INS("Move result to " + REG(this), null, "LCA", "CAST", REG(this));
				}
			} 
			else if ((!(getType() instanceof PointerType) && getType().equals(Type.charType)) && child.getType() instanceof PointerType) 
			{
				code += COM("Cast Pointer(" + child.toString() + ") to Char");
				if (SmallCC.nostack)
				{
					code += INS("Move child val to CAST reg", null, "MCW", REG(child), "CAST");
				}
				code += SNIP("pointer_to_char");
				if (SmallCC.nostack)
				{
					code += INS("Move result to " + REG(this), null, "LCA", "CAST", REG(this));
				}
			}
			else if (getType() instanceof PointerType && (!(child.getType() instanceof PointerType) && child.getType().equals(Type.charType)))
			{
				code += COM("Cast Char(" + child.toString() + ") to Pointer");
				if (SmallCC.nostack)
				{
					code += INS("Move child val to CAST reg", null, "MCW", REG(child), "CAST");
				}
				code += SNIP("char_to_pointer");
				if (SmallCC.nostack)
				{
					code += INS("Move result to " + REG(this), null, "LCA", "CAST", REG(this));
				}
			}
			else if ((!(getType() instanceof PointerType) && getType().equals(Type.intType)) && child.getType().equals(Type.charType))
			{
				code += COM("Cast Char(" + child.toString() + ") to Number");
				if (SmallCC.nostack)
				{
					code += INS("Move child val to CAST reg", null, "MCW", REG(child), "CAST");
				}
				code += SNIP("char_to_number");
				if (SmallCC.nostack)
				{
					code += INS("Move result to " + REG(this), null, "LCA", "CAST", REG(this));
				}
			} 
			else
			{
				// Otherwise we don't need to do anything and our value is already on the stack
			}
		}
		return code;
	}

	@Override
	public Expression getLeftExpression() {
		return child;
	}

	@Override
	public Expression getRightExpression() {
		return null;
	}

	@Override
	public String toString() 
	{
		return "((" + getType() + ") "  + child + ")";	
	}	
}
