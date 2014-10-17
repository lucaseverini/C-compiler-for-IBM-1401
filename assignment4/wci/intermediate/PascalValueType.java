package wci.intermediate;

public class PascalValueType {
	public PascalValueType() {
	}

	public boolean equals(Object other) {
		return other == this;
	}

	//all the atomic types simply exist as singletons
	//they have no defining properties apart from their uniqueness
	//most compound types are built recursively from these, except enums
	//and ranges, which are weird
	public static PascalValueType REAL = new PascalIntegralType();
	public static PascalValueType INTEGER = new PascalIntegralType();
	public static PascalValueType CHAR = new PascalIntegralType();
	public static PascalValueType BOOLEAN = new PascalIntegralType();
	public static PascalValueType SET = new PascalSetType();
}
