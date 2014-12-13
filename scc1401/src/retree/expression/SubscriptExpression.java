package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class SubscriptExpression extends Expression {
    private Expression l, r;

    public SubscriptExpression(Expression l, Expression r) throws TypeMismatchException {
        super(l.getType());
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

    public Expression collapse() {
        try {
            Expression l2 = l.collapse();
            Expression r2 = r.collapse();
            if ((r2 instanceof ConstantExpression))
            {
                if ((l2 instanceof VariableExpression ))
                {
                    ConstantExpression c = (ConstantExpression)r2;
                    VariableExpression var = (VariableExpression)l2;
                    return new VariableExpression(l2.getType().getType(), var.getOffset() + c.getValue(), var.isStatic());
                } else if ((l2 instanceof ArrayNameExpression))
                {
                    ConstantExpression c = (ConstantExpression)r2;
                    ArrayNameExpression arr = (ArrayNameExpression)l2;
                    int offset = arr.getArray().getOffset();
                    ArrayType arrType = arr.getArray().getType();
                    offset = offset + arrType.getArrayBaseType().sizeof() - arrType.sizeof();
                    return new VariableExpression(l2.getType().getType(), offset + c.getValue(), arr.getArray().isStatic());
                }

            }
            return new SubscriptExpression(l2, r2);
        } catch (TypeMismatchException e) {
            //should never happen
            return null;
        }
    }

    public String generateCode(boolean valueNeeded) {
        String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
        if (valueNeeded) {
            code += SNIP("number_to_pointer");
            code += INS("MA", STACK_OFF(0), STACK_OFF(-3));
            code += POP(3);
            code += POP(3,"X1");
            code += PUSH(((PointerType)l.getType()).getType().sizeof(), "0+X1");
        }
        return code;
    }

}
