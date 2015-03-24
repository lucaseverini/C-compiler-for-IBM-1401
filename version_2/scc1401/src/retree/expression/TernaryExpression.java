/*
	TernaryExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import retree.exceptions.TypeMismatchException;
import compiler.SmallCC;
import retree.intermediate.*;
import static retree.RetreeUtils.*;

public class TernaryExpression extends Expression
{
	private Expression condition, positive, negative;

	public TernaryExpression(Expression condition, Expression positive, Expression negative) throws TypeMismatchException
	{
		super(positive.getType());

		if (!positive.getType().equals(negative.getType()))
		{
			throw new TypeMismatchException(negative, positive.getType(), negative.getType());
		}

		this.condition = condition;
		this.positive = positive;
		this.negative = negative;
	}


	public Expression collapse()
	{
		Expression c2 = condition.collapse();

		if (c2 instanceof ConstantExpression)
		{
			ConstantExpression c = (ConstantExpression)c2;
			return (c.getValue() == 0) ? negative.collapse() : positive.collapse();
		}

		try
		{
			return new TernaryExpression(c2, positive.collapse(), negative.collapse());
		}
		catch(TypeMismatchException e)
		{
			return null;
		}
	}

	public String generateCode(boolean valueNeeded)
	{

		String negLabel = label(SmallCC.nextLabelNumber());
		String endLabel = label(SmallCC.nextLabelNumber());
		Optimizer.addInstruction("Ternary (?:) " + this.toString(),"","");
		String code = COM("Ternary (?:) " + this.toString()) +
						condition.generateCode(true);
		Optimizer.addInstruction("Clear WM", "", "MCS", STACK_OFF(0), STACK_OFF(0));
		code += INS("Clear WM", null, "MCS", STACK_OFF(0), STACK_OFF(0));
		code += POP(condition.getType().sizeof());
		Optimizer.addInstruction("Jump if false", "", "BCE", negLabel, STACK_OFF(condition.getType().sizeof()), " ");
		code += INS("Jump if false", null, "BCE", negLabel, STACK_OFF(condition.getType().sizeof()), " ");
		code += positive.generateCode(valueNeeded);
		Optimizer.addInstruction("Jump to end", "", "B", endLabel);
		code += INS("Jump to end", null, "B", endLabel);
		Optimizer.addInstruction("Come here if False", negLabel, "NOP");
		code += INS("Come here if False", negLabel, "NOP");
		code += negative.generateCode(valueNeeded);
		Optimizer.addInstruction("End of Ternary", endLabel, "NOP");
		code += INS("End of Ternary", endLabel, "NOP");

		return code;
	}

	public String toString()
	{
		return "(" + condition + " ? " + positive + " : " + negative + ")";
	}
}
