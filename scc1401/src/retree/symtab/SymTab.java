package retree.symtab;
import java.util.HashMap;
import retree.expression.VariableExpression;
import retree.type.Type;
public class SymTab {
	private HashMap<String,VariableExpression> table = new HashMap<String,VariableExpression>();
	private int localOffset = 5;
	private int paramOffset = -10;
	private boolean isStatic = false;
	
	public SymTab(int spaceToReserve, boolean isStatic) {
		localOffset = spaceToReserve;
		this.isStatic = isStatic;
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
			VariableExpression varExp = new VariableExpression(t, localOffset, isStatic);
			localOffset += t.getWidth();
			table.put(identifier, varExp);
			return varExp;
		}
	}
	
	public int getLocalOffset() {
		return localOffset;
	}
}
