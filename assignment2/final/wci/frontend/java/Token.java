/*
	Token.java

    Assignment #2 - CS153 - SJSU
	By Sean Papay, Matt Pleva, Luca Severini 
	September-14-2014
*/

package wci.frontend.java;

/* A class representing a single token of a java program
 * based on the standards laid out in http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html
 */

public class Token {
	public enum TokenType {
		identifier, keyword, separator, operator,
		integerLiteral, floatingPointLiteral, booleanLiteral,
		characterLiteral, stringLiteral, nullLiteral, eof
	}

	private final String text;
	private final TokenType type;

	protected Token(String text, TokenType type) {
		this.text = text;
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public TokenType getType() {
		return type;
	}

	public String typeString() {
		String type_str = "";
		switch (this.type) {
			case identifier:
				type_str = "IDENTIFIER";
				break;
				
			case keyword:
				type_str = "KEYWORD";
				break;
				
			case separator:
				type_str = "SEPARATOR";
				break;
				
			case operator:
				type_str = "OPERATOR";
				break;
				
			case integerLiteral:
				type_str = "INTEGER";
				break;
				
			case floatingPointLiteral:
				type_str = "FLOAT";
				break;
				
			case booleanLiteral:
				type_str = "BOOLEAN";
				break;
				
			case characterLiteral:
				type_str = "CHARACTER";
				break;
				
			case stringLiteral:
				type_str = "STRING";
				break;
				
			case nullLiteral:
				type_str = "NULL";
				break;
				
			case eof:
				type_str = "EOF";
				break;
		}
		return type_str;
	}
	
	public String textString() {
		return text;
	}
}
