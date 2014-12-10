package retree.symtab;

import retree.symtab.SymTab;
import java.util.ArrayList;
import retree.expression.*;
import retree.type.*;

public class SymbolTableStack {
	
	private ArrayList<SymTab> stack = new ArrayList<SymTab>();

	public SymbolTableStack() {
		stack.add(new SymTab(1000, true));
	}

	public SymTab peek() {
		return stack.get(stack.size()-1);
	}

	public SymTab pop() {
		return stack.remove(stack.size()-1);
	}

	public SymTab push() {
		stack.add(new SymTab(0, false));
		return stack.get(stack.size()-1);
	}
	
	public SymTab push(int spaceToReserve) {
		stack.add(new SymTab(spaceToReserve, false));
		return stack.get(stack.size()-1);
	}

	public SymTab getStaticSymTab() {
		return stack.get(0);
	}

	public VariableExpression searchStack(String identifier) {
		for(int i = stack.size()-1; i > -1; i --) {
			if (stack.get(i).get(identifier) != null) return stack.get(i).get(identifier);
		}
		return null;
	}
	
	public VariableExpression searchTop(String identifier) {
		return peek().get(identifier);
	}
	
	public VariableExpression add(String identifier, Type type, boolean isParam) throws Exception{
		SymTab table = peek();
		if (table.get(identifier) != null) {
			throw new Exception("Redefinition of variable " + identifier);
		} else {
			return table.put(identifier, type, isParam);
		}
	}
	
	public VariableExpression addStatic(String identifier, Type type) throws Exception{
		SymTab table = getStaticSymTab();
		if (table.get(identifier) != null) {
			throw new Exception("Redefinition of variable " + identifier);
		} else {
			return table.put(identifier, type, false);
		}
	}

}
