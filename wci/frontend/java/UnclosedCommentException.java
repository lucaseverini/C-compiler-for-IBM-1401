package wci.frontend.java;

public class UnclosedCommentException extends Exception {
	//TODO - sean : add nice errors with line numbers n stuff
	@Override
	public String toString() {
		return "Unclosed comment";
	}
}
