/*
	LessThanExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.type.*;
import compiler.SmallCC;

public class LessThanExpression extends Expression 
{
    private final Expression l, r;

    public LessThanExpression(Expression l, Expression r) 
	{
        super(Type.intType);
/*
        if (! l.getType().equals(r.getType())) 
		{
            throw new TypeMismatchException(r, l.getType(), r.getType());
        }
*/
        if (! l.getType().equals(Type.intType)) 
		{
			l = new CastExpression(Type.intType, l);
		}

		if (! r.getType().equals(Type.intType)) 
		{
			r = new CastExpression(Type.intType, r);
		}
		
        this.l = l;
        this.r = r;
    }

	@Override
    public Expression collapse() 
	{
		Expression l2 = l.collapse();
		Expression r2 = r.collapse();
			
		if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression) 
		{
			return new ConstantExpression(Type.intType, ((ConstantExpression)l2).getValue() < ((ConstantExpression)r2).getValue() ? 1 : 0);
		}
		
		return new LessThanExpression(l2, r2);
    }

	@Override
    public String generateCode(boolean valueNeeded) 
	{
		//l = B, r = A
		if (valueNeeded) 
		{
			String code = COM("Less " + this.toString()) +
								l.generateCode(valueNeeded) + SNIP("clean_number") + 
								r.generateCode(valueNeeded) + SNIP("clean_number");
			
			String labelLessThan = label(SmallCC.nextLabelNumber());
			String labelEnd = label(SmallCC.nextLabelNumber());
						
			// ###############
			// ## WARNING!! ##
			// ###############
			// IS CORRECT HERE TO USE A FIXED VALUE (5) FOR THE VARIABLE SIZE
			code += INS("Compare stack to stack at -5", null, "C", STACK_OFF(0), STACK_OFF(-5));
			code += POP(5);
			
			code += INS("Move 0 in stack", null, "MCW", NUM_CONST(0, false), STACK_OFF(0));
			code += INS("Jump if less", null, "BL", labelLessThan);
			code += INS("Jump to End", null, "B", labelEnd);
			code += INS("Move 1 in stack", labelLessThan, "MCW", NUM_CONST(1, false), STACK_OFF(0));
			code += INS("End of Less", labelEnd, "NOP");
			
			return code;
		}
		else
		{
			return l.generateCode(false) + r.generateCode(false);
		}
	} 

	@Override
    public String toString()
    {
        return "(" + l + " < " + r + ")";
    }
}
