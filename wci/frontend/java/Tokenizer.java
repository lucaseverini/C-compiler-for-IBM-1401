package wci.frontend.java;

public class Tokenizer {
	private String source;
	private int position;

	public Tokenizer(String source) {
		this.source = source + '\n';
		position = 0;
	}

	private boolean isSymbol(char c) {
		return "(){}[];,.:+-*/|&^%<>!~?".contains(String.valueOf(c));
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
			while(Character.isWhitespace(c = source.charAt(position++)));

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
				return nextStringOrCharLiteral(c);
			}
		} catch (IndexOutOfBoundsException e) {
			position = source.length();
			return new Token("", Token.TokenType.eof);
		}
		
		return null;
	}
	
	//TODO - Sean
	private Token nextNumberLiteral() throws IndexOutOfBoundsException {
		int start = position;
		boolean hexidecimal = false;
		boolean expectingExponent = false;
		Token.TokenType type = Token.TokenType.integerLiteral;
		char c;
		while (true) {
			c = source.charAt(position++);
			if (c == 'x' || c == 'X') {
				hexidecimal  = true;
			}
			else if (c == '.') {
				type = Token.TokenType.floatingPointLiteral;
			}
			else if ((c == 'e' || c == 'E') && ! hexidecimal) {
				type = Token.TokenType.floatingPointLiteral;
				expectingExponent = true;
			}
			else if (c == 'p' || c == 'P') {
				type = Token.TokenType.floatingPointLiteral;
				expectingExponent = true;
			} else if (expectingExponent && c == '+' || c == '-') {
			} else if (!isIdentifierChar(c)) {
				return new Token(source.substring(start, position), type);
			}
		}
	}
	
	//TODO - Matt
	private Token nextIdentOrKeyword() throws IndexOutOfBoundsException {
		return null;
	}

	// Luca
	private Token nextStringOrCharLiteral(char c) throws IndexOutOfBoundsException {
		if(c == '\'') {
			String text = String.valueOf(source.charAt(++position));
			if(source.charAt(++position) == '\'')
			{
				return new Token(text, Token.TokenType.characterLiteral);
			}
		}
		else {
			String text = "";
			
			while(true) {
				char ch = source.charAt(++position);
				if(ch != '\"') {
					text = text + ch;
				}
				else {
					return new Token(text, Token.TokenType.stringLiteral);
				}
			}
		}
		
		throw new IndexOutOfBoundsException();
	}

	//Remember, if the token is a '.', it may be the start of a floating point literal
	//this method should call nextNumberLiteral in that case
	private Token nextSymbolOrComment() throws IndexOutOfBoundsException {
		return null;
	}
	
	public void reset() {
		position = 0;
	}
}
