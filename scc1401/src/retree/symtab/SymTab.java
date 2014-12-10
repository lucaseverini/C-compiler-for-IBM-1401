package retree.symtab;
import java.util.HashMap;
import retree.expression.VariableExpression;
import retree.type.Type;
public class SymTab {
	private HashMap<String,VariableExpression> table = new HashMap<String,VariableExpression>();
	private int localoffset = 5;
	private int paramOffset = -10;
	private boolean isStatic = false;

	public SymTab() {
	}

	public SymTab(boolean isStatic) {
		if (isStatic) {
			this.isStatic = true;
			//beginning of static variables ?
			localoffset = 1000;
		}
	}

	public VariableExpression get(String identifier) {
		return table.get(identifier) == null ? null : table.get(identifier);
	}

	public VariableExpression put(String identifier, Type t, boolean isParam){
		if (isParam) {
			VariableExpression varExp = new VariableExpression(t, paramOffset, false);
			paramOffset -= t.getWidth();
			table.put(identifier, varExp);
			return varExp;
		} else {
			VariableExpression varExp = new VariableExpression(t, localoffset, isStatic);
			localoffset += t.getWidth();
			table.put(identifier, varExp);
			return varExp;
		}
	}
}
