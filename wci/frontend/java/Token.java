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
			case keyword:
				type_str = "keyword";
			case separator:
				type_str = "separator";
			case operator:
				type_str = "operator";
			case integerLiteral:
				type_str = "integerLiteral";
			case floatingPointLiteral:
				type_str = "floatingPointLiteral";
			case booleanLiteral:
				type_str = "booleanLiteral";
			case characterLiteral:
				type_str = "characterLiteral";
			case stringLiteral:
				type_str = "stringLiteral";
			case nullLiteral:
				type_str = "nullLiteral";
			case eof:
				type_str = "eof";
		}
		return "Token: "+ this.text + " Type: " + type_str;
	}

}
