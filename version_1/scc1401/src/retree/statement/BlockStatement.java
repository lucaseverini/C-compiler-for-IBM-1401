package retree.statement;

import retree.program.*;
import java.util.*;
import static retree.RetreeUtils.*;

public class BlockStatement implements Statement {
	private List<Initializer> initializers;
	private List<Statement> statements;
	private int stackOffset;
	private String returnLabel, parentReturnLabel;
	
	/*
	public BlockStatement(List<Initializer> initializers, List<Statement> statements, int stackOffset) {
		this.initializers = initializers;
		this.statements = statements;
		this.stackOffset = stackOffset;
	}
	*/
	
	
	public BlockStatement(List<Initializer> initializers, List<Statement> statements, int stackOffset, String returnLabel, String parentReturnLabel) {
		this.initializers = initializers;
		this.statements = statements;
		this.stackOffset = stackOffset;
		this.returnLabel = returnLabel;
		this.parentReturnLabel = parentReturnLabel;
	}
	
	public String generateCode() {
		String code = "";
		for (Initializer i : initializers) {
			code += i.generateCode();
		}
		if (stackOffset > 0) {
			code += INS("MA", ADDR_CONST(stackOffset),"X2");
		}
		for (Statement s : statements) {
			code += s.generateCode();
		}
		
		//if we call return from the function, we jump here
		if (returnLabel != null) {
			code += LBL_INS(returnLabel, "NOP");
		}
		if (stackOffset > 0) {
			code += INS("MA", ADDR_CONST(-stackOffset), "X2");
		}
		for (Initializer i : initializers) {
			code += i.freeCode();
		}
		
		//if we were returning, and there are more blocks to escape,
		//keep going up
		if (parentReturnLabel != null) {
			code += INS("BCE", parentReturnLabel, "RF", "R");
		} else {
			//if we reached the top, clear our return flag
			code += INS("MCW", "@ @", "RF");
		}
		
		return code;
	}

}
