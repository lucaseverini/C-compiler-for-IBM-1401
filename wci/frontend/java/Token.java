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

}
