/*
	LessThanExpression.java

    Small-C compiler - SJSU
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
        if (! l.getType().equals(r.getType())) {
            throw new TypeMismatchException(r, l.getType(), r.getType());
        }*/
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

    public String generateCode(boolean valueNeeded) 
	{
		//l = B, r = A
		if (valueNeeded) 
		{
			String code = COM("LessThanExpression " + this.toString()) +
			l.generateCode(valueNeeded) + SNIP("clean_number") + r.generateCode(valueNeeded) + SNIP("clean_number");
			
			String labelLessThan = label(SmallCC.nextLabelNumber());
			String labelEnd = label(SmallCC.nextLabelNumber());
			
			code += INS("C", STACK_OFF(0), STACK_OFF(-5));
			code += POP(5);
			code += INS("MCW", NUM_CONST(0, false), STACK_OFF(0));
			code += INS("BL", labelLessThan);
			code += INS("B", labelEnd);
			code += LBL_INS(labelLessThan, "MCW", NUM_CONST(1, false), STACK_OFF(0));
			code += LBL_INS(labelEnd, "NOP");
			
			return code;
		}
		else
		{
			return l.generateCode(false) + r.generateCode(false);
		}
	} /*
        String labelEqual = label(SmallCC.nextLabelNumber());
        String labelEnd = label(SmallCC.nextLabelNumber());
        String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
        if (valueNeeded) {
            code += INS("CMPB", STACK_OFF(0), STACK_OFF(-r.getType().sizeof()));
            code += POP(1);
            code += INS("BCE", labelEqual, STACK_OFF(1), "1");
            code += PUSH(Type.intType.sizeof(), NUM_CONST(0));
            code += INS("B", labelEnd);
            code += LBL_INS(labelEqual, "NOP");
            code += PUSH(Type.intType.sizeof(), NUM_CONST(1));
            code += LBL_INS(labelEnd, "NOP");

        }
        return code;
    }*/

    public String toString()
    {
        return "(" + l + " < " + r + ")";
    }
}
