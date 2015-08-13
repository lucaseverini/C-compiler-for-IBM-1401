package retree.regalloc;

import retree.expression.Expression;

import java.util.Comparator;

/**
 * Created by mplv on 8/12/15.
 */
public class ExpressionIntervalComparator implements Comparator<Expression> {

    // order is true for increasing ending and false for increasing starting
    boolean order = false;

    public void setOrder(boolean b)
    {
        order = b;
    }

    public boolean getOrder()
    {
        return order;
    }

    @Override
    public int compare(Expression o1, Expression o2) {
        // ^ ending
        ScopeInterval s1 = o1.scopeInterval;
        ScopeInterval s2 = o2.scopeInterval;
        if (order) {
            if (s1.getEnd() > s2.getEnd()) {
                return 1;
            } else if (s1.getEnd() < s2.getEnd()) {
                return -1;
            }
        } else { // ^ starting
            if (s1.getStart() > s2.getStart()) {
                return 1;
            } else if (s1.getStart() < s2.getStart()) {
                return -1;
            }
        }
        return 0;
    }
}
