package retree.program;

import static retree.RetreeUtils.*;

import java.util.List;
import java.util.ArrayList;

public class Program {
	private List<Initializer> initializers;
	private List<FunctionDefinition> functions;
	private int mainLabelNumber = -1;


	public Program() {
		initializers = new ArrayList<Initializer>();
		functions = new ArrayList<FunctionDefinition>();
	}

	public void addInitializer(Initializer init) {
		initializers.add(init);
	}

	public void addFunction(FunctionDefinition func) {
		functions.add(func);
	}
	
	public void setMainLabelNumber(int mainLabelNumber) {
		this.mainLabelNumber = mainLabelNumber;
	}

	// TODO - call main
	public String generateCode() {
		if (mainLabelNumber < 0) {
			//error
		}
		String code = "";
		for (Initializer init : initializers) {
			code += init.generateCode();
		}
		
		code += INS("B", label(mainLabelNumber));
		
		code += INS("H");
		
		for (FunctionDefinition func : functions) {
			code += func.generateCode();
		}
		return HEADER() + code + INS("END", "START");
	}
}
