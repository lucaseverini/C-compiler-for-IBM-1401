package wci.frontend.java;

public class Tokenizer {
	private String source;
	private int position;
	private int tokenStart;

	pubic Tokenizer(String source) {
		this.source = source;
		position = 0;
		tokenStart = 0;
	}

	private isSymbol(char c) {
		return "(){}[];,.:+-*/|&^%<>!~?".contains(c)

	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private boolean isIdentifierStart(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '$' || c == '_';
	}

	private boolean isIdentifierChar(char c) {
		return isDigit(c) || isIdentifierStart(c);
	}

	public Token nextToken() {
		char c;
		while(Character.isWhitespace(c = source.charAt(tokenStart))) {
			tokenStaart++;
		}
		position = tokenStart;
		if (isDigit(c)) {
			return nextNumberLiteral();
		}
		if (isIdentifierStart(c)) {
			return nextIdentOrKeyword();
		}
		if (isSymbol(c)) {
			return nextSymbolOrComment();
		}
		if (c == '\'' || c == '\"') {
			return nextStringOrCharLiteral();
		}
	}
	
	//TODO - Sean
	private Token nextNumberLiteral();
	
	
	//TODO - Matt
	private Token nextIdentOrKeyword();
	
	//TODO - someone
	private Token nextSymbolOrComment();
	
	//TODO - Luca
	private Token nextStringOrCharLiteral();


	public void reset() {
		position = 0;
		tokenStart = 0;
	}
}
