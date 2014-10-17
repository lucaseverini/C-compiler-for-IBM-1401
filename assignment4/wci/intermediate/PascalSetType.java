package wci.intermediate;

public class PascalSetType extends PascalValueType {
	private PascalIntegralType elementType;
	public PascalSetType(PascalIntegralType elementType) {
		this.elementType = elementType;
	}
	
	public boolean equals(Object other) {
		return other instanceof PascalSetType &&
			((PascalSetType)other).elementType.equals(elementType);
	}
}
