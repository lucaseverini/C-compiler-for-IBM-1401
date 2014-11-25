public class SymTab {
	private Hashmap<String,VariableNode> table = new Hashmap<String,VariableNode>();

	public Symtab() {}

	public VariableNode get(String identifier) {
		return table.get(identifier) == null ? null : table.get(identifier);
	}

	public void put(String identifier, VariableNode varNode){
		table.put(identifier,varNode);
	}

	public boolean search(String identifier){
		return table.get(identifier) == null ? null : table.get(identifier);
	}
}
