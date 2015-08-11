/*
	SubscriptExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import retree.expression.Expression;
import retree.regalloc.RegisterAllocator;

import static retree.RetreeUtils.*;

public class WhileStatement extends LoopStatement
{		
	public WhileStatement(Expression condition, Statement body) 
	{
		super(condition, body);
/*		
		this.condition = condition.collapse();
		this.body = body;
		// the single label jumped to if the condition is false
		this.topLabel = label(SmallCC.nextLabelNumber());
		// the single label jumped to if the condition is true
		this.bottomLabel = label(SmallCC.nextLabelNumber());
		this.size = condition.getType().sizeof();
*/
	}

	// reg alloc stuff
	@Override
	public String generateCode(RegisterAllocator registerAllocator) throws Exception
	{
		registerAllocator.linearScanRegisterAllocation(expressionList);
		String code = COM("While " + this.toString());

		code += INS("Top of While", topLabel, "NOP");


		code += condition.generateCode(true);

		if (SmallCC.nostack)
		{
			code += INS("Jump to bottom of While", null, "BCE", bottomLabel, REG(condition), "0");
			code += INS("Jump to bottom of While", null, "BCE", bottomLabel, REG(condition), "!");
			code += INS("Jump to bottom of While", null, "BCE", bottomLabel, REG(condition), "?");

			code += body.generateCode(registerAllocator);

			code += INS("Jump to top of While", null, "B", topLabel);
			code += "\n";

			code += INS("Bottom of While", bottomLabel, "NOP");
		}
		else {
			code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0)); // this removes the word mark
			code += POP(size);
			code += INS("Jump to bottom of While", null, "BCE", bottomLabel, STACK_OFF(size), " ");

			code += body.generateCode(registerAllocator);

			code += INS("Jump to top of While", null, "B", topLabel);
			code += "\n";

			code += INS("Bottom of While", bottomLabel, "NOP");
		}
		code += "\n";
		
		return code;
	}

	@Override
    public String toString()
    {
		return "[while (" + condition + ") " + body + " top:" + topLabel + " bottom:" + bottomLabel + "]";
	}

	public Expression getCondition() {
		return condition;
	}
}

