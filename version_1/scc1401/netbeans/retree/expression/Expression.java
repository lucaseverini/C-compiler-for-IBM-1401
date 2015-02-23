package retree.expression;

import java.util.*;
import retree.program.Initializer;
import retree.type.Type;
import static retree.RetreeUtils.*;

public abstract class Expression
{
	private final Type type;
	
	public Expression(Type type) 
	{
		this.type = type;
	}

	public Type getType() 
	{
		return type;
	}

	public Expression collapse()
	{
		return this;
	}
	
	public abstract String generateCode(boolean valueNeeded);
}
