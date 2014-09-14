package wci.frontend.java;

public class Tokenizer {
	private String source;
	private int position;
	private int tokenStart;

	public Tokenizer(String source) {
		this.source = source;
		position = 0;
		tokenStart = 0;
	}

	private boolean isSymbol(char c) {
		return "(){}[];,.:+-*/|&^%<>!~?".contains(""+c);
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

	private boolean isKeyword(String str) {
		String keywords = "abstract continue for new switch assert default "+
		"if package synchronized boolean do goto private this break double "+
		"implements protected throw byte else import public throws case enum "+
		"instanceof return transient catch extends int short try char final "+
		"interface static void class finally long strictfp volatile const float "+
		"native super while";
		return keywords.contains(str);
	}


	public Token nextToken() {
		char c;
		try {
			while(Character.isWhitespace(c = source.charAt(tokenStart))) {
				tokenStart++;
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
		} catch (IndexOutOfBoundsException e) {
			position = source.length();
			tokenStart = position;
			return new Token("", Token.TokenType.eof);
		}
		return null;
	}

	//TODO - Sean
	private Token nextNumberLiteral() throws IndexOutOfBoundsException {
		return null;
	}


	//TODO - Matt
	private Token nextIdentOrKeyword() throws IndexOutOfBoundsException{
		String token = "";
		while(!Character.isWhitespace(source.charAt(position)) &&
				!isSymbol(source.charAt(position))){
			token += source.charAt(position);
			position ++;
		}

		if (isKeyword(token)){
			return new Token(token, Token.TokenType.keyword);
		} else {
			return new Token(token, Token.TokenType.identifier);
		}

	}

	//TODO - someone
	private Token nextSymbolOrComment() throws IndexOutOfBoundsException{return null;}

	//TODO - Luca
	private Token nextStringOrCharLiteral() throws IndexOutOfBoundsException{return null;}


	public void reset() {
		position = 0;
		tokenStart = 0;
	}
}
