package retree;

import compiler.SimpleNode;

public class Rearborator{

	public static Program Reaborate(){
		return null;
	}

	private static Expression ExpressionBuilder(SimpleNode n){
		if (n.jjtGetNumChildren() == 0)
		{
			return ExpressionTypeBuilder(n)
		}
		for (int i = 0; i < n.jjtGetNumChildren(); i++) {
			return ExpressionBuilder(n.jjtGetChild(i));
		}
	}

	private static Expression ExpressionTypeBuilder(SimpleNode n) {
		String nodeType = n.toString();
		switch (nodeType) {
			case "":
		}
	}

}
