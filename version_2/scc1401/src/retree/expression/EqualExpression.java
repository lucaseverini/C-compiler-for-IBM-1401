/*
	EqualExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class EqualExpression extends Expression 
{
	private Expression l, r;

	public EqualExpression(Expression l, Expression r) throws TypeMismatchException 
	{
		super(Type.intType);
		
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
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() == ((ConstantExpression)r2).getValue() ? 1 : 0);
			}
			
			return new EqualExpression(l2, r2);
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
		String labelEqual = label(SmallCC.nextLabelNumber());
		String labelEnd = label(SmallCC.nextLabelNumber());	
		
		String code = COM("Equal " + this.toString());
		code += l.generateCode(valueNeeded);
		
		// WARNING!!
		// ##################################################
		// ##
		// ##  DO WE NEED TWO CALLS TO SNIPPET CLEAN NUMBER ?
		// ##
		// ###################################################
		if (valueNeeded && l.getType().equals(Type.intType))
		{
			code += SNIP("clean_number");
		}
		
		code += r.generateCode(valueNeeded);
		
		if (valueNeeded && r.getType().equals(Type.intType))
		{
			code += SNIP("clean_number");
		}
		
		if (valueNeeded)
		{
			int size = l.getType().sizeof();

			code += INS("Compare stack to stack at " + -size, null, "C", STACK_OFF(0), STACK_OFF(-size));
			code += POP(size) + POP(size);
			code += PUSH(Type.intType.sizeof(), NUM_CONST(0, false));

			code += INS("Jump if equal", null, "BE", labelEqual);
			code += INS("Jump to End", null, "B", labelEnd);
			code += INS("Move 1 in stack", labelEqual, "MCW", NUM_CONST(1, false), STACK_OFF(0));
			code += INS("End of Equal", labelEnd, "NOP");
		}
		
		return code;
	}

	@Override
	public String toString() 
	{
		return "(" + l +" == "+ r + ")";
	}
}
