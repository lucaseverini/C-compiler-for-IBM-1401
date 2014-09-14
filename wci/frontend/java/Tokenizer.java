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
		return "(){}[];,.:+-*/|&^%<>!~?".contains(c);
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
				return nextStringOrCharLiteral(c);
			}
		} catch (IndexOutOfBoundsException e) {
			position = source.length();
			tokenStart = position;
			return new Token("", Token.TokenType.eof);
		}
		
		return null;
	}
/*	
	//TODO - Sean
	private Token nextNumberLiteral() throws IndexOutOfBoundsException {
		
	}	
	
	//TODO - Matt
	private Token nextIdentOrKeyword() throws IndexOutOfBoundsException;
	
	//TODO - someone
	private Token nextSymbolOrComment() throws IndexOutOfBoundsException;
*/	
	//TODO - Luca
	private Token nextStringOrCharLiteral(char c) throws IndexOutOfBoundsException
	{
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

	public void reset() {
		position = 0;
		tokenStart = 0;
	}
}
