package retree.statement;

import java.util.*;

public class BlockStatement implements Statement {
	private List<Statement> substatements;
	
	public String generateCode() {
		String code = "";
		for (Statement s : substatements) {
			code += s.generateCode();
		}
		return code;
	}

}
