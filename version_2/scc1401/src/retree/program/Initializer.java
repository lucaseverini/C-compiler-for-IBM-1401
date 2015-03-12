/*
	Initializer.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.program;

import static retree.RetreeUtils.*;
import retree.exceptions.*;
import retree.expression.*;
import retree.type.*;
import java.util.*;

public class Initializer 
{
	private final VariableExpression variable;
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
			for (int i = 0, index = 0; i < at.sizeof(); i += at.getArrayBaseType().sizeof(), index++)
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

	public String generateCode() throws Exception
	{
		String code = "";
				
		if (subInitializers != null) 
		{
			Boolean firstElement = true;
			
			ListIterator<Initializer> iter = subInitializers.listIterator(subInitializers.size());
			while(iter.hasPrevious()) 
			{
				Initializer init = iter.previous();
				
				if(firstElement && init.value != null)
				{
					firstElement = false;
					
					code += "\n";
					code += INS(null, null, "ORG", Integer.toString(init.getVariable().getOffset() - (init.getSize() - 1)));
				}
				
				code += init.generateCode();
			}
		} 
		else 
		{
			// In case we want to set the workmark at the right position but is not really necessary
			// code += INS("SW", variable.getWordMarkAddress());
		}
		
		if (value == null) 
		{
			return code;
		}
		
		if (value.getType() instanceof PointerType) 
		{
			if(variable.isStatic())
			{			
				if(!isArrayMember)
				{
					code += "\n";
					code += INS(null, null, "ORG", Integer.toString(variable.getOffset() - (this.getSize() - 1)));
				}
				
				code += INS(null, null, "DCW", "@" + ADDR_COD(value.getValue()) + "@");
			}
			else
			{
				code += INS(null, null, "LCA", ADDR_CONST(value.getValue(), isArrayMember), variable.getAddress());
			}
		} 
		else if (value.getType().equals(Type.intType))
		{
			if(variable.isStatic())
			{			
				if(!isArrayMember)
				{
					code += "\n";
					code += INS(null, null, "ORG", Integer.toString(variable.getOffset() - (this.getSize() - 1)));
				}
				
				code += INS(null, null, "DCW", "@" + COD(value.getValue()) + "@");
			}
			else
			{
				code += INS(null, null, "LCA", NUM_CONST(value.getValue(), isArrayMember), variable.getAddress());
			}
		}
		else if (value.getType().equals(Type.charType))
		{
			if(variable.isStatic())
			{			
				if(!isArrayMember)
				{
					code += "\n";
					code += INS(null, null, "ORG", Integer.toString(variable.getOffset() - (this.getSize() - 1)));
				}
				
				code += INS(null, null, "DCW", "@" + CHAR_COD(value.getValue()) + "@");
			}
			else
			{
				code += INS(null, null, "LCA", CHAR_CONST(value.getValue(), isArrayMember), variable.getAddress());
			}
		} 
		else 
		{
			return null;
		}
		
		return code;
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
		
		return code;
	}
	
	public VariableExpression getVariable()
	{
		return variable;
	}
	
	public int getSize() throws Exception
	{
		if (value.getType() instanceof PointerType) 
		{
			return 3;
		}
		else if (value.getType().equals(Type.intType))
		{
			return 5;
		}
		else if (value.getType().equals(Type.charType))
		{
			return 1;
		}
		else
		{
			throw new Exception("Invalid Initializer type");
		}
	}
}
