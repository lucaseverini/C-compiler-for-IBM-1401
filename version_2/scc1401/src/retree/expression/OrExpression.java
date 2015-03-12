/*
	OrExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class OrExpression extends Expression 
{
    private Expression l, r;

    public OrExpression(Expression l, Expression r) throws TypeMismatchException 
	{
        super(Type.intType);
		
        if (!l.getType().equals(r.getType()))
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
                return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() == ((ConstantExpression)r2).getValue() ? 0 : 1);
            }
			
            return new OrExpression(l2, r2);
        } 
		catch (TypeMismatchException e)
		{
            //should never happen
            return null;
        }
    }

    public String generateCode(boolean valueNeeded)
	{
        String labelSecond = label(SmallCC.nextLabelNumber());
        String labelEnd = label(SmallCC.nextLabelNumber());

        String code = COM("OrExpression " + this.toString());
		code += PUSH(5, NUM_CONST(0, false));
        code += l.generateCode(true);
        code += INS(null, null, "MCS", STACK_OFF(0),STACK_OFF(0));
        code += POP(l.getType().sizeof());
        code += INS("Jump to Second if equal", null, "BCE", labelSecond, STACK_OFF(l.getType().sizeof())," ");
        code += INS(null, null, "MCW", NUM_CONST(1, false),STACK_OFF(0));
        code += INS("Jump to End", null, "B", labelEnd);
        code += INS("Second", labelSecond, "NOP");
        code += r.generateCode(true);
        code += INS(null, null, "MCS", STACK_OFF(0),STACK_OFF(0));
        code += POP(r.getType().sizeof());
        code += INS("Jump to End if equal", null, "BCE", labelEnd, STACK_OFF(r.getType().sizeof())," ");
        code += INS(null, null, "MCW", NUM_CONST(1, false),STACK_OFF(0));
        code += INS("End", labelEnd, "NOP");

        return code;
    }

    public String toString()
    {
        return "(" + l + " || " + r + ")";
    }
}
