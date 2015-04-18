/*
	TypeMismatchException.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.exceptions;

import retree.expression.Expression;
import retree.type.Type;

public class TypeMismatchException extends Exception
{
	private static final long serialVersionUID = 1L;
	private final Expression exp;
	private final Type expected;
	private final Type found;

	public TypeMismatchException(Expression exp, Type expected, Type found) 
	{
		this.exp = exp;
		this.expected = expected;
		this.found = found;
	}
	
	@Override
	public String toString() 
	{
		return  "Type mismatch.  Expected: " + expected.toString() + ", found: " + found.toString();
	}
}
