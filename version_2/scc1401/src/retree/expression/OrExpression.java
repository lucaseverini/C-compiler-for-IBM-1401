/*
	OrExpression.java

    The Small-C cross-compiler for IBM 1401

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

	@Override
    public Expression collapse() 
	{
        try 
		{
            Expression l2 = l.collapse();
            Expression r2 = r.collapse();
			
            if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression) 
			{
				return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() != 0 || ((ConstantExpression)r2).getValue() != 0 ? 1 : 0);
            }
			
            return new OrExpression(l2, r2);
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
        String labelSecond = label(SmallCC.nextLabelNumber());
        String labelEnd = label(SmallCC.nextLabelNumber());

        String code = COM("Or (||) " + this.toString());
		code += PUSH(5, NUM_CONST(0, false));
        code += l.generateCode(true);
		int lSize = l.getType().sizeof();
		int rSize = r.getType().sizeof();
        code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0));
        code += POP(lSize);
        code += INS("Jump to Second if equal to stack at " + lSize, null, "BCE", labelSecond, STACK_OFF(lSize)," ");
        code += INS("Move 1 in stack", null, "MCW", NUM_CONST(1, false), STACK_OFF(0));
        code += INS("Jump to End", null, "B", labelEnd);
        code += INS("Second", labelSecond, "NOP");
        code += r.generateCode(true);
        code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0),STACK_OFF(0));
        code += POP(rSize);
        code += INS("Jump to End if equal to stack at " + rSize, null, "BCE", labelEnd, STACK_OFF(rSize)," ");
        code += INS("Move 1 in stack", null, "MCW", NUM_CONST(1, false), STACK_OFF(0));
        code += INS("End of Or", labelEnd, "NOP");

        return code;
    }

	@Override
    public String toString()
    {
        return "(" + l + " || " + r + ")";
    }
}
