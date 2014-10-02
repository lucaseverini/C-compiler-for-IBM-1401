/**
 * <h1>RuntimeSet</h1>
 *
 * <p>Print a pascal set description into a string</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */

package wci.backend.interpreter;

public class RuntimeSet 
{
	public long bits = 0;
	
	public RuntimeSet(long bits) 
	{
		this.bits = bits;
	}
	
	@Override
	public String toString() 
	{
		String out = "[";
		boolean commaPrefix = false;
		for (int i = 0; i <= 50; i++) {
			if ((bits & (1l<<i)) != 0) {
				if (commaPrefix) {
					out += ", ";
				}
				out += i;
				commaPrefix = true;
			}
		}
		return out + "]";	
	}
}
