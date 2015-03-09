/*
	CommaExpression.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

public class CommaExpression extends Expression 
{
	private final Expression l, r;
	
	public CommaExpression(Expression l, Expression r)
	{
		super(r.getType());
		
		this.l = l;
		this.r = r;
	}
	
	public String generateCode(boolean valueNeeded) 
	{
		return l.generateCode(false) + r.generateCode(valueNeeded);
	}
}
