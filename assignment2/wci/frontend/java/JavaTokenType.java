package wci.frontend.java;

import java.util.Hashtable;
import java.util.HashSet;

import wci.frontend.TokenType;

/**
 * <h1>JavaTokenType</h1>
 *
 * <p>Pascal token types.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public enum JavaTokenType implements TokenType
{
    // Reserved words.
    SWITCH("switch"), CONST("const"), DO("do"), ELSE("else"), END("end"),
    FOR("for"), GOTO("goto"), IF("if"), IN("in"), NULL("null"),
    BREAK("break"), TO("to"), WHILE("while"), CONTINUE("continue"),

    // Special symbols.
    PLUS_PLUS("++"), PLUS("+"), MINUS("-"), MINUS_MINUS("--"), MULTIPLY("*"), ASSIGN("="),
    DOT("."), COMMA(","), SEMICOLON(";"), COLON(":"), QUOTE("'"), NOT("!"), MOD("%"), AND("&"), OR("|"), 
    EQUALS("=="), NOT_EQUALS("!="), LESS_THAN("<"), LESS_EQUALS("<="), DIVIDE("/"), AND_AND("&&"), OR_OR("||"),
    GREATER_EQUALS(">="), GREATER_THAN(">"), LEFT_PAREN("("), RIGHT_PAREN(")"), 
	MINUS_ASSIGN("-="), PLUS_ASSIGN("+="), MULTIPLY_ASSIGN("*="), DIVIDE_ASSIGN("/="),
    LEFT_BRACKET("["), RIGHT_BRACKET("]"), LEFT_BRACE("{"), RIGHT_BRACE("}"),
    UP_ARROW("^"),

    IDENTIFIER, INTEGER, REAL, STRING, COMMENT,
    ERROR, END_OF_FILE;

    private static final int FIRST_RESERVED_INDEX = SWITCH.ordinal();
    private static final int LAST_RESERVED_INDEX  = CONTINUE.ordinal();

    private static final int FIRST_SPECIAL_INDEX = PLUS_PLUS.ordinal();
    private static final int LAST_SPECIAL_INDEX  = UP_ARROW.ordinal();

    private String text;  // token text

    /**
     * Constructor.
     */
    JavaTokenType()
    {
        this.text = this.toString().toLowerCase();
    }

    /**
     * Constructor.
     * @param text the token text.
     */
    JavaTokenType(String text)
    {
        this.text = text;
    }

    /**
     * Getter.
     * @return the token text.
     */
    public String getText()
    {
        return text;
    }

    // Set of lower-cased Pascal reserved word text strings.
    public static HashSet<String> RESERVED_WORDS = new HashSet<String>();
    static 
	{
        JavaTokenType values[] = JavaTokenType.values();
        for (int i = FIRST_RESERVED_INDEX; i <= LAST_RESERVED_INDEX; ++i) 
		{
            RESERVED_WORDS.add(values[i].getText().toLowerCase());
        }
    }

    // Hash table of Pascal special symbols.  Each special symbol's text
    // is the key to its Pascal token type.
    public static Hashtable<String, JavaTokenType> SPECIAL_SYMBOLS =
        new Hashtable<String, JavaTokenType>();
    static 
	{
        JavaTokenType values[] = JavaTokenType.values();
        for (int i = FIRST_SPECIAL_INDEX; i <= LAST_SPECIAL_INDEX; ++i) 
		{
            SPECIAL_SYMBOLS.put(values[i].getText(), values[i]);
        }
    }
}
