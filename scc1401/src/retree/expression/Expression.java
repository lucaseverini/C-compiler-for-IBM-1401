package retree.expression;
import java.util.*;
import retree.program.Initializer;
import retree.type.Type;
import static retree.RetreeUtils.*;
public abstract class Expression {
	private Type type;
	private String asmSnippet = "";

	// the below two are for string only
	private boolean str = false;
	private Initializer init = null;

	public Expression(Type type) {
		this.type = type;
	}

	public Initializer getInitializer()
	{
		return init;
	}

	public void setInitializer(Initializer i)
	{
		init = i;
	}

	public Type getType() {
		return type;
	}

	public boolean getStr() {
		return str;
	}

	public void setStr(boolean b){
		str = b;
	}

	public String getASM()
	{
		return asmSnippet;
	}

	public void addASM(String s)
	{
		if (s.length() > 2)
		{
			s = s.substring(1,s.length()-1);
			Scanner sc = new Scanner(s);
			if (sc.hasNext())
			{
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
		}
	}

	public Expression collapse() {
		return this;
	}
	public abstract String generateCode(boolean valueNeeded);

}
