package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class SubscriptExpression extends LValue {
    private Expression l, r;

    public SubscriptExpression(Expression l, Expression r) throws TypeMismatchException {
        super(((PointerType)l.getType()).getType());
        if (!r.getType().equals(Type.intType))
        {
            r = new CastExpression(Type.intType, r);
        }
        if (!(l.getType() instanceof PointerType && r.getType().equals(Type.intType) )) {
            throw new TypeMismatchException(r, l.getType(), r.getType());
        }
        this.l = l;
        this.r = r;
    }

    public LValue collapse() {
			/* Broken for now.
        try {
            Expression l2 = l.collapse();
            Expression r2 = r.collapse();
            if ((r2 instanceof ConstantExpression))
            {
                if ((l2 instanceof VariableExpression ))
                {
                    ConstantExpression c = (ConstantExpression)r2;
                    VariableExpression var = (VariableExpression)l2;
                    return new VariableExpression(((PointerType)l2.getType()).getType(), var.getOffset() + c.getValue(), var.isStatic());
                } else if ((l2 instanceof ArrayNameExpression))
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
        } catch (TypeMismatchException e) {
            //should never happen
            return null;
        }
        */
        return this;
    }

    public String generateCode(boolean valueNeeded) {
        /*String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
        if (valueNeeded) {
            PointerType ptype = (PointerType)l.getType();
            PUSH(5, NUM_CONST(ptype.getType().sizeof()));
            code += INS("M", STACK_OFF(-Type.intType.sizeof()), STACK_OFF(1+Type.intType.sizeof()));
            //this puts the product at size + 1 bits above the stack
            code += INS("SW", STACK_OFF(2));
            code += INS("MCW", STACK_OFF(1+Type.intType.sizeof()), STACK_OFF(-Type.intType.sizeof()));
            code += INS("CW", STACK_OFF(2));
            code += POP(Type.intType.sizeof());
            //at this point, the top of the stack should be r*sizeof(l)
            code += SNIP("number_to_pointer");
            code += INS("MA", STACK_OFF(0), STACK_OFF(-3));
            code += POP(3);
            code += POP(3,"X1");
            code += PUSH(ptype.getType().sizeof(), "0+X1");
        }
        return code;
        */
        String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
        if (valueNeeded) {
            PointerType ptype = (PointerType)l.getType();
            code += generateAddress();
            code += POP(3,"X1");
            code += PUSH(ptype.getType().sizeof(), "0+X1");

        }
        return code;
    }

    public String generateAddress() {
        String code = l.generateCode(true) + r.generateCode(true);
        PointerType ptype = (PointerType)l.getType();
        code += COM("raw index on the stack");
        code += PUSH(5, NUM_CONST(ptype.getType().sizeof()));
        code += INS("M", STACK_OFF(-Type.intType.sizeof()), STACK_OFF(1+Type.intType.sizeof()));
        //this puts the product at size + 1 bits above the stack
        code += INS("SW", STACK_OFF(2));
        code += INS("MCW", STACK_OFF(1+Type.intType.sizeof()), STACK_OFF(-Type.intType.sizeof()));
        code += INS("CW", STACK_OFF(2));
        code += POP(Type.intType.sizeof());
        //at this point, the top of the stack should be r*sizeof(l)
        code += COM("STACK TOP IS NOW ARRAY INDEX");
        code += SNIP("number_to_pointer");
        code += INS("MA", STACK_OFF(0), STACK_OFF(-3));
        code += POP(3);
        code += COM("STACK top is location in array now.");
        return code;
    }

}
