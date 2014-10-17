package wci.intermediate;

//The type parameter here is actually the java type of the value.
//it is needed for range checking
public class PascalRangeType<T> extends PascalIntegralType {
	private PascalIntegralType rangeOf;
	private Comparable<T> low, high;
	
	public PascalRangeType(PascalIntegralType rangeOf, Comparable<T> low, Comparable<T> high) {
		this.rangeOf = rangeOf;
		this.low = low;
		this.high = high;
	}
	
	public boolean rangeCheck(T value) {
		return low.compareTo(value) <= 0 && high.compareTo(value) >= 0;
	}
	
	public boolean equals(Object other) {
		//instanceof is all wonky with generic types
		//we have to use getClass, but that gives us nice compiler warnings
		//ignore them.
		return getClass() == other.getClass() &&
			((PascalRangeType<T>)other).rangeOf.equals(rangeOf) &&
			((PascalRangeType<T>)other).low.equals(low) &&
			((PascalRangeType<T>)other).high.equals(high);
	}
}
