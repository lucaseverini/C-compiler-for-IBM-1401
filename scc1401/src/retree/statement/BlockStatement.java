package retree.statement;

import retree.program.*;
import java.util.*;
import static retree.RetreeUtils.*;

public class BlockStatement implements Statement {
	private List<Initializer> initializers;
	private List<Statement> statements;
	private int stackOffset;
	
	public BlockStatement(List<Initializer> initializers, List<Statement> statements, int stackOffset) {
		this.initializers = initializers;
		this.statements = statements;
		this.stackOffset = stackOffset;
	}
	
	public String generateCode() {
		String code = "";
		for (Initializer i : initializers) {
			code += i.generateCode();
		}
		code += INS("MA", ADDR_CONST(stackOffset),"X2");
		for (Statement s : statements) {
			code += s.generateCode();
		}
		code += INS("MA", ADDR_CONST(-stackOffset), "X2");
		return code;
	}

}
