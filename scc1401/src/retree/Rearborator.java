package retree;

import compiler.MyNode;
import static compiler.SmallCCTreeConstants.*;
import retree.symtab.SymbolTableStack;
import java.util.HashMap;
import retree.type.PointerType;
import retree.type.Type;
import retree.program.*;
import retree.expression.*;
import java.util.*;

public class Rearborator {
	/*
	private List<HashMap<String, VariableExpression>> variableTable;
	private HashMap<String, FunctionDeclaration> functionTable;
	private Program program;
	private int labelNum;

	public Program Reaborate(MyNode root) {
		variableTable = new ArrayList<HashMap<String, VariableExpression>>();
		variableTable.add(new HashMap<String, VariableExpression>());
		functionTable = new HashMap<String, FunctionDeclaration>();
		program = new Program();
		labelNum = 0;

		for (int i = 0; i < root.jjtGetNumChildren(); ++i) {
			MyNode programComponent = (MyNode)root.jjtGetChild(i);
			switch (programComponent.id) {
				case JJTFUNCTIONDEFINITION:
					program.addFunction(genFunctionDefinition(programComponent));
					break;
				case JJTFUNCTIONDECLARATION:
					parseFunctionDeclaration(programComponent);
					break;
				case JJTGLOBALVARIABLEDECLARATION:
					Initializer init = genGlobalVariableDeclaration(programComponent);
					if (init != null) {
						program.addInitializer(init);
					}
					symTabStack.getGlobal().put((String)programComponent.value,genType(programComponent));
					break;
				case JJTLOCALVARIABLEDECLARATION:
					symTabStack.peek().put((String)programComponent.value,genType(programComponent));
					break;
			}

		}
		return program;
	}

	public SymbolTableStack getSymTabStack() {return symTabStack;}

	private void parseFunctionDeclaration(MyNode dec) {
		MyNode returnType = null;
		String name;
		MyNode paramList;
		Type rType;
		if (dec.jjtGetChild(0).getId() == JJTTYPE) {
			returnType = dec.jjtGetChild(0);
			name = dec.jjtGetChild(1).getName();
			paramList = dec.jjtGetChild(2);
			rType = genType(returnType);
		} else {
			name = dec.jjtGetChild(0).getName();
			paramList = dec.jjtGetChild(1);
			rType = Type.intType;
		}

		List<Type> paramTypes = new ArrayList<Type>();
		for (int i = 0; i < paramList.jjtGetNumChildren; ++i) {
			Node paramNode = paramList.jjtGetChild(i);
			paramTypes.add(genType(paramNode.jjtGetChild(0)));
		}
		FunctionDeclaration declaration = new FunctionDeclaration(nextLabel, new FunctionType(rType, paramTypes));
		funcTab.insert(name, declaration);
	}

	private Type genType(MyNode typeNode) {
		//sometimes our types are children of a type node
		if (typeNode.getId() == JJTTYPE) {
			typeNode = typeNode.jjtGetChild(0);
		}
		switch (typeNode.getId()) {
			case JJTBARETYPE:
				if (typeNode.getName().equals("char")) return Type.charType;
				return Type.intType;
			case JJTPOINTERTYPE:
				return new PointerType(genType(typeNode.getChild(0)));
		}

	}

	private String nextLabel() {
		int num = labelNum++;
		String label = "F";
		label += ('A' + num%26);
		num/=26;
		label += ('A' + num%26);
		num/=26;
		label += ('A' + num%26);
		return label;
	}

	private static Expression ExpressionBuilder(MyNode n){
		if (n.jjtGetNumChildren() == 0)
		{
			return ExpressionTypeBuilder(n);
		}
		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			return ExpressionBuilder((MyNode)n.jjtGetChild(i));
		}
	}

	private static Expression ExpressionTypeBuilder(MyNode n) {
		String nodeType = n.toString();
		switch (nodeType) {
			case "":
		}
	}
	
	private Initializer genGlobalVariableDeclaration(MyNode declaration) {
		//TODO
		return null;
	}
	*/

}
