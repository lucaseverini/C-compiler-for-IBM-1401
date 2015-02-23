package retree.statement;

import compiler.SmallCC;

import retree.expression.Expression;
import static retree.RetreeUtils.*;

public class DoWhileStatement implements Statement {
	private Expression condition;
	private Statement body;
	
	
	public DoWhileStatement(Expression condition, Statement body) {
		this.condition = condition.collapse();
		this.body = body;
	}

	public String generateCode() {
		int size = condition.getType().sizeof();
		//the single label jumped to if the condition is false
		String topLabel = label(SmallCC.nextLabelNumber());
		String bottomLabel = label(SmallCC.nextLabelNumber());
		
		String code = COM("DoWhileStatement(" + condition + ":" + body + ")") +
		LBL_INS(topLabel, "NOP");
		code += body.generateCode();	
		code += condition.generateCode(true);
		code += INS("MCS", STACK_OFF(0), STACK_OFF(0));
		code += POP(size);
		code += INS("BCE", bottomLabel, STACK_OFF(size), " ");
		code += INS("B", topLabel);
		code += LBL_INS(bottomLabel, "NOP");
		
		return code;
	}
}

