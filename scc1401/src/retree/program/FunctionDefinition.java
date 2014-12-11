package retree.program;
import static retree.RetreeUtils.*;
import retree.statement.*;
import retree.expression.*;

public class FunctionDefinition {
	private ConstantExpression declaration;
	private BlockStatement block;
	public FunctionDefinition(ConstantExpression declaration, BlockStatement block) {
		this.declaration = declaration;
		this.block = block;
	}
	
	//As per our calling conventions, the caller is responsible for pushing
	//the new stack frame down.  We are simply responsible for our return
	//address.
	public String generateCode() {
		return LBL_INS(label(declaration.getValue()), "SBR", "0+X3") +
			block.generateCode() +
			INS("B", "0+X3");
	}

}
