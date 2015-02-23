/*
	PreProcSymTab.java

    Assignment #6 - CS153 - SJSU
	November-10-2014

	By Sean Papay, Matt Pleva, Luca Severini
*/

package preprocessor;

import java.util.*;

// PreProcSymTab -----------------------------------------------------
public class PreProcSymTab extends HashMap<String, String>
{
	private static final long serialVersionUID = 1L;
	
	public PreProcSymTab()
	{
		super();
		
		// Add some defaults values used by Autocoder assembly directives
		put("STACK", "400");
		put("STACK_SIZE", "2500");
		put("DATA", "2000");
		put("CODE", "3000");
	}
	
	@Override
	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		
		Set<String> keys = keySet();
        for(String key: keys)
		{
			buf.append(key);
			
			String value = get(key);
			if(value != null)
			{
				buf.append(" = ");
				buf.append(value);
			}
			
			buf.append("\n");
 		}
		
		return buf.toString();
	}
	
	public boolean isDefined(String name)
	{
		return containsKey(name);
	}

	public boolean isUndefined(String name)
	{
		return !containsKey(name);
	}

	public String getValue(String name)
	{
		return get(name);
	}

	public void setValue(String name, String value)
	{
		put(name, value);
	}

	// Just for test
	public static void main(String[] args) throws Exception
	{
		PreProcSymTab tab = new PreProcSymTab();

		System.out.println(tab);

		tab.put("FRUIT", "Banana");
		tab.put("CHEESE", null);

		System.out.println(tab);

		String val = tab.get("FRUIT");
		System.out.println("FRUIT: " + val);
		
		tab.remove("FRUIT");

		tab.put("FRUIT", "Apple");
		tab.put("FRUIT2", "Melon");
		tab.put("FRUIT", "Apple");
		tab.put("FRUIT", "Pineapple");

		System.out.println(tab);
	}
}
