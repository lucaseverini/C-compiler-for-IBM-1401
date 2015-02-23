package retree.program;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.expression.*;
import retree.type.*;
import java.util.*;

public class Initializer 
{
	private VariableExpression variable;
	private ConstantExpression value;
	// private boolean isStatic; // unused
	private boolean isArrayMember;
	// these are used for array types
	private List<Initializer> subInitializers = null;
	
	public Initializer(VariableExpression variable, Expression value, boolean arrayMember) throws NonConstantExpressionException 
	{
		this.variable = variable;
		this.value = null;
		this.isArrayMember = arrayMember;
		
		if (value != null) 
		{
			value = value.collapse();
			if (value instanceof ConstantExpression)
			{
				this.value = (ConstantExpression) value;
			} 
			else 
			{
				throw new NonConstantExpressionException(value);
			}
		}
				
		if (variable.getType() instanceof ArrayType) 
		{
			this.subInitializers = new ArrayList<>();
			
			ArrayType at = (ArrayType) variable.getType();
			for (int i = 0, index=0; i < at.sizeof(); i += at.getArrayBaseType().sizeof(), index++)
			{
				VariableExpression subVar = new VariableExpression(at.getArrayBaseType(), variable.getOffset() - i, variable.isStatic(), variable + "[" + index + "]");
				Initializer subInitializer = new Initializer(subVar, (Expression)null, true);
				subInitializers.add(subInitializer);
			}
		}
	}

	public Initializer(VariableExpression variable, List<Expression> l) throws Exception 
	{
		this.variable = variable;
		this.value = null;
		
		if (variable.getType() instanceof ArrayType) 
		{
			this.subInitializers = new ArrayList<>();
			
			ArrayType at = (ArrayType) variable.getType();
			for (int i = 0, index = 0; i < at.sizeof(); i += at.getArrayBaseType().sizeof(), index++)
			{
				VariableExpression subVar = new VariableExpression(at.getArrayBaseType(), variable.getOffset() - i, variable.isStatic(), variable + "[" + index + "]");
				Expression tmp = l.get(i / at.getArrayBaseType().sizeof());
				tmp = tmp.collapse();
				Initializer subInitializer = new Initializer(subVar, tmp, true);
				subInitializers.add(subInitializer);
			}
		}
		else
		{
			throw new Exception("Not Array Type");
		}
	}

	public String generateCode() 
	{
		String code = "";
		
		if (subInitializers != null) 
		{
			for (Initializer i : subInitializers)
			{
				code += i.generateCode();
			}
		} 
		else 
		{
			// code += INS("SW", variable.getWordMarkAddress());
		}
		
		if (value == null) 
		{
			return code;
		}
		
		if (value.getType() instanceof PointerType) 
		{
			return code + INS("LCA", ADDR_CONST(value.getValue(), isArrayMember), variable.getAddress());
		} 
		else if (value.getType().equals(Type.intType))
		{
			return code + INS("LCA", NUM_CONST(value.getValue(), isArrayMember), variable.getAddress());
		}
		else if (value.getType().equals(Type.charType))
		{
			return code + INS("LCA", CHAR_CONST(value.getValue(), isArrayMember), variable.getAddress());
		} 
		else 
		{
			//oops
			return null;
		}
	}

	// this generates code that occurs when the initialized variable goes out of scope
	// mainly just clears the word marker
	public String freeCode() 
	{
		String code = "";
		
		if (subInitializers != null) 
		{
			for (Initializer i : subInitializers)
			{
				code += i.freeCode();
			}
		} 
		else 
		{
			// code += INS("CW", variable.getWordMarkAddress());
		}
		
		return code;
	}
}
