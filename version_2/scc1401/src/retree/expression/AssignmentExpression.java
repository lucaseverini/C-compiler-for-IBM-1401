/*
	AssignmentExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import compiler.SmallCC;

import static retree.RetreeUtils.*;

public class AssignmentExpression extends Expression 
{
	private LValue l;
	private Expression r;

	public AssignmentExpression(LValue l, Expression r) throws Exception 
	{
		super(l.getType());
		associativity = false;
		if (! l.getType().equals(r.getType())) 
		{
			throw new retree.exceptions.TypeMismatchException(r, l.getType(), r.getType());
		}
		
		this.l = l;
		this.r = r;
	}

	@Override
	public Expression collapse() 
	{
		try 
		{
			return new AssignmentExpression(l.collapse(), r.collapse());
		} 
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public String generateCode(boolean valueNeeded) 
	{
		// no sequence point, so we can evaluate the right side first
		String rightCode = r.generateCode(true);
		String leftCode = l.generateAddress();
/*		
		Type lType = l.getType();
		if(lType.isPointerType())
		{
			System.out.println("Left " + l + " is pointer to " + ((PointerType)lType).getRefType());
		}

		Type rType = r.getType();
		if(rType.isPointerType())
		{
			System.out.println("Right " + r + " is pointer to " + ((PointerType)rType).getRefType());
		}
*/
		String code = COM("Assignment " + this.toString());
		code += rightCode;
		code += leftCode;
		if (SmallCC.nostack)
		{
			code += INS("Move " + REG(l) + " to X1", null, "MCW", REG(l), "X1");
			code += INS("Move right result to left addr", null, "LCA", REG(r), "0+X1");
			if (valueNeeded) {
				// TODO figure out what to do here
//				code += INS("Move right result to left addr", null, "MCW", REG(r), l.generateAddress());
			}

		} else {
			code += POP(3, "X1");

			if (valueNeeded) {
				code += INS("Load stack in memory X1", null, "LCA", STACK_OFF(0), "0+X1");
			} else {
				code += POP(r.getType().sizeof(), "0+X1");
			}

			code += COM("End Assignment " + this.toString());
		}
		return code+"\n";
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
		return "(" + l + " = " + r + ")";
	}
}
