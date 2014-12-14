package retree.statement;

import retree.expression.*;
import retree.exceptions.*;
import static retree.RetreeUtils.*;

public class ReturnStatement implements Statement {
	private Expression exp;
	private VariableExpression returnLocation;
	private String returnLabel;
	
	public ReturnStatement(Expression exp, VariableExpression returnLocation, String returnLabel) {
		this.exp = exp;
		this.returnLocation = returnLocation;
		this.returnLabel = returnLabel;
	}
	
	public String generateCode() {
		String code = "";
		int offset = returnLocation.getOffset();
		if (exp != null && returnLocation != null) {
			code += exp.generateCode(true);
			code += POP(exp.getType().sizeof(), OFF(offset));
		}
		code += COM("set the return flag, so we know do deallocate our stack");
		code += INS("MCW", "@R@", "RF");
		
		code += COM("and branch");
		code += INS("B", returnLabel);
		return code;
		
	}
	
	
}
