package retree.statement;

import compiler.SmallCC;

import retree.expression.Expression;
import static retree.RetreeUtils.*;

public class IfStatement implements Statement {
	private Expression condition;
	private Statement ifClause, elseClause;
	
	
	public IfStatement(Expression condition, Statement ifClause, Statement elseClause) {
		this.condition = condition.collapse();
		this.ifClause = ifClause;
		this.elseClause = elseClause;
	}

	public String generateCode() {
		int size = condition.getType().sizeof();
		//the single label jumped to if the condition is false
		String falseLabel = label(SmallCC.nextLabelNumber());
		//the single label jumped to if the condition is true
		String trueLabel = null;
		if (elseClause != null) {
			trueLabel = label(SmallCC.nextLabelNumber());
		}
		String code = condition.generateCode(true);
		code += INS("MCS", STACK_OFF(0), STACK_OFF(0)); //this removes the word mark
		code += POP(size);
		code += INS("BCE", falseLabel, STACK_OFF(size), " ");
		//now the if clause
		code += ifClause.generateCode();
		if (elseClause != null) {
			code += INS("B", trueLabel);
			code += LBL_INS(falseLabel, "NOP");
			code += elseClause.generateCode();
			code += LBL_INS(trueLabel, "NOP");
		} else {
			code += LBL_INS(falseLabel, "NOP");
		}
		return code;
	}
}

