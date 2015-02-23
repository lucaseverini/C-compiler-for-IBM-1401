package retree.symtab;

import compiler.SmallCC;
import retree.symtab.SymTab;
import java.util.ArrayList;
import retree.expression.*;
import retree.type.*;

public class SymbolTableStack {

	int staticOffset = 0;	// MUST BE CALCULATED ACCORDINGLY TO AVOID COLLISIONS WITH STACK OR CODE (Luca)

	private ArrayList<SymTab> stack = new ArrayList<SymTab>();

	public SymbolTableStack(int staticsPos) throws Exception 
	{
		if(staticsPos == 0)
		{
			throw new Exception();
		}
		
		staticOffset = staticsPos;
		stack.add(new SymTab(staticOffset, true));
	}

	public SymbolTableStack(int staticsPos, int moveStackDownBy) throws Exception 
	{
		if(staticsPos == 0)
		{
			throw new Exception();
		}

		staticOffset = staticsPos;
		staticOffset += moveStackDownBy;
		stack.add(new SymTab(staticOffset, true));
	}

	public SymTab peek() {
		return stack.get(stack.size()-1);
	}

	public SymTab pop() {
		return stack.remove(stack.size()-1);
	}

	public SymTab push() {
		if (peek().isStatic()) {
			stack.add(new SymTab(0, false));
		} else {
			stack.add(new SymTab(peek().getLocalOffset(), false));
		}
		return peek();
	}

	public SymTab push(int spaceToReserve) {
		if (peek().isStatic()) {
			stack.add(new SymTab(0, spaceToReserve, false));
		} else {
			stack.add(new SymTab(peek().getLocalOffset(), spaceToReserve, false));
		}
		return peek();
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
