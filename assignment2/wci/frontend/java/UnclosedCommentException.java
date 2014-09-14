/*
	UnclosedCommentException.java

    Assignment #2 - CS153 - SJSU
	By Sean Papay, Matt Pleva, Luca Severini 
	September-14-2014
*/

package wci.frontend.java;

public class UnclosedCommentException extends Exception {
	//TODO - sean : add nice errors with line numbers n stuff
	@Override
	public String toString() {
		return "Unclosed comment";
	}
}
