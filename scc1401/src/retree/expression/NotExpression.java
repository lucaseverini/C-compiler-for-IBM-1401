package retree.expression;
import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.type.*;
import compiler.SmallCC;

public class NotExpression extends Expression {
    private Expression l;

    public NotExpression(Expression l) throws TypeMismatchException {
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
            if (l2 instanceof ConstantExpression) {
                return new ConstantExpression(l2.getType(), ((ConstantExpression)l2).getValue() ? 0 : 1);
            }
            return new NotExpression(l2);
        } catch (TypeMismatchException e) {
            //should never happen
            return null;
        }
    }

    public String generateCode(boolean valueNeeded) {
        String labelEnd = label(SmallCC.nextLabelNumber());

        String code = PUSH(5,NUM_CONST(0));
        code += l.generateCode(true);
        code += INS("MCS",STACK_OFF(0),STACK_OFF(0));
        code += POP(l.getType().sizeof());
        code += INS("BCE",labelEnd, STACK_OFF(l.getType().sizeof())," ");
        code += INS("MCW", NUM_CONST(0), STACK_OFF(0));
        code += LBL_INS(labelEnd, "NOP");
        return code;
    }

}
