/*
	SubtractExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.*;

public class SubtractExpression extends Expression 
{
	private final Expression l, r;
	
	public SubtractExpression(Expression l, Expression r) 
	{
		super(strongestType(l.getType(), r.getType()));
		
		if (getType() instanceof PointerType) 
		{
			PointerType ptype = (PointerType) getType();
			
			try 
			{
				if (! (l.getType() instanceof PointerType)) 
				{
					if(l instanceof ConstantExpression)
					{
						l = new ConstantExpression(Type.intType, ((ConstantExpression)l).getValue() * ptype.getRefType().sizeof());
					}
					else
					{
						l = new MultiplyExpression(l, new ConstantExpression(Type.intType, ptype.getRefType().sizeof()));
					}
				} 
				else if (!(r.getType() instanceof PointerType)) 
				{
					if(r instanceof ConstantExpression)
					{
						r = new ConstantExpression(Type.intType, ((ConstantExpression)r).getValue() * ptype.getRefType().sizeof());
					}
					else
					{
						r = new MultiplyExpression(r, new ConstantExpression(Type.intType, ptype.getRefType().sizeof()));
					}
				}
			} 
			catch (Exception e) 
			{
				//this should never happen, the only exceptions thrown are if we make a multiplyExpression with a pointer type
			}
		}
		
		if (!l.getType().equals(getType())) 
		{
			l = new CastExpression(getType(),l);
		} 
		else if (!r.getType().equals(getType()))
		{
			r = new CastExpression(getType(),r);
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
	
	@Override
	public Expression collapse()
	{
		if (getType() instanceof PointerType) 
		{
			Expression r2 = new CastExpression(r.getType(), new SubtractExpression(new ConstantExpression(Type.intType, 16000), new CastExpression(Type.intType, r)));
			return new AddExpression(l, r2).collapse();
		} 
		else
		{
			Expression l2 = l.collapse();
			Expression r2 = r.collapse();
			
			if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression) 
			{
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() - ((ConstantExpression)r2).getValue());
			}
			
			return new SubtractExpression(l2, r2);
		}
	}
	
	@Override
	public String generateCode(boolean valueNeeded) 
	{
		String code = COM("Subtract " + this.toString());

		if (l.getType() instanceof PointerType) 
		{
			Expression r2 = new CastExpression(r.getType(), new SubtractExpression(new ConstantExpression(Type.intType, 16000), new CastExpression(Type.intType, r)));
			Expression exp = new AddExpression(l, r2).collapse();
			
			code += exp.generateCode(valueNeeded);
		}
		else
		{
			code += l.generateCode(valueNeeded);
			code += r.generateCode(valueNeeded);

			if (valueNeeded) 
			{
				int size = -r.getType().sizeof();
				code += INS("Subtract stack to stack at " + size, null, "S", STACK_OFF(0), STACK_OFF(size));		
				code += POP(r.getType().sizeof());
			}
		}
		
		return code;
	}

	@Override
	public Expression getLeftExpression() {
		return l;
	}

	@Override
	public Expression getRightExpression() {
		return r;
	}
	
	@Override
	public String toString() 
	{
		return "(" + l + " - " + r + ")";
	}
}
