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

        int lSize = l.getType().sizeof();
        int rSize = r.getType().sizeof();
        String code = COM("Or (||) " + this.toString());
        if (! SmallCC.nostack)
        {
            code += PUSH(5, NUM_CONST(0, false));
        } else {
            code += INS("Move 0 to " + REG(this), null, "LCA", NUM_CONST(0, false), REG(this));
        }
        code += l.generateCode(true);
        if (SmallCC.nostack)
        {
            code += INS("Clear WM in stack", null, "MCS", REG(l), REG(l));
            code += INS("Set WM in stack", null, "SW", ADDR_LIT(347 + (l.scopeInterval.getAssignedRegister() * Type.intType.getSize())));
            code += INS("Jump to Second if equal to stack at " + lSize, null, "BCE", labelSecond, REG(l), " ");
            code += INS("Move 1 in stack", null, "MCW", NUM_CONST(1, false), REG(this));
            code += INS("Jump to End", null, "B", labelEnd);
            code += INS("Second", labelSecond, "MCW", NUM_CONST(0, false), REG(this));
            code += r.generateCode(true);
            code += INS("Clear WM in stack", null, "MCS", REG(r), REG(r));
            code += INS("Set WM in stack", null, "SW", ADDR_LIT(347 + (r.scopeInterval.getAssignedRegister() * Type.intType.getSize())));
            code += INS("Jump to End if equal to stack at " + rSize, null, "BCE", labelEnd, REG(r), " ");
            code += INS("Move 1 in stack", null, "MCW", NUM_CONST(1, false), REG(this));
            code += INS("End of Or", labelEnd, "NOP");
        } else {

            code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0));
            code += POP(lSize);
            code += INS("Jump to Second if equal to stack at " + lSize, null, "BCE", labelSecond, STACK_OFF(lSize), " ");
            code += INS("Move 1 in stack", null, "MCW", NUM_CONST(1, false), STACK_OFF(0));
            code += INS("Jump to End", null, "B", labelEnd);
            code += INS("Second", labelSecond, "NOP");
            code += r.generateCode(true);
            code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0));
            code += POP(rSize);
            code += INS("Jump to End if equal to stack at " + rSize, null, "BCE", labelEnd, STACK_OFF(rSize), " ");
            code += INS("Move 1 in stack", null, "MCW", NUM_CONST(1, false), STACK_OFF(0));
            code += INS("End of Or", labelEnd, "NOP");
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
        return "(" + l + " || " + r + ")";
    }
}
