public class FunctionType {

	private List<Type> paramList;
	private Type returnType;

	public FunctionType(List<Type> l, Type retType) {
		paramList = l;
		returnType = retType;
	}

	public List<Type> getParamList() {
		return paramList;
	}

	public Type getReturnType() {
		return returnType;
	}
}
