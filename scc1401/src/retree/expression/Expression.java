package retree.expression;
import java.util.*;
import retree.type.Type;
import static retree.RetreeUtils.*;
public abstract class Expression {
	private Type type;
	private String asmSnippet = "";

	public Expression(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public String getIdent()
	{
		return asmSnippet;
	}

	public void setIdent(String s)
	{
		s = s.substring(1,s.length()-1);
		Scanner sc = new Scanner(s);
		String op = sc.next();
		ArrayList<String> instructionPartsList = new ArrayList<String>();
		while(sc.hasNext())
		{
			String tmp = sc.next();
			if(tmp.contains(","))
			{
				String[] parts = tmp.split(",");
				for (String tmp1 : parts) {
					instructionPartsList.add(tmp1);
				}
			} else {
				instructionPartsList.add(tmp);
			}
		}
		sc.close();
		int len = instructionPartsList.toArray().length;
		String[] instrParts = new String[len];
		asmSnippet += INS(op,instructionPartsList.toArray(instrParts));
	}

	public Expression collapse() {
		return this;
	}
	public abstract String generateCode(boolean valueNeeded);

}
