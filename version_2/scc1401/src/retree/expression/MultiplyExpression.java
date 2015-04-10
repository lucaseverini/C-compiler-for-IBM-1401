/*
	MultiplyExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class MultiplyExpression extends Expression
{
	private Expression l, r;

	public MultiplyExpression(Expression l, Expression r) throws Exception 
	{
		super(strongestType(l.getType(), r.getType()));
		
		if (getType() instanceof PointerType)
		{
			throw new Exception("Invalid types for operator *: " + l.getType() + " * " + r.getType());
		}
				
		if (!l.getType().equals(getType())) 
		{
			l = new CastExpression(getType(), l);
		} 
		else if (!r.getType().equals(getType())) 
		{
			l = new CastExpression(getType(), r);
		}
		
		this.l = l;
		this.r = r;
	}

	private static Type strongestType(Type a, Type b)
	{
		if (a instanceof PointerType) 
		{
			return a;
		}
		
		if (b instanceof PointerType) 
		{
			return b;
		}
		
		if (a.equals(Type.intType) || b.equals(Type.intType)) 
		{
			return Type.intType;
		}
		
		return a;
	}

	public Expression collapse()
	{
		try 
		{
			Expression l2 = l.collapse();
			Expression r2 = r.collapse();
			
			if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression)
			{
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() * ((ConstantExpression)r2).getValue());
			}
			
			return new MultiplyExpression(l2, r2);
		} 
		catch (Exception e)
		{
			//should never happen
			return null;
		}
	}

	public String generateCode(boolean valueNeeded) 
	{
		int size = r.getType().sizeof();
		
		if(l instanceof ConstantExpression && r instanceof ConstantExpression)
		{
			String code = COM("Constant " + this.toString()); 
			int value = ((ConstantExpression)l).getValue() * ((ConstantExpression)r).getValue();
			code += PUSH(size, NUM_CONST(value, false));
			return code;
		}
		
		String code = COM("Multiply " + this.toString());
		code += l.generateCode(valueNeeded) + r.generateCode(valueNeeded);	
		
		if (valueNeeded) 
		{
			code += INS("Multiply stack at " + -size + " to stack at " + (size + 1), null, "M", STACK_OFF(-size), STACK_OFF(size + 1));
			
			// this puts the product at size + 1 bits above the stack
			// ###############
			// ## WARNING!! ##
			// ###############
			// DO WE NEED THIS SW ??
			code += INS("Set WM in stack at 2", null, "SW", STACK_OFF(2));
			
			code += INS("Load stack at " + (size + 1) + " to stack at " + -size, null, "LCA", STACK_OFF(size + 1), STACK_OFF(-size));
			
			code += POP(size);
		}
		
		return code;
	}

	public String toString()
	{
		return "(" + l + " * " + r + ")";
	}
}
