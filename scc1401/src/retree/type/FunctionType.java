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
}
