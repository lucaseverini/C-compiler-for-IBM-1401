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
	private Expression exp;
	private Type expected, found;

	public TypeMismatchException(Expression exp, Type expected, Type found) 
	{
		this.exp = exp;
		this.expected = expected;
		this.found = found;
	}
	
	public String toString() 
	{
		return  "Type mismatch.  Expected: " + expected.toString() + ", found: " + found.toString();
	}
}
