package retree.symtab;

import retree.type.Type;

public class FunctionDeclaration {
	private String label;
	private Type type;

	public FunctionDeclaration(String l, Type t) {
		this.label = l;
		this.type = t;
	}

	public Type getType(){return type;}
	public String getLabel(){return label;}

}
