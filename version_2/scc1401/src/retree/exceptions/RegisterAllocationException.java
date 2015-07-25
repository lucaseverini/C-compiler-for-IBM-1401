package retree.exceptions;

import retree.expression.Expression;
import retree.regalloc.RegisterAllocator;

/**
 * Created by mplv on 7/24/15.
 */
public class RegisterAllocationException extends Exception {
    private static final long serialVersionUID = 1L;
    private final Expression exp;
    public RegisterAllocationException(Expression ex)
    {
        exp = ex;
    }

    @Override
    public String toString(){
        return "Expression " + exp + " exceeds number of availible registers for computing it's result.\n"+
                "Please break it up into less steps";
    }
}
