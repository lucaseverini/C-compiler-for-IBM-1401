/*
	NotExpression.java

    The Small-C cross-compiler for IBM 1401

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
		associativity = false;
        this.l = l;
    }

	@Override
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

	@Override
    public String generateCode(boolean valueNeeded)
	{
        String labelEnd = label(SmallCC.nextLabelNumber());

        String code = COM("Not (!) " + this.toString()) + PUSH(5,NUM_CONST(0, false));
        code += l.generateCode(true);
		
		int size = l.getType().sizeof();
        code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0));
        code += POP(size);
        code += INS("Jump to End if equal to stack at " + size, null, "BCE", labelEnd, STACK_OFF(size), " ");
        code += INS("Move 0 in stack", null, "MCW", NUM_CONST(0, false), STACK_OFF(0));
        code += INS("End of Not", labelEnd, "NOP");
		
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
        return "(" + "!" + l + ")";
    }
}
