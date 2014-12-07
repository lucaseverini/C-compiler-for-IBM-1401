package retree.symtab;
import java.util.HashMap;
import retree.expression.VariableExpression;
import retree.type.Type;
public class SymTab {
	private HashMap<String,VariableExpression> table = new HashMap<String,VariableExpression>();
	private int localoffset = 0;
	private boolean isStatic = false;

	public SymTab() {
	}
	
	public SymTab(boolean isStatic) {
		if (isStatic) {
			this.isStatic = true;
			//beginning of static variables ?
			localoffset = 0;
		}
	}

	public VariableExpression get(String identifier) {
		return table.get(identifier) == null ? null : table.get(identifier);
	}

	public VariableExpression put(String identifier, Type t){
		VariableExpression varExp = new VariableExpression(t, localoffset, isStatic);
		localoffset += t.getWidth();
		table.put(identifier, varExp);
		return varExp;
	}
}
