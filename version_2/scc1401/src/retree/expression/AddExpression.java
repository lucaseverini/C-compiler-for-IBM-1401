/*
	AddExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;

import compiler.SmallCC;
import retree.type.*;

public class AddExpression extends Expression
{
	private final Expression l, r;
	
	public AddExpression(Expression l, Expression r) 
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
		
		if (! l.getType().equals(getType()))
		{
			l = new CastExpression(getType(), l);
		} 
		else if(!r.getType().equals(getType())) 
		{
			r = new CastExpression(getType(), r);
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
		Expression l2 = l.collapse();
		Expression r2 = r.collapse();
		
		if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression) 
		{
			return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() + ((ConstantExpression)r2).getValue());
		}
		
		return new AddExpression(l2, r2);
	}
	
	@Override
	public String generateCode(boolean valueNeeded) 
	{
		String code = COM("Addition " + this.toString());
		
		code += l.generateCode(valueNeeded); 
		code += r.generateCode(valueNeeded);

		if (SmallCC.nostack)
		{
			if (l.getType() instanceof  PointerType)
			{
				code += INS("Adding left to right", null, "MA", REG(l), REG(r));
			} else {
				code += INS("Add left to right", null, "A", REG(l), REG(r));
				code += INS("Move result to " + REG(this), null, "LCA", REG(r), REG(this));
			}
		} else {

			if (valueNeeded)
			{
				int size = -r.getType().sizeof();

				if (l.getType() instanceof PointerType)
				{
					code += INS("Add stack to stack at " + size, null, "MA", STACK_OFF(0), STACK_OFF(size));
				}
				else
				{
					code += INS("Add stack to stack at " + size, null, "A", STACK_OFF(0), STACK_OFF(size));
				}

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
		return "(" + l + " + " + r + ")";
	}
}
