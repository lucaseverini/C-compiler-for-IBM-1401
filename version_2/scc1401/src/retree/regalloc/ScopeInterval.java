package retree.regalloc;

/**
 * Created by mplv on 7/24/15.
 */
public class ScopeInterval {
    private int assignedRegister = -1;
    private int start = -1;
    private int end = -1;

    public ScopeInterval() {}

    public ScopeInterval(int start, int end)
    {
        this.start = start;
        this.end = end;
    }

    public int getStart(){
        return this.start;
    }

    public int getEnd() {
        return end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getAssignedRegister() {
        return assignedRegister;
    }

    public void setAssignedRegister(int assignedRegister) {
        this.assignedRegister = assignedRegister;
    }
}
