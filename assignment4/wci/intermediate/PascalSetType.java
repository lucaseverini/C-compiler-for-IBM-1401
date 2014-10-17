package wci.intermediate;

public class PascalSetType extends PascalValueType {
	private PascalValueType elementType;
	public PascalSetType(PascalValueType elementType) {
		if (elementType == PascalValueType.INTEGER
			|| elementType instanceof PascalRangeType
			|| elementType instanceof PascalEnumType) {
				this.elementType = elementType;
		} else {
			throw new PascalTypeException("Invalid set element type");
		}
	}
	
	public boolean equals(Object other) {
		return other instanceof PascalSetType &&
			((PascalSetType)other).elementType.equals(elementType);
	}
}
