/*
	Program.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.program;

import compiler.SmallCC;
import static retree.RetreeUtils.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Program 
{
	private final List<Initializer> initializers;
	private final List<FunctionDefinition> functions;
	private int mainLabelNumber = -1;

	public Program() 
	{
		initializers = new ArrayList<Initializer>();
		functions = new ArrayList<FunctionDefinition>();
	}

	public void addInitializer(Initializer init) 
	{
		initializers.add(init);
	}

	public void addFunction(FunctionDefinition func) 
	{
		functions.add(func);
	}

	public void setMainLabelNumber(int mainLabelNumber) 
	{
		this.mainLabelNumber = mainLabelNumber;
	}

	// TODO - call main
	public String generateCode() throws Exception
	{
		if (mainLabelNumber < 0) 
		{
			return null;
		}
		
		String code = "";
		
		code += HEADER();
		
		code += SET_VARDATA(initializers);
		code += SET_CODE();
		code += SET_STACK();
	
		code += INS("Jump to function main", null, "B", label(mainLabelNumber));
		code += INS("Program executed. System halts", null, "H");
		
		String mainFunc = "";
		String funcs = "";
		
		Collections.sort(functions);
		
		for (FunctionDefinition func : functions) 
		{
			if(func.toString().equals("main"))
			{
				mainFunc += func.generateCode();
			}
			else
			{
				funcs += func.generateCode();
			}
		}
		
		code += mainFunc;
		code += funcs;
		
		code += FOOTER();
		
		if(!SmallCC.optimize)
		{
			code += INS("End of program code.", null, "END", "START");
		}
		
		return code ;
	}
}
