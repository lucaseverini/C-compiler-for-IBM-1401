package wci.frontend.java;

public class Tokenizer {
	private String source;
	private int position;
	private int tokenStart;

	pubic Tokenizer(String source) {
		this.source = source + '\n';
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
		try {
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
		} catch (IndexOutOfBoundsException e) {
			position = source.length();
			tokenStart = position;
			return new Token("", eof);
		}
	}
	
	//TODO - Sean
	private Token nextNumberLiteral() throws IndexOutOfBoundsException {
		int start = position;
		boolean hexidecimal = false;
		boolean expectingExponent = false;
		Token.TokenType type = integerLiteral;
		char c;
		while (c = source.charAt(position++)) {
			if (c == 'x' || c == 'X') hexidecimal  = true;
			else if (c == '.') type == floatingPointLiteral;
			else if ((c == 'e' || c == 'E') && ! hexidecimal) {
				type = floatingPointLiteral;
				expectingExponent = true;
			}
			else if (c == 'p' || c == 'P') {
				type = floatingPointLiteral;
				expectingExponent = true;
			} else if (expectingExponent && c == '+' || c == '-') continue;
			else if (!isIdentifierChar(c) {
				return new Token(source.substring(start, position), type);
			}
		}
	}
	
	
	//TODO - Matt
	private Token nextIdentOrKeyword() throws IndexOutOfBoundsException{
	}
	
	//TODO - someone
	//Remember, if the token is a '.', it may be the start of a floating point literal
	//this method should call nextNumberLiteral in that case
	private Token nextSymbolOrComment() throws IndexOutOfBoundsException{
	}
	
	//TODO - Luca
	private Token nextStringOrCharLiteral() throws IndexOutOfBoundsException{
	}


	public void reset() {
		position = 0;
		tokenStart = 0;
	}
}
