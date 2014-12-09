package retree.program;
import static retree.RetreeUtils.*;
import retree.statement.*;

public class FunctionDefinition {
	private FunctionDeclaration declaration;
	private BlockStatement block;
	public FunctionDefinition(FunctionDeclaration declaration, BlockStatement block) {
		this.declaration = declaration;
		this.block = block;
	}
	
	//As per our calling conventions, the caller is responsible for pushing
	//the new stack frame down.  We are simply responsible for our return
	//address.
	public String generateCode() {
		return LBL_INS(declaration.getLabel(), "SBR", "0+X3") +
			PUSH() +                                               //push the return address
			block.generateCode() +
			INS("B", "0+X3");
	}

}
