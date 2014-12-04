package retree.expression;
import retree.type.Type;
public  abstract class LValue extends Expression {
	public LValue(Type type) {
		super(type);
	}

	@Override
	public abstract LValue collapse();

	//like generateCode, but pushed the memory location to the stack instead
	public abstract String generateLocation();
}
