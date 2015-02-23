//java really needs a built-in pair class...
package compiler;
public class Pair<A,B> {
	public A a;
	public B b;
	public Pair (A a, B b) {
		this.a = a;
		this.b = b;
	}
	public Pair() {
		a = null;
		b = null;
	}
}
