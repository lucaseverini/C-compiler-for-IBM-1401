/*
	AssignmentExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.expression.LValue;
import retree.expression.Expression;

public class AssignmentExpression extends Expression 
{
	private LValue l;
	private Expression r;

	public AssignmentExpression(LValue l, Expression r) throws retree.exceptions.TypeMismatchException 
	{
		super(l.getType());
		
		if (! l.getType().equals(r.getType())) 
		{
			throw new retree.exceptions.TypeMismatchException(r, l.getType(), r.getType());
		}
		
		this.l = l;
		this.r = r;
	}

	public Expression collapse() 
	{
		try 
		{
			return new AssignmentExpression(l.collapse(), r.collapse());
		} 
		catch (retree.exceptions.TypeMismatchException e)
		{
			return null;
		}
	}

	public String generateCode(boolean valueNeeded) 
	{
		//no sequence point, so we can evaluate the right side first
		String code = COM("Assignment " + this.toString()) + 
			r.generateCode(true) +
			l.generateAddress() +
			POP(3, "X1");
		
		if (valueNeeded) 
		{
			code += INS(null, null, "LCA", STACK_OFF(0), "0+X1");
		}
		else 
		{
			code += POP(l.getType().sizeof(), "0+X1");
		}
		
		return code;
	}

	public String toString()
	{
		return "(" + l + " = " + r + ")";
	}
}
