/*
	TernaryExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import retree.exceptions.TypeMismatchException;
import compiler.SmallCC;
import static retree.RetreeUtils.*;

public class TernaryExpression extends Expression
{
	private Expression condition, positive, negative;

	public TernaryExpression(Expression condition, Expression positive, Expression negative) throws TypeMismatchException 
	{
		super(positive.getType());
		associativity = false;
		if (!positive.getType().equals(negative.getType()))
		{
			throw new TypeMismatchException(negative, positive.getType(), negative.getType());
		}
		
		this.condition = condition;
		this.positive = positive;
		this.negative = negative;
	}
	
	
	@Override
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
	
	@Override
	public String generateCode(boolean valueNeeded) 
	{
		String negLabel = label(SmallCC.nextLabelNumber());
		String endLabel = label(SmallCC.nextLabelNumber());
		
		String code = COM("Ternary (?:) " + this.toString()) +
						condition.generateCode(true);

		if (SmallCC.nostack) {
			code += INS("Jump if false", null, "BCE", negLabel, "0+"+REG(condition), "0");
			code += INS("Jump if false", null, "BCE", negLabel, "0+"+REG(condition), "!");
			code += INS("Jump if false", null, "BCE", negLabel, "0+"+REG(condition), "?");
			code += positive.generateCode(valueNeeded);
			code += INS("Jump to end", null, "B", endLabel);
			code += INS("Come here if False", negLabel, "NOP");
			code += negative.generateCode(valueNeeded);
			code += INS("End of Ternary", endLabel, "NOP");
		} else {
			code += INS("Clear WM in stack", null, "MCS", STACK_OFF(0), STACK_OFF(0));
			code += POP(condition.getType().sizeof());
			code += INS("Jump if false", null, "BCE", negLabel, STACK_OFF(condition.getType().sizeof()), " ");
			code += positive.generateCode(valueNeeded);
			code += INS("Jump to end", null, "B", endLabel);
			code += INS("Come here if False", negLabel, "NOP");
			code += negative.generateCode(valueNeeded);
			code += INS("End of Ternary", endLabel, "NOP");
		}
		
		return code;
	}

	@Override
	public Expression getLeftExpression() {
		return positive;
	}

	@Override
	public Expression getRightExpression() {
		return negative;
	}

	@Override
	public String toString()
	{
		return "(" + condition + " ? " + positive + " : " + negative + ")";
	}
}
