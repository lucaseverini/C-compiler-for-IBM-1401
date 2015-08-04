/*
	ExpressionStatement.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import retree.expression.Expression;
import retree.regalloc.RegisterAllocator;

import java.util.ArrayList;

public class ExpressionStatement extends Statement
{
	private final Expression exp;

	private ArrayList<Expression> list = new ArrayList<>();
	
	public ExpressionStatement(Expression exp)
	{
		this.exp = exp.collapse();
	}

	public Expression getExpression(){return exp;}

	public ArrayList<Expression> getList(){return list;}

	@Override
	public String generateCode(RegisterAllocator registerAllocator)
	{
		registerAllocator.linearScanRegisterAllocation(expressionList);
		return exp.generateCode(false);
	}

	@Override
	public String toString()
    {
       return exp.toString();
    }
}
