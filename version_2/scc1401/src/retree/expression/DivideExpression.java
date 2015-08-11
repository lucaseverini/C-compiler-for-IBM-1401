/*
	DivideExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;

import compiler.SmallCC;
import retree.exceptions.*;
import retree.type.*;

public class DivideExpression extends Expression
{
	private Expression l, r;
	
	public DivideExpression(Expression l, Expression r) throws TypeMismatchException
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
		try {
			Expression l2 = l.collapse();
			Expression r2 = r.collapse();
			
			if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression)
			{
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() / ((ConstantExpression)r2).getValue());
			}
			
			return new DivideExpression(l2, r2);
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
		String code = COM("Divide " + this.toString()) +
		l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
		
		if (valueNeeded) 
		{
			if (SmallCC.nostack)
			{
				code += COM("Move operands to MDREGS");
				code += INS("", null, "LCA", REG(r), "MDREGA");
				code += INS("Move addr of MDREGB to X1", null, "MCW", ADDR_CONST(442, false), "X1");
				code += INS("", null, "ZA", REG(l), "MDREGB");
				code += INS("Divide MDREGA to MDREGB", null, "D", "MDREGA", "15996+X1");
				code += INS("Move result to "+REG(this), null, "MN", "15994+X1", REG(this));
				code += INS("", null, "MN");
				code += INS("", null, "MN");
				code += INS("", null, "MN");
				code += INS("", null, "MN");
			} else {
				code += SNIP("SNIP_DIV");
				int size = -Type.intType.sizeof();
				code += INS("Move stack in stack at " + size, null, "MCW", STACK_OFF(0), STACK_OFF(size));
				code += POP(Type.intType.sizeof());
			}
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
		return "(" + l +" / "+ r + ")";
	}
}
