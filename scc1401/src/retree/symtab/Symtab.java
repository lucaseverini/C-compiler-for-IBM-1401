package retree.symtab;
import java.util.HashMap;
import retree.expression.VariableExpression;
import retree.type.Type;
public class SymTab {
	private HashMap<String,VariableExpression> table = new HashMap<String,VariableExpression>();
	private int localoffset = 0;

	public SymTab() {;}

	public VariableExpression get(String identifier) {
		return table.get(identifier) == null ? null : table.get(identifier);
	}

	public void put(String identifier, Type t){
		VariableExpression varExp = new VariableExpression(t, localoffset);
		localoffset += 2;
		table.put(identifier,varExp);
	}

	public VariableExpression search(String identifier){
		return table.get(identifier) == null ? null : table.get(identifier);
	}
}
