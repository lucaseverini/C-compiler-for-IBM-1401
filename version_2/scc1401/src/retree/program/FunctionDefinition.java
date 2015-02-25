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

	public ConstantExpression getDeclaration() {
		return declaration;
	}

	//As per our calling conventions, the caller is responsible for pushing
	//the new stack frame down.  We are simply responsible for our return
	//address.
	public String generateCode()
	{
		// add in function name get here
		return COM("FunctionDefinition(" + SmallCC.getFunctionNameFromExpression(declaration) + ")") +
			LBL_INS(label(declaration.getValue()), "SBR", "3+X3") + // Luca: This seems to have problems without SW
			INS("SW", "1+X3") +
			INS("CW", "2+X3") +
			INS("CW", "3+X3") +
			block.generateCode() +

			INS("LCA", "3+X3", "X1") +
			INS("B", "0+X1");
	}

}
