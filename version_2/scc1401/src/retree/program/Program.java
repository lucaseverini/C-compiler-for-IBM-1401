/*
	Program.java

    The Small-C cross-compiler for IBM 1401

	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.program;

import compiler.SmallCC;
import jdk.nashorn.internal.ir.FunctionCall;
import retree.expression.FunctionCallExpression;

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

		// Sort the functions by name
		Collections.sort(functions);

		// Holder for the main function so that we can do call tracing
		FunctionDefinition main = null;

		// find the main functionDefinition so that we can use it later
		for (FunctionDefinition func : functions)
		{
			if(func.toString().equals("main"))
			{
				main = func;
				break;
			}
		}

		// generate the code for the main method which has a side effect of
		// setting up the functions main calls
		mainFunc += main.generateCode();

		// now the we know what functions main calls lets start generating code for those functions
		while(!FunctionCallExpression.finishedProcessing()) {
			for (FunctionDefinition func : functions) {
				// beware that as we generate code for those functions they in turn may call other functions
				// that we have passed over already
				System.out.println(FunctionCallExpression.isCalledAndNotProcessed(func.toString()) + " " + func.toString());
				if (FunctionCallExpression.isCalledAndNotProcessed(func.toString())) {
					// now generate the code and restart searching for the functions
					funcs += func.generateCode();
					FunctionCallExpression.removeCall(func.toString());
					break;
				}
			}
		}
		
		code += mainFunc;
		code += funcs;
		
		code += FOOTER();
		
		if(SmallCC.optimize == 0)
		{
			code += INS("End of program code.", null, "END", "START");
		}
		
		return code ;
	}
}
