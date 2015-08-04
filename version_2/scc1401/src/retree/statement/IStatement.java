package retree.statement;

import retree.regalloc.RegisterAllocator;

/**
 * Created by mplv on 7/29/15.
 */
public interface IStatement {
    public String generateCode(RegisterAllocator registerAllocator) throws Exception;
}
