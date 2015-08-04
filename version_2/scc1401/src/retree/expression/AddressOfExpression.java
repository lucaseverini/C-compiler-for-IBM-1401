/*
	TypeMismatchException.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.PointerType;

public class AddressOfExpression extends Expression
{
	private final LValue child;

	public AddressOfExpression(LValue child) 
	{
		super(new PointerType(child.getType()));
		associativity = false;
		this.child = child;
	}

	@Override
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

	@Override
	public Expression collapse() 
	{
		return new AddressOfExpression(child.collapse());
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
		return "( &" + child + " )";
	}
}
