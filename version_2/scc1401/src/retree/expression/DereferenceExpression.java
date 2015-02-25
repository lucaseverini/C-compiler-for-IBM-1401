package retree.expression;

import retree.exceptions.TypeMismatchException;
import static retree.RetreeUtils.*;
import retree.expression.Expression;
import retree.expression.LValue;
import retree.type.PointerType;
import retree.type.Type;
import retree.exceptions.*;

public class DereferenceExpression extends LValue 
{
	private Expression child;

	public DereferenceExpression(Expression child) throws TypeMismatchException 
	{
		super(getReferenceType(child));
		
		this.child = child;
	}

	private static Type getReferenceType(Expression exp) throws retree.exceptions.TypeMismatchException
	{
		if (!(exp.getType() instanceof PointerType))
		{
			throw new retree.exceptions.TypeMismatchException(exp, new PointerType(exp.getType()), exp.getType());
		}
		
		return ((PointerType) exp.getType()).getType();
	}

	public LValue collapse() 
	{
		try {
			return new DereferenceExpression(child.collapse());
		} 
		catch (TypeMismatchException e) 
		{
			//should never happen
			return null;
		}
	}

	public String generateCode(boolean valueNeeded) 
	{
		try 
		{
			if (!valueNeeded) 
			{
				return COM("DereferenceExpression " + this.toString()) + child.generateCode(false);
			} 
			else 
			{
				return COM("DereferenceExpression " + this.toString()) + child.generateCode(true) +
					POP(3,"X1") +
					PUSH(getReferenceType(child).sizeof(), "0+X1");
			}
		} 
		catch(TypeMismatchException e)
		{
			return null;
		}
	}

	public String generateAddress()
	{
		return child.generateCode(true);
	}

	public String toString() 
	{
		return "( " + "*" + child +" )";
	}
}
