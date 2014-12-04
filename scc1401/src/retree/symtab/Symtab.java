package retree.symtab;
public class SymTab {
	private Hashmap<String,VariableExpression> table = new Hashmap<String,VariableExpression>();

	public SymTab() {;}

	public VariableExpression get(String identifier) {
		return table.get(identifier) == null ? null : table.get(identifier);
	}

	public void put(String identifier, VariableExpression varExp){
		table.put(identifier,varExp);
	}

	public boolean search(String identifier){
		return table.get(identifier) == null ? null : table.get(identifier);
	}
}
