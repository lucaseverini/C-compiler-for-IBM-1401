/*
	SubscriptExpression.java

	The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;

import compiler.SmallCC;
import retree.exceptions.*;
import retree.type.*;

public class SubscriptExpression extends LValue
{
	private Expression l, r;

	public SubscriptExpression(Expression l, Expression r) throws TypeMismatchException
	{
		super(((PointerType)l.getType()).getRefType());
			
		if (!r.getType().equals(Type.intType))
		{
			r = new CastExpression(Type.intType, r);
		}
		
		if (!(l.getType() instanceof PointerType && r.getType().equals(Type.intType) ))
		{
			throw new TypeMismatchException(r, l.getType(), r.getType());
		}
		
		this.l = l;
		this.r = r;
	}

	@Override
	public String generateCode(boolean valueNeeded)
	{
		String code = "";
		
		if (valueNeeded) 
		{
			PointerType ptype = (PointerType)l.getType();
			
			code += generateAddress();
			if (SmallCC.nostack)
			{
				code += INS("Load address into X1", null, "MCW", REG(this), "X1");
				code += INS("Get value of subscript", null, "LCA", "0+X1", REG(this));
			} else {
				code += POP(3, "X1");
				code += PUSH(ptype.getRefType().sizeof(), "0+X1");
			}
		}
		
		return code;
	}

	@Override
	public String generateAddress() 
	{
		String code = COM("SubScript " + this.toString());
		
		Boolean leftIsArray = (l instanceof ArrayNameExpression);
		Boolean leftIsPointer = l.getType().isPointerType();
		Boolean rightIsConst = (r instanceof ConstantExpression);
		
		if(leftIsPointer && rightIsConst)
		{
			// If left operand is a pointer and right expression is constant generate some shorter code
			
			if(leftIsArray)
			{
				code += ((ArrayNameExpression)l).generateCodeForConstSubscript(true, ((ConstantExpression)r).getValue());
                if (SmallCC.nostack)
                {
                    code += code += INS("Move result to " + REG(this), null, "LCA", REG(l), REG(this));
                }
			}
			else
			{
				code += ((VariableExpression)l).generateCodeForConstSubscript(true, ((ConstantExpression)r).getValue());
                if (SmallCC.nostack) {
                    code += code += INS("Move result to " + REG(this), null, "LCA", REG(l), REG(this));
                }
			}
		}
		else
		{				
			String left = l.generateCode(true);
			String right = r.generateCode(true);

			code += left + right;
			
			int size = Type.intType.sizeof();

			PointerType ptype = (PointerType)l.getType();

			code += COM("Put raw index on the stack");
            if (SmallCC.nostack) {
                code += INS("Move left result to MDREGA", null, "LCA", REG(l), "MDREGA");
                code += INS("Move addr of MDREGB to X1", null, "MCW", ADDR_CONST(442, false), "X1");
                code += INS("", null, "MCW", REG(r), "15994+X1");
                code += INS("Multiply MDREGA to MDREGB", null, "M", "MDREGA", "MDREGB");
                code += INS("Move result to "+REG(this), null, "MN", "MDREGB", REG(this));
                code += INS("", null, "MN");
                code += INS("", null, "MN");
                code += INS("", null, "MN");
                code += INS("", null, "MN");

                code += SNIP("number_to_pointer");

                code += INS("", null, "MCW", "CSTRES", "X1");
                code += INS("", null, "LCA", "0+CSTRES", REG(this));

            } else {
                code += PUSH(5, NUM_CONST(ptype.getRefType().sizeof(), false));

                code += INS("Multiply stack at " + -size + " to stack at " + (1 + size), null, "M", STACK_OFF(-size), STACK_OFF(1 + size));

                // this puts the product at size + 1 bytes above the stack
                code += INS("Set WM in stack at 2", null, "SW", STACK_OFF(2));
                code += INS("Load stack at " + (1 + size) + " in stack at " + -size, null, "LCA", STACK_OFF(1 + size), STACK_OFF(-size));

                code += POP(Type.intType.sizeof());
                // at this point, the top of the stack should be r * sizeof(l)
                code += COM("Stack top is now array index");

                code += SNIP("number_to_pointer");

                code += INS("Add stack to stack at -3", null, "MA", STACK_OFF(0), STACK_OFF(-3));
                code += POP(3);

                code += COM("Stack top is location in array now");
            }
		}
		
		code += COM("End SubScript " + this.toString());
		
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
		return "(" + l + "[" + r + "]" + ")";
	}

	@Override
	public void setOffset(int offset)
	{

	}

	@Override
	public LValue collapse() 
	{
/* Broken for now.
		try 
		{
			Expression l2 = l.collapse();
			Expression r2 = r.collapse();
		
			if ((r2 instanceof ConstantExpression))
			{
				if ((l2 instanceof VariableExpression ))
				{
					ConstantExpression c = (ConstantExpression)r2;
					VariableExpression var = (VariableExpression)l2;
					return new VariableExpression(((PointerType)l2.getType()).getType(), var.getOffset() + c.getValue(), var.isStatic());
				} 
				else if ((l2 instanceof ArrayNameExpression))
				{
					ConstantExpression c = (ConstantExpression)r2;
					ArrayNameExpression arr = (ArrayNameExpression)l2;
					int offset = arr.getArray().getOffset();
					ArrayType arrType = (ArrayType) arr.getArray().getType();
					offset = offset + arrType.getArrayBaseType().sizeof() - arrType.sizeof();
					return new VariableExpression(((PointerType)l2.getType()).getType(), offset + c.getValue(), arr.getArray().isStatic());
				}
			}
		
			return new SubscriptExpression(l2, r2);
		} 
		catch (TypeMismatchException e) 
		{
			//should never happen
			return null;
		}
*/
		return this;
	}
}
