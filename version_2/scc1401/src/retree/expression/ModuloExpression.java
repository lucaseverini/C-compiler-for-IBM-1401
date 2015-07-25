/*
	ModuloExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class ModuloExpression extends Expression 
{
	private Expression l, r;
	
	public ModuloExpression(Expression l, Expression r) throws TypeMismatchException 
	{
		super(l.getType());
		
		if (! l.getType().equals(r.getType())) 
		{
			throw new TypeMismatchException(r, l.getType(), r.getType());
		}
		
		this.l = l;
		this.r = r;
	}
	
	@Override
	public Expression collapse() 
	{
		try 
		{
			Expression l2 = l.collapse();
			Expression r2 = r.collapse();
			
			if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression) 
			{
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() % ((ConstantExpression)r2).getValue());
			}
			
			return new ModuloExpression(l2, r2);
		} 
		catch (TypeMismatchException e) 
		{
			//should never happen
			return null;
		}
	}
	
	@Override
	public String generateCode(boolean valueNeeded)
	{
		String code = COM("Modulo (%) " + this.toString());
		code += r.generateCode(valueNeeded) + l.generateCode(valueNeeded);
		
		if (valueNeeded) 
		{
			code += SNIP("SNIP_DIV");
			code += POP(Type.intType.sizeof());
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
        return "(" + l + " % " + r + ")";
    }
}
