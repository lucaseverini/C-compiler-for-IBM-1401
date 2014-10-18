package wci.intermediate;

import java.util.List;
import java.util.Map;

public class PascalRecordType extends PascalValueType
{
	private List<Map.Entry<String, PascalValueType>> entries;
	
	public PascalRecordType(List<Map.Entry<String, PascalValueType>> entries)
	{
		this.entries = entries;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof PascalRecordType)) return false;
		
		PascalRecordType o = (PascalRecordType) other;
		if (entries.size() != o.entries.size()) 
		{
			return false;
		}
		
		for (int i = 0; i < entries.size(); i++) 
		{
			if (!entries.get(i).getKey().equals(o.entries.get(i).getKey())) 
			{
				return false;
			}
			
			if (!entries.get(i).getValue().equals(o.entries.get(i).getValue())) 
			{
				return false;
			}
		}
		return true;
	}
}
