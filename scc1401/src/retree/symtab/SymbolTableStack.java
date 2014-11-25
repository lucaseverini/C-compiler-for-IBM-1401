public class SymbolTableStack {

	private ArrayList<SymTab> stack = new ArrayList<Stack>();

	public SymbolTableStack() {stack.add(new SymTab());}

	public SymTab peek() {
		return stack.get(stack.size()-1);
	}

	public SymTab pop() {
		return stack.remove(stack.size()-1);
	}

	public SymTab push() {
		stack.add(new SymTab());
		return stack.get(stack.size()-1);
	}

	public VariableNode searchStack(String identifier) {
		for(int i = stack.size()-1; i > -1; i --) {
			if (stack.get(i).get(identifier) != null) return stack.get(i).get(identifier);
		}
		return null;
	}

}
