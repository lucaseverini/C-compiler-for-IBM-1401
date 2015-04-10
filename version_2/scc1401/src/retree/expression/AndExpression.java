/*
	AndExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class AndExpression extends Expression 
{
    private Expression l, r;

    public AndExpression(Expression l, Expression r) throws TypeMismatchException 
	{
        super(Type.intType);
		
        if (! l.getType().equals(r.getType())) 
		{
            throw new TypeMismatchException(r, l.getType(), r.getType());
        }
		
        this.l = l;
        this.r = r;
    }

    public Expression collapse()
	{
        try 
		{
            Expression l2 = l.collapse();
            Expression r2 = r.collapse();
			
            if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression)
			{
                return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() != 0 && ((ConstantExpression)r2).getValue() != 0 ? 1 : 0);
            }
			
            return new AndExpression(l2, r2);
        } 
		catch (TypeMismatchException e) 
		{
            // should never happen
            return null;
        }
    }

    public String generateCode(boolean valueNeeded) 
	{
        String labelZero = label(SmallCC.nextLabelNumber());
        String labelEnd = label(SmallCC.nextLabelNumber());

        String code = COM("And " + this.toString()); 
		code += PUSH(5, NUM_CONST(1, false));
        code += l.generateCode(true);
        code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0),STACK_OFF(0));
        code += POP(l.getType().sizeof());
        code += INS("Jump to Zero if equal", null, "BCE", labelZero, STACK_OFF(l.getType().sizeof())," ");
        code += r.generateCode(true);
        code += INS("Clear WM", null, "MCS", STACK_OFF(0),STACK_OFF(0));
        code += POP(r.getType().sizeof());
        code += INS("Jump to Zero if equal", null, "BCE", labelZero, STACK_OFF(r.getType().sizeof())," ");
        code += INS("Jump to End", null, "B", labelEnd);
        code += INS("Move 0 in stack", labelZero, "MCW", NUM_CONST(0, false), STACK_OFF(0));
        code += INS("End of And", labelEnd, "NOP");

        return code;
    }

    public String toString() 
	{
        return "(" + l +" && "+ r + ")";
    }
}
