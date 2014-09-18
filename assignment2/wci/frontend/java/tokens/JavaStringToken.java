package wci.frontend.java.tokens;

import wci.frontend.*;
import wci.frontend.java.*;

import static wci.frontend.Source.EOL;
import static wci.frontend.Source.EOF;
import static wci.frontend.java.JavaTokenType.*;
import static wci.frontend.java.JavaErrorCode.*;

/**
 * <h1>PascalStringToken</h1>
 *
 * <p> Pascal string tokens.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class JavaStringToken extends JavaToken
{
    /**
     * Constructor.
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public JavaStringToken(Source source)
        throws Exception
    {
        super(source);
    }

    /**
     * Extract a Pascal string token from the source.
     * @throws Exception if an error occurred.
     */
	@Override
    protected void extract() throws Exception
    {
        StringBuilder textBuffer = new StringBuilder();
        StringBuilder valueBuffer = new StringBuilder();

        char currentChar = nextChar();  // consume initial quote
        textBuffer.append('\"');

        // Get string characters.
        do {
            // Replace any whitespace character with a blank.
            if (Character.isWhitespace(currentChar)) {
                currentChar = ' ';
            }

            if ((currentChar != '\"') && (currentChar != EOF)) {
                textBuffer.append(currentChar);
                valueBuffer.append(currentChar);
                currentChar = nextChar();  // consume character
            }

            // Escape
            if (currentChar == '\\') 
			{
				currentChar = nextChar();
				switch(currentChar)
				{
					case 't':
						textBuffer.append((char)9);
						break;
						
					case 'n':
						textBuffer.append((char)10);
						break;
						
					case 'r':
						textBuffer.append((char)13);
						break;
						
					case '\"':
						textBuffer.append('\"');
						break;

					case '\\':
						textBuffer.append('\\');
						break;
						
					default:
						type = ERROR;
						value = INVALID_CHARACTER;
						text = textBuffer.toString();
						return;
				}	
				
				currentChar = nextChar();
			}
        } while ((currentChar != '\"') && (currentChar != EOF));

        if (currentChar == '\"') {
            nextChar();  // consume final quote
            textBuffer.append('\"');

            type = STRING;
            value = valueBuffer.toString();
        }
        else {
            type = ERROR;
            value = UNEXPECTED_EOF;
        }

        text = textBuffer.toString();
    }
}
