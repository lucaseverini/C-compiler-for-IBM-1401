package wci.intermediate;

public class PascalEnumType extends PascalValueType {
	private List<String> members;
	public PascalEnumType(List<String> members) {
		this.members = members;
	}
	
	public boolean equals(Object other) {
		if (!other instanceof PascalEnumType) return false;
		PascalEnumType o = (PascalEnumType) other;
		if (o.members.size() != members.size()) return false;
		for (int i = 0; i < members.size(); ++i) {
			if !(members.get(i).equals(o.members.get(i))) return false;
		}
		return true;
	}
}
