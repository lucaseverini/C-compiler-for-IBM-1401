package retree.statement;

import compiler.SmallCC;

import retree.expression.Expression;
import static retree.RetreeUtils.*;

public class WhileStatement implements Statement {
	private Expression condition;
	private Statement body;
	
	
	public WhileStatement(Expression condition, Statement body) {
		this.condition = condition.collapse();
		this.body = body;
	}

	public String generateCode() {
		int size = condition.getType().sizeof();
		//the single label jumped to if the condition is false
		String topLabel = label(SmallCC.nextLabelNumber());
		//the single label jumped to if the condition is true
		String bottomLabel = label(SmallCC.nextLabelNumber());
		
		String code = LBL_INS(topLabel, "NOP");
		code += condition.generateCode(true);
		code += INS("MCS", STACK_OFF(0), STACK_OFF(0)); //this removes the word mark
		code += POP(size);
		code += INS("BCE", bottomLabel, STACK_OFF(size), " ");
		//now the if clause
		code += body.generateCode();
		code += INS("B", topLabel);
		code += LBL_INS(bottomLabel, "NOP");
		return code;
	}
}

