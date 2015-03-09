/*
	NotExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class NotExpression extends Expression 
{
    private final Expression l;

    public NotExpression(Expression l) throws TypeMismatchException
	{
        super(Type.intType);
        this.l = l;
    }

    public Expression collapse() 
	{
        try 
		{
            Expression l2 = l.collapse();
            if (l2 instanceof ConstantExpression) 
			{
                return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() == 0 ? 1 : 0);
            }
			
            return new NotExpression(l2);
        } 
		catch (TypeMismatchException e)
		{
            // should never happen
            return null;
        }
    }

    public String generateCode(boolean valueNeeded)
	{
        String labelEnd = label(SmallCC.nextLabelNumber());

        String code = COM("NotExpression " + this.toString()) + PUSH(5,NUM_CONST(0, false));
        code += l.generateCode(true);
        code += INS("MCS",STACK_OFF(0),STACK_OFF(0));
        code += POP(l.getType().sizeof());
        code += INS("BCE",labelEnd, STACK_OFF(l.getType().sizeof())," ");
        code += INS("MCW", NUM_CONST(0, false), STACK_OFF(0));
        code += LBL_INS(labelEnd, "NOP");
		
        return code;
    }

    public String toString()
    {
        return "(" + "!" + l + ")";
    }
}
