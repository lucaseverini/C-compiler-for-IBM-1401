/*
	TypeMismatchException.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.expression.LValue;
import retree.expression.Expression;
import retree.type.PointerType;

public class AddressOfExpression extends Expression
{
	private final LValue child;

	public AddressOfExpression(LValue child) 
	{
		super(new PointerType(child.getType()));
		this.child = child;
	}

	public String generateCode(boolean valueNeeded) 
	{
		if (valueNeeded) 
		{
			return COM("AddressOf " + this.toString()) + child.generateAddress();
		} 
		else 
		{
			return COM("AddressOf " + this.toString()) + child.generateCode(false);
		}
	}

	public Expression collapse() 
	{
		return new AddressOfExpression(child.collapse());
	}

	public String toString()
	{
		return "( &" + child + " )";
	}
}
