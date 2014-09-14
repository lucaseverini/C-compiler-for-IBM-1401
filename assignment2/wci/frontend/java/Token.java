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

	private String text;
	private TokenType type;

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

	public String toString() {
		String type_str = "";
		switch (this.type) {
			case identifier:
				type_str = "identifier";
				break;
				
			case keyword:
				type_str = "keyword";
				break;
				
			case separator:
				type_str = "separator";
				break;
				
			case operator:
				type_str = "operator";
				break;
				
			case integerLiteral:
				type_str = "integerLiteral";
				break;
				
			case floatingPointLiteral:
				type_str = "floatingPointLiteral";
				break;
				
			case booleanLiteral:
				type_str = "booleanLiteral";
				break;
				
			case characterLiteral:
				type_str = "characterLiteral";
				break;
				
			case stringLiteral:
				type_str = "stringLiteral";
				break;
				
			case nullLiteral:
				type_str = "nullLiteral";
				break;
				
			case eof:
				type_str = "eof";
				break;
		}
		return "Token: "+ this.text + "\t\tType: " + type_str;
	}
}
