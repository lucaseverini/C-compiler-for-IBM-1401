package retree.symtab;

import retree.symtab.SymTab;
import java.util.ArrayList;
import retree.expression.*;

public class SymbolTableStack {

	int globaloffset = 1000;

	private ArrayList<SymTab> stack = new ArrayList<SymTab>();

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

	public SymTab getGlobalSymTab() {
		return stack.get(0);
	}

	public VariableExpression searchStack(String identifier) {
		for(int i = stack.size()-1; i > -1; i --) {
			if (stack.get(i).get(identifier) != null) return stack.get(i).get(identifier);
		}
		return null;
	}

}
