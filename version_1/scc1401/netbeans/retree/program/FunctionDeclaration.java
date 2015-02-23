package retree.program;
import retree.type.*;

public class FunctionDeclaration {
	private String label;
	private FunctionType type;
	
	public FunctionDeclaration(String label, FunctionType type) {
		this.label = label;
		this.type = type;
	}
	
	public String getLabel() {
		return label;
	}
	
	public FunctionType getType() {
		return type;
	}

}
