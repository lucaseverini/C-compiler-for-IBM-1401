/*
	PostIncrementExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;

public class PostIncrementExpression extends Expression
{
	private final LValue l;

	public PostIncrementExpression(LValue l) throws TypeMismatchException 
	{
		super(l.getType());
		
		this.l = l;
	}

	@Override
	public Expression collapse()
	{
		try 
		{
			return new PostIncrementExpression(l.collapse());
		} 
		catch (retree.exceptions.TypeMismatchException e)
		{
			return null;
		}
	}

	@Override
	public String generateCode(boolean valueNeeded) 
	{
		String code = COM("PostIncrement "+ this.toString());
		code += l.generateAddress();
		
		// String code = l.generateCode(valueNeeded);
		code += POP(3, "X1");
		
		if (valueNeeded) 
		{
			code += PUSH(l.getType().sizeof(), "0+X1");
		}
		
		if (getType() instanceof PointerType)
		{
			PointerType pt = (PointerType) getType();
			code += INS("Postincrement pointer at X1", null, "MA", ADDR_CONST(pt.getRefType().sizeof(), false), "0+X1");
		} 
		else 
		{
			code += INS("Postincrement memory at X1", null, "A", NUM_CONST(1, false), "0+X1");
		}
		
		return code;
	}

	@Override
	public Expression getLeftExpression() {
		return l;
	}

	@Override
	public Expression getRightExpression() {
		return null;
	}

	@Override
	public String toString()
	{
		return "(" + l + "++" + ")";
	}
}
