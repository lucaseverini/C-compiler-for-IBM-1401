package retree.statement;

import compiler.SmallCC;

import retree.expression.Expression;
import static retree.RetreeUtils.*;

public class ForStatement implements Statement {
	private Expression init = null, condition = null, post = null;
	private Statement body;
	
	private String topLabel;
	private String bottomLabel;
	private String continueLabel;
	
	
	public ForStatement(Expression init, Expression condition, Expression post, Statement body) {
		if (init != null) {
			this.init = init.collapse();
		}
		if (condition != null) {
			this.condition = condition.collapse();
		}
		if (post != null) {
			this.post = post.collapse();
		}
		this.body = body;
		
		topLabel = label(SmallCC.nextLabelNumber());
		bottomLabel = label(SmallCC.nextLabelNumber());
		continueLabel = label(SmallCC.nextLabelNumber());
	}

	public String generateCode() {
		int size = condition.getType().sizeof();
		String code = "";
		if (init!= null) {
			code = COM("ForStatement(" + init + "," + condition + "'" + post + ":" + body + ":" + topLabel +":" + bottomLabel + ":" + continueLabel + ")") +
			init.generateCode(false);
		}
		code += LBL_INS(topLabel, "NOP");
		if (condition != null) {
			code += condition.generateCode(true);
			code += INS("MCS", STACK_OFF(0), STACK_OFF(0));
			code += POP(size);
			code += INS("BCE", bottomLabel, STACK_OFF(size), " ");
		}
		code += body.generateCode();
		code += LBL_INS(continueLabel, "NOP");
		if (post != null) {
			code += post.generateCode(false);
		}
		code += INS("B", topLabel);
		code += LBL_INS(bottomLabel, "NOP");
		return code;
	}
}

