package wci.frontend.java;

public class Tokenizer {
	private String source;
	private int position;

	public Tokenizer(String source) {
		this.source = source + '\n';
		position = 0;
	}

	private boolean isSymbol(char c) {
		return "(){}[];,.:+-*/|&^%<>!~?=".contains(String.valueOf(c));
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
		String keywords = " abstract continue for new switch assert default "+
		" if package synchronized boolean do goto private this break double "+
		" implements protected throw byte else import public throws case enum "+
		" instanceof return transient catch extends int short try char final "+
		" interface static void class finally long strictfp volatile const float "+
		" native super while ";
		return keywords.contains(" " + str + " ");
	}

	private long packLineAndCols(){
		return packLineAndCols(position);
	}

	private long packLineAndCols(int pos){
		int numNewLines = 0;
		int posInLine = 0;
		/*  Because we have to explain this.
			The upper 32 bits of ret is the line number and the lower 32 bits is
			the column index.
		*/
		long ret = 0;
		for (int i = 0; i < pos; i ++) {
			if (this.source.charAt(i) == '\n'){
				numNewLines ++;
				posInLine = 0;
			} else {
				posInLine++;
			}
		}
		ret = ((numNewLines + 1L) << 32) | posInLine;
		return ret;
	}

	public static long getLine(long lac) {
		return lac >> 32;
	}

	public static long getCol(long lac) {
		return ((lac << 32) >> 32);
	}

	public static void printLineAndCol(long lac) {
		System.out.println("Line: " + (lac >> 32) + "\t Col: " + ((lac << 32) >> 32));
	}

	public Token nextToken() {
		char c;
		try {
			while(Character.isWhitespace(c = source.charAt(position))) {
				position++;
			}

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
		} catch (UnclosedCommentException e) {
			System.out.println(e);
		}
		return null;
	}

	//TODO - Sean
	private Token nextNumberLiteral() {
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
				position--;
				return new Token(source.substring(start, position), type);
			}
		}
	}


	//TODO - Matt
	private Token nextIdentOrKeyword() {
		String token = "";
		while(!Character.isWhitespace(source.charAt(position))) {
			if (isIdentifierChar(source.charAt(position))){
				token += source.charAt(position);
				position ++;
			} else {
				break;
			}
		}

		if (isKeyword(token)) {
			printLineAndCol(packLineAndCols(position));
			return new Token(token, Token.TokenType.keyword);
		} else if (" true false ".contains(" "+token+" ")) {
			return new Token(token, Token.TokenType.booleanLiteral);
		} else if (" null ".contains(" "+token+" ")) {
			return new Token(token, Token.TokenType.nullLiteral);
		} else {
			printLineAndCol(packLineAndCols(position));
			return new Token(token, Token.TokenType.identifier);
		}
	}

	// TODO - Luca
	private Token nextStringOrCharLiteral(char c) {
		if(c == '\'') {
			String text = String.valueOf(getNextRealChar(source.charAt(++position)));
			if(source.charAt(++position) == '\'')
			{
				position++;
				return new Token(text, Token.TokenType.characterLiteral);
			}
		}
		else if(c == '"') {
			String text = "";
			while(true) {
				char ch = source.charAt(++position);
				if(ch != '"') {
					text = text + getNextRealChar(ch);
				}
				else {
					position++;
					return new Token(text, Token.TokenType.stringLiteral);
				}
			}
		}

		return null;
	}

	private char getNextRealChar(char c) {
		if(c == '\\') {
			switch(source.charAt(++position)) {
				case 't':
					return (char)9;

				case 'n':
					return (char)10;

				case 'r':
					return (char)13;

				case '\\':
					return '\\';

				case '\'':
					return '\'';

				case '"':
					return '"';

				default:
					return '?';
			}
		}

		return c;
	}

	private void consumeBlockComment() throws UnclosedCommentException{
		try {
			while(true) {
				char c = source.charAt(position++);
				if (c == '*' && source.charAt(position) == '/') {
					position++;
					return;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			throw new UnclosedCommentException();
		}
	}

	private void consumeLineComment() {
		while(source.charAt(position++) != '\n');
	}

	//Remember, if the token is a '.', it may be the start of a floating point literal
	//this method should call nextNumberLiteral in that case
	//also, this method handles comments, in which case it consumes the comment and returns
	//the next token after that comment.
	private Token nextSymbolOrComment() throws UnclosedCommentException {
		char c = source.charAt(position), c2, c3, c4;
		switch (c) {
			//first, all of our cases that are guaranteed to be single-character
			//separators
			case '(':
			case ')':
			case '{':
			case '}':
			case '[':
			case ']':
			case ';':
			case ',':
				position++;
				return new Token(c + "", Token.TokenType.separator);
			case ':':
			case '?':
			case '~':
				position++;
				return new Token(c + "", Token.TokenType.operator);
			//now for the trickier cases
			case '.' :
				if (isDigit(source.charAt(position + 1))) {
					return nextNumberLiteral();
				} else {
					position++;
					return new Token(".", Token.TokenType.separator);
				}

			case '>':
			case '<':
				c2 = source.charAt(++position);
				if (c == c2) {
					c3 = source.charAt(++position);
					if (c3 == '=') {
						position++;
						return new Token(source.substring(position - 3, position), Token.TokenType.operator);
					} else if (c == '>' && c3 == '>') {
						c4 = source.charAt(++position);
						if (c4 == '=') {
							position++;
							return new Token(">>>=", Token.TokenType.operator);
						} else {
							return new Token(">>>", Token.TokenType.operator);
						}
					} else {
						return new Token(source.substring(position - 2, position), Token.TokenType.operator);
					}
				} else if (c2 == '=') {
					position++;
					return new Token(source.substring(position - 2, position), Token.TokenType.operator);
				} else {
					return new Token(c + "", Token.TokenType.operator);
				}

			case '/':
				c2 = source.charAt(++position);
				if (c2 == '*') {
					position++;
					consumeBlockComment();
					return nextToken();
				} else if (c2 == '/') {
					consumeLineComment();
					return nextToken();
				} else position--;
				//intentional fall through here.
			//these are all the operators that can either stand alone or be followed by a '='
			case '*':
			case '!':
			case '^':
			case '%':
			case '=':
				c2 = source.charAt(++position);
				if (c2 == '=') {
					position++;
					return new Token(c + "=", Token.TokenType.operator);
				} else {
					return new Token(c + "", Token.TokenType.operator);
				}
			//these are all the operators that can either stand alone, be reduplicated, or be followed by a '='
			case '+':
			case '-':
			case '&':
			case '|':
				c2 = source.charAt(++position);
				if (c2 == c) {
					position++;
					return new Token(c + "" + c2, Token.TokenType.operator);
				} else if (c2 == '=') {
					position++;
					return new Token(c + "=", Token.TokenType.operator);
				} else {
					return new Token(c + "", Token.TokenType.operator);
				}
		}
		return null;
	}

	public void reset() {
		position = 0;
	}
}
