package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class AndExpression extends Expression {
    private Expression l, r;

    public AndExpression(Expression l, Expression r) throws TypeMismatchException {
        super(Type.intType);
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
                return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() == ((ConstantExpression)r2).getValue() ? 0 : 1);
            }
            return new AndExpression(l2, r2);
        } catch (TypeMismatchException e) {
            //should never happen
            return null;
        }
    }

    public String generateCode(boolean valueNeeded) {
        String labelZero = label(SmallCC.nextLabelNumber());
        String labelEnd = label(SmallCC.nextLabelNumber());

        String code = PUSH(5,NUM_CONST(1));
        code += l.generateCode(true);
        code += INS("MCS",STACK_OFF(0),STACK_OFF(0));
        code += POP(l.getType().sizeof());
        code += INS("BCE",labelZero, STACK_OFF(l.getType().sizeof())," ");
        code += r.generateCode(true);
        code += INS("MCS",STACK_OFF(0),STACK_OFF(0));
        code += POP(r.getType().sizeof());
        code += INS("BCE",labelZero, STACK_OFF(r.getType().sizeof())," ");
        code += INS("B",labelEnd);
        code += LBL_INS(labelZero, "MCW", NUM_CONST(0), STACK_OFF(0));
        code += LBL_INS(labelEnd,"NOP");

        return code;
    }

    public String toString() {
        return "(" + l +" && "+ r + ")";
    }

}
