package retree.expression;
import static retree.RetreeUtils.*;
import retree.expression.LValue;
import retree.expression.Expression;
import retree.type.PointerType;
public class AddressOfExpression extends Expression {
	private LValue child;

	public AddressOfExpression(LValue child) {
		super(new PointerType(child.getType()));
		this.child = child;
	}

	public String generateCode(boolean valueNeeded) 
	{
		if (valueNeeded) 
		{
			return COM("AddressOfExpression(" + child + ")") + child.generateAddress();
		} 
		else 
		{
			return COM("AddressOfExpression(" + child + ")") + child.generateCode(false);
		}
	}

	public Expression collapse() {
		return new AddressOfExpression(child.collapse());
	}

	public String toString()
	{
		return "( &" + child + " )";
	}

}
