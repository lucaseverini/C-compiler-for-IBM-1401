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
            if ((r2 instanceof ConstantExpression) && ((l2 instanceof VariableExpression )))// || (l2 instanceof ArrayNameExpression)))
            {
                ConstantExpression c = (ConstantExpression)r2;
                // return new VariableExpression(l2.getType().getType(), l2.getOffset() + c.getValue(), false);
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
            // code += ;
        }
        return code;
    }

}
