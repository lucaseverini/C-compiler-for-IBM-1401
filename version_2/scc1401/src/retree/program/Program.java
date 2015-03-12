/*
	Program.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.program;

import static retree.RetreeUtils.*;

import java.util.List;
import java.util.ArrayList;

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
	public String generateCode() 
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
	
		code += INS("B", label(mainLabelNumber));
		code += INS("H");

		for (FunctionDefinition func : functions) 
		{
			code += func.generateCode();
		}
		
		code += FOOTER();
		
		code += INS("END", "START");
		
		return code ;
	}
}
