package wci.intermediate;

public class PascalArrayType extends PascalValueType {
	private PascalIntegralType  indexType
	private PascalValueType valueType;
	public PascalArrayType (PascalIntegralType indexType, PascalValueType valueType) {
		this.indexType = indexType;
		this.valueType = valueType;
	}
	
	public boolean equals(Object other) {
		return other instanceof PascalArrayType &&
			((PascalArrayType)other).indexType.equals(indexType) &&
			((PascalArrayType)other).valueType.equals(valueType);
			
	}
}
