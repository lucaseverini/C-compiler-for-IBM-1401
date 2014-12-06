package retree.program;

import java.util.List;
import java.util.ArrayList;

public class Program {
	private List<Initializer> initializers;
	private List<FunctionDefinition> functions;


	public Program() {
		initializers = new ArrayList<Initializer>();
		functions = new ArrayList<FunctionDefinition>();
	}

	public void addInitializer(Intializer init) {
		initializers.add(init);
	}

	public void addFunction(FunctionDefinition func) {
		functions.add(func);
	}

	// TODO - call main
	private String generateCode() {
		String code = "";
		for (Initializer init : initializers) {
			code += init.generateCode();
		}
		for (FunctionDefinition func : functions) {
			code += func.generateCode();
		}
		return code;
	}
}