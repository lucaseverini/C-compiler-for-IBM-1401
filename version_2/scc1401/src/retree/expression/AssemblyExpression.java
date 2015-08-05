/*
	AssemblyExpression.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.expression;

import compiler.SmallCC;
import java.util.*;
import static retree.RetreeUtils.*;
//import retree.intermediate.Optimizer;
import retree.type.*;

public class AssemblyExpression extends Expression
{
	private String asmInstructions = "";
	private String args = "";
	private String operation = "";
	private String[] arguments;
	
	public AssemblyExpression() 
	{
		super(Type.intType);
	}
	
	@Override
	public Expression collapse() 
	{
		return this;
	}

	public void addASM(String s)
	{
		if (s.length() > 2)
		{
			if (args.equals(""))
			{
				args += s;
			} 
			else
			{
				args += "," + s;
			}
			
			s = s.substring(1,s.length()-1);
			Scanner sc = new Scanner(s);
			if (sc.hasNext())
			{
				String op = sc.next();
				
				ArrayList<String> instructionPartsList = new ArrayList<String>();
				
				while(sc.hasNext())
				{
					String tmp = sc.next();
					if(tmp.contains(","))
					{
						String[] parts = tmp.split(",");
						for (String tmp1 : parts)
						{
							instructionPartsList.add(tmp1);
						}
					}
					else 
					{
						instructionPartsList.add(tmp);
					}
				}
				
				sc.close();
				
				if(SmallCC.optimize > 0)
				{
					int len = instructionPartsList.toArray().length;
					
					String[] instrParts = new String[len];
					arguments = new String[len];
					
					for (int i = 0 ; i < len; i ++)
					{
						arguments[i] = instructionPartsList.get(i);
						instrParts[i] = instructionPartsList.get(i);
					}
					
					operation = op;
				}
				else
				{				
					int len = instructionPartsList.toArray().length;
					String[] instrParts = new String[len];
				
					asmInstructions += INS(null, null, op, instructionPartsList.toArray(instrParts));
				}
			}
		}
	}
	
// ***********************************************************************	
// **
// ** Would be great if the asm expression could work like the others...
// **
// ***********************************************************************
	
	@Override
	public String generateCode(boolean valueNeeded) 
	{
		if(SmallCC.optimize > 0)
		{
			INS(null,null,operation,arguments);
//			Optimizer.addInstruction("Start asm block", "", operation, arguments);
//			Optimizer.addInstruction("End asm block", "", "");
			
			return "";	// We don't need instruction if optimizer is active
		}
		else
		{
			return COM("Start asm block") + asmInstructions + COM("End asm block");
		}
	}


	@Override
	public Expression getLeftExpression() {
		return null;
	}

	@Override
	public Expression getRightExpression() {
		return null;
	}

	@Override
	public String toString() 
	{
		return "asm (" + args + ")";
	}
}

