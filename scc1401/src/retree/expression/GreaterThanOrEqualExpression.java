package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class GreaterThanOrEqualExpression extends Expression {
    private Expression l, r;

    public GreaterThanOrEqualExpression(Expression l, Expression r) throws TypeMismatchException {
        super(l.getType());
        if (! l.getType().equals(r.getType())) {
            throw new TypeMismatchException(r, l.getType(), r.getType());
        }
        this.l = l;
        this.r = r;
    }

    public Expression collapse() {
        try {
            Expression l2 = l.collapse();
            Expression r2 = r.collapse();
            if (l2 instanceof ConstantExpression && r2 instanceof ConstantExpression) {
                return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() + ((ConstantExpression)r2).getValue());
            }
            return new GreaterThanOrEqualExpression(l2, r2);
        } catch (TypeMismatchException e) {
            //should never happen
            return null;
        }
    }

    public String generateCode(boolean valueNeeded) {
        String labelEqual = label(SmallCC.nextLabelNumber());
        String labelEnd = label(SmallCC.nextLabelNumber());
        String code = l.generateCode(valueNeeded) + r.generateCode(valueNeeded);
        if (valueNeeded) {
            code += INS("CMPB", STACK_OFF(0), STACK_OFF(-r.getType().sizeof()));
            code += POP(1);
            code += INS("BCE", labelEqual, STACK_OFF(1), "3");
            code += INS("BCE", labelEqual, STACK_OFF(1), "2");
            code += PUSH(Type.intType.sizeof(), NUM_CONST(0));
            code += INS("B", labelEnd);
            code += LBL_INS(labelEqual, "NOP");
            code += PUSH(Type.intType.sizeof(), NUM_CONST(1));
            code += LBL_INS(labelEnd, "NOP");

        }
        return code;
    }

}
