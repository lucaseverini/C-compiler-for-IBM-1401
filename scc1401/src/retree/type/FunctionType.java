package retree.type;
import java.util.List;

public class FunctionType {

	private List<Type> paramList;
	private Type returnType;

	public FunctionType(Type retType, List<Type> l) {
		paramList = l;
		returnType = retType;
	}

	public List<Type> getParamList() {
		return paramList;
	}

	public Type getReturnType() {
		return returnType;
	}
	
	public String toString() {
		String str = returnType.toString() + " (";
		for (Type paramType : paramList) {
			str += paramType.toString() + ", ";
		}
		return str + ")";
	}
}
