package retree.type;
import java.util.List;

public class FunctionType extends Type {

	private List<Type> paramTypes;
	private Type returnType;
	private boolean variadic;

	public FunctionType(Type returnType, List<Type> paramTypes, boolean variadic) {
		super(0); //has no defined size
		this.returnType = returnType;
		this.paramTypes = paramTypes;
		this.variadic = variadic;
	}

	public List<Type> getParamTypes() {
		return paramTypes;
	}

	public Type getReturnType() {
		return returnType;
	}
	
	public boolean Equals(Object other) {
		if (!(other instanceof FunctionType)) return false;
		FunctionType f = (FunctionType)other;
		if (!f.getReturnType().equals(returnType)) return false;
		if (f.getParamTypes().size() != paramTypes.size()) return false;
		for (int i = 0; i < paramTypes.size(); ++i) {
			if (!f.getParamTypes().get(i).equals(paramTypes.get(i))) return false;
		}
		return variadic == f.variadic;
	}
	
	public String toString() {
		String str = returnType.toString() + " (";
		for (Type paramType : paramTypes) {
			str += paramType.toString() + ", ";
		}
		if (variadic) str += "...";
		return str + ")";
	}
}
