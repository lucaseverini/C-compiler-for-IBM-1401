package retree.regalloc;

import java.util.Comparator;

/**
 * Created by mplv on 7/27/15.
 */
public class ScopedIntervalComparator implements Comparator<ScopeInterval> {

    // order is true for increasing ending and false for increasing starting
    boolean order = true;

    public void setOrder(boolean b)
    {
        order = b;
    }

    public boolean getOrder()
    {
        return order;
    }

    @Override
    public int compare(ScopeInterval o1, ScopeInterval o2) {
        // ^ ending
        if (order) {
            if (o1.getEnd() > o2.getEnd()) {
                return 1;
            } else if (o1.getEnd() < o2.getEnd()) {
                return -1;
            }
        } else { // ^ starting
            if (o1.getStart() > o2.getStart()) {
                return 1;
            } else if (o1.getStart() < o2.getStart()) {
                return -1;
            }
        }
        return 0;
    }
}
