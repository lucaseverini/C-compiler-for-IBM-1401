/*
	DereferenceExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import compiler.SmallCC;
import retree.exceptions.TypeMismatchException;
import static retree.RetreeUtils.*;
import retree.type.PointerType;
import retree.type.Type;

public class DereferenceExpression extends LValue 
{
	private final Expression child;

	public DereferenceExpression(Expression child) throws TypeMismatchException 
	{
		super(getReferenceType(child));
		associativity = false;
		this.child = child;
	}

	private static Type getReferenceType(Expression exp) throws retree.exceptions.TypeMismatchException
	{
		if (!(exp.getType() instanceof PointerType))
		{
			throw new retree.exceptions.TypeMismatchException(exp, new PointerType(exp.getType()), exp.getType());
		}
		
		return ((PointerType) exp.getType()).getRefType();
	}

	@Override
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

	@Override
	public String generateCode(boolean valueNeeded) 
	{
		try 
		{
			if (!valueNeeded) 
			{
				String code = COM("DereferenceExpression " + this.toString());
				code += child.generateCode(false);
				if (SmallCC.nostack)
				{
					code += INS("Move result to " +REG(this), null, "MCW", REG(child), REG(this));
				}
				return code;
			} 
			else 
			{
				String code = COM("DereferenceExpression " + this.toString());
				code += child.generateCode(true);
				if (SmallCC.nostack)
				{
					code += INS("Copy address to X1", null, "MCW", REG(child), "X1");
					code += INS("Load val to " + REG(this), null, "LCA", "0+X1", REG(this));
				} else {
					code += POP(3, "X1");

					int childSize = getReferenceType(child).sizeof();
/*
				// Assure that WM is in the right position
				switch(childSize)
				{
					case 1:
						code += INS(null, null, "SW", "1+X2");
						break;
					
					case 3:
						code += INS(null, null, "SW", "1+X2");
						code += INS(null, null, "CW", "2+X2");
						code += INS(null, null, "CW", "3+X2");
						break;
						
					case 5:
						code += INS(null, null, "SW", "1+X2");
						code += INS(null, null, "CW", "2+X2");
						code += INS(null, null, "CW", "3+X2");
						code += INS(null, null, "CW", "4+X2");
						code += INS(null, null, "CW", "5+X2");
						break;
				}
*/
					code += PUSH(childSize, "0+X1"); // Do something better than this...
/*
				switch(childSize)
				{
					case 1:
						code += INS("Set WM", null, "SW", "0+X2");
						break;
						
					case 3:
						//code += INS("Set WM", null, "SW", "15998+X2");
						break;
						
					case 5:
						//code += INS("Set WM", null, "SW", "15998+X2");
						break;
				}
*/
				}
				code += COM("End DereferenceExpression " + this.toString());
				code += "\n";
				
				return code;
			}
		} 
		catch(TypeMismatchException e)
		{
			return null;
		}
	}

	@Override
	public void setOffset(int offset)
	{

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
	public String generateAddress()
	{
		return child.generateCode(true);
	}

	@Override
	public String toString() 
	{
		return "(" + "*" + child +")";
	}
}
