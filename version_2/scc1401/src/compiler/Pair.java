/*
	MyNode.java

    Small-C compiler - SJSU
	March-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package compiler;

public class Pair<A,B> 
{
	public A a;
	public B b;
	
	public Pair (A a, B b) 
	{
		this.a = a;
		this.b = b;
	}
	
	public Pair() 
	{
		a = null;
		b = null;
	}
}
