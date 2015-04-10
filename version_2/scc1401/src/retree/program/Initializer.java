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
	private boolean isArrayMember;
	// Used for array types
	private List<Initializer> subInitializers = null;
	private int initializerSize = 0;
	
	public Initializer(VariableExpression variable, Expression value, boolean arrayMember) throws Exception, NonConstantExpressionException 
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
				VariableExpression subVar = new VariableExpression(at.getArrayBaseType(), variable.getOffset() - i, 
												variable.isStatic(), variable.isParameter(), variable + "[" + index + "]");
				Initializer subInitializer = new Initializer(subVar, (Expression)null, true);
				subInitializers.add(subInitializer);
				
				initializerSize += subInitializer.getInitializerSize();
			}
		}
		else
		{
			initializerSize = getVariable().getType().getSize();
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
				VariableExpression subVar = new VariableExpression(at.getArrayBaseType(), variable.getOffset() + i, 
														variable.isStatic(), variable.isParameter(), variable + "[" + index + "]");
				Expression tmp = l.get(i / at.getArrayBaseType().sizeof());
				tmp = tmp.collapse();
				Initializer subInitializer = new Initializer(subVar, tmp, true);
				subInitializers.add(subInitializer);
				
				initializerSize += subInitializer.getInitializerSize();
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
			// ORG directive only for initialization data of static variables
			if(variable.isStatic())
			{			
				code += "\n";
				code += INS(variable.toString(), null, "ORG", Integer.toString(subInitializers.get(0).getVariable().getOffset()));
			}
			
			ListIterator<Initializer> iter = subInitializers.listIterator(subInitializers.size());
			while(iter.hasPrevious()) 
			{
				Initializer init = iter.previous();	
				
				if(init.value == null && this.variable.isStatic()) // Uninitialized static variables are blank by default
				{
					init.value = new ConstantExpression(init.getVariable().getType());
				}
				
				code += init.generateCode();
			}
		} 
		else 
		{
			if(value == null && this.variable.isStatic()) // Uninitialized static variables are blank by default
			{
				value = new ConstantExpression(getVariable().getType());
			}

			// In case we want to set a default value and/or the wordmark at the right position but is not really necessary
			// code += INS("SW", variable.getWordMarkAddress());
		}

		if (value == null) 
		{
			return code;
		}
		
		if (variable.getType() instanceof PointerType) 
		{
			if(variable.isStatic())
			{			
				if(!isArrayMember)
				{
					code += "\n";
					code += INS(variable.toString(), null, "ORG", Integer.toString(variable.getOffset()));
				}
				
				code += INS(null, null, "DCW", "@" + ADDR_COD(value.getValue()) + "@");
			}
			else
			{
				String refType = "" + ((PointerType)variable.getType()).getRefType();
				String comm = "Load *" + refType + " " + value.getValue() + " into memory " + variable.getAddress();
				code += INS(comm, null, "LCA", ADDR_CONST(value.getValue(), isArrayMember), variable.getAddress());
			}
		} 
		else if (variable.getType().equals(Type.intType))
		{
			if(variable.isStatic())
			{			
				if(!isArrayMember)
				{
					code += "\n";
					code += INS(variable.toString(), null, "ORG", Integer.toString(variable.getOffset()));
				}
				
				if(variable.getType().isPointerType())
				{
					code += INS(null, null, "DCW", "@" + ADDR_COD(value.getValue()) + "@");
				}
				else
				{
					code += INS(null, null, "DCW", "@" + COD(value.getValue()) + "@");
				}
			}
			else
			{
				String comm = "Load int " + value.getValue() + " into memory " + variable.getAddress();
				code += INS(comm, null, "LCA", NUM_CONST(value.getValue(), isArrayMember), variable.getAddress());
			}
		}
		else if (variable.getType().equals(Type.charType))
		{
			if(variable.isStatic())
			{			
				if(!isArrayMember)
				{
					code += "\n";
					code += INS(variable.toString(), null, "ORG", Integer.toString(variable.getOffset()));
				}
				
				if(variable.getType().isPointerType())
				{
					code += INS(null, null, "DCW", "@" + ADDR_COD(value.getValue()) + "@");
				}
				else
				{
					code += INS(null, null, "DCW", "@" + CHAR_COD(value.getValue()) + "@");
				}
			}
			else
			{
				String comm = "Load char " + value.getValue() + " into memory " + variable.getAddress();
				code += INS(comm, null, "LCA", CHAR_CONST(value.getValue(), isArrayMember), variable.getAddress());
			}
		} 
		else 
		{
			return null;
		}
		
		return code;
	}

	// this generates code that occurs when the initialized variable goes out of scope
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
	
	public int getInitializerSize()
	{
		return initializerSize;
	}
	
	public int getSize() throws Exception
	{
		if(value == null)
		{
			return 0;
		}
		else if (value.getType() instanceof PointerType) 
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
