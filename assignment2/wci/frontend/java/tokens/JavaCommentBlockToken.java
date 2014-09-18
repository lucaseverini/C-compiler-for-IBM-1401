package wci.frontend.java.tokens;

import wci.frontend.*;
import wci.frontend.java.*;

import static wci.frontend.Source.EOF;
import static wci.frontend.java.JavaErrorCode.*;
import static wci.frontend.java.JavaTokenType.*;

/**
 * <h1>JavaCommentBlockToken</h1>
 */
public class JavaCommentBlockToken extends JavaToken
{
    /**
     * Constructor.
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public JavaCommentBlockToken(Source source) throws Exception
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

        textBuffer.append("/*");

        // Get all comment
		char currentChar = nextChar();
				
        while(currentChar != '*' && peekChar() != '/')
		{
			if(currentChar == EOF)
			{
				type = ERROR;
				value = MISSING_END_COMMENT_BLOCK;
				text = textBuffer.toString();
				return;
			}

			textBuffer.append(currentChar);
			valueBuffer.append(currentChar);
 			currentChar = nextChar();
		}
		
		textBuffer.append("*/");
		nextChar();
		nextChar();

        type = COMMENT;
        value = valueBuffer.toString();
   
        text = textBuffer.toString();
    }
}
