package retree.statement;

import retree.program.*;
import java.util.*;

public class BlockStatement implements Statement {
	private List<Initializer> initializers;
	private List<Statement> statements;
	
	public BlockStatement(List<Initializer> initializers, List<Statement> statements) {
		this.initializers = initializers;
		this.statements = statements;
	}
	
	public String generateCode() {
		String code = "";
		for (Initializer i : initializers) {
			code += i.generateCode();
		}
		for (Statement s : statements) {
			code += s.generateCode();
		}
		return code;
	}

}
