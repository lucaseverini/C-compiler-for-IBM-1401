/**
 * <h1>PascalParserTD</h1>
 *
 * <p>The top-down Pascal parser.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */

package wci.frontend.pascal;

import java.util.EnumSet;
import wci.frontend.*;
import wci.frontend.pascal.parsers.*;
import wci.intermediate.*;
import wci.message.*;
import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.message.MessageType.PARSER_SUMMARY;

public class PascalParserTD extends Parser
{
    protected static PascalErrorHandler errorHandler = new PascalErrorHandler();

    /**
     * Constructor.
     * @param scanner the scanner to be used with this parser.
     */
    public PascalParserTD(Scanner scanner)
    {
        super(scanner);
    }

    /**
     * Constructor for subclasses.
     * @param parent the parent parser.
     */
    public PascalParserTD(PascalParserTD parent)
    {
        super(parent.getScanner());
    }

    /**
     * Getter.
     * @return the error handler.
     */
    public PascalErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    /**
     * Parse a Pascal source program and generate the symbol table
     * and the intermediate code.
     * @throws Exception if an error occurred.
     */
    public void parse()
        throws Exception
    {
        long startTime = System.currentTimeMillis();
        iCode = ICodeFactory.createICode();
        
        try {
					/*
<<<<<<< HEAD
					Token token;
					ICodeNode rootNode = null;
					token = nextToken();
					do {            

            if (token.getType() == BEGIN && rootNode == null) {
                StatementParser statementParser = new StatementParser(this);
                rootNode = statementParser.parse(token);
                token = currentToken();
            } else if (token.getType() == CONST) {
							ConstBlockParser constBlockParser = new ConstBlockParser(this);
							token = constBlockParser.parse(token);
						} else {
                errorHandler.flag(token, UNEXPECTED_TOKEN, this);
                token = nextToken();
            }

					} while (token.getType() != END_OF_FILE && token.getType() != DOT);
					// Look for the final period.
					if (token.getType() != DOT) {
							errorHandler.flag(token, MISSING_PERIOD, this);
					}
					
					// Set the parse tree root node.
					if (rootNode != null) {
							iCode.setRoot(rootNode);
					}

					// Send the parser summary message.
					float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
					sendMessage(new Message(PARSER_SUMMARY,
																	new Number[] {token.getLineNumber(),
																								getErrorCount(),
																								elapsedTime}));
				
				
=======
*/
				Token token = nextToken();
				ICodeNode rootNode = null;

				do {
				if (token.getType() == BEGIN) {
					StatementParser statementParser = new StatementParser(this);
					rootNode = statementParser.parse(token);
					token = currentToken();
				} 
				else if (token.getType() == CONST) {
					DeclarationsParser declarParser = new DeclarationsParser(this);
					declarParser.parse(token);
					token = currentToken();
				}
				else
				{
					errorHandler.flag(token, UNEXPECTED_TOKEN, this);
				}
				
				if (token.getType() == DOT) 
				{
					break;
				}
 			}
			while(true);
 
            // Set the parse tree root node.
            if (rootNode != null) {
                iCode.setRoot(rootNode);
            }

            // Send the parser summary message.
            float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
            sendMessage(new Message(PARSER_SUMMARY,
                                    new Number[] {token.getLineNumber(),
                                                  getErrorCount(),
                                                  elapsedTime}));
//>>>>>>> 87edcfc1387361235ba3c759698d3038c42e1bab
        }
        catch (java.io.IOException ex) {
            errorHandler.abortTranslation(IO_ERROR, this);
        }
    }

    /**
     * Return the number of syntax errors found by the parser.
     * @return the error count.
     */
    public int getErrorCount()
    {
        return errorHandler.getErrorCount();
    }

    /**
     * Synchronize the parser.
     * @param syncSet the set of token types for synchronizing the parser.
     * @return the token where the parser has synchronized.
     * @throws Exception if an error occurred.
     */
    public Token synchronize(EnumSet syncSet)
        throws Exception
    {
        Token token = currentToken();

        // If the current token is not in the synchronization set,
        // then it is unexpected and the parser must recover.
        if (!syncSet.contains(token.getType())) {

            // Flag the unexpected token.
            errorHandler.flag(token, UNEXPECTED_TOKEN, this);

            // Recover by skipping tokens that are not
            // in the synchronization set.
            do {
                token = nextToken();
            } while (!(token instanceof EofToken) &&
                     !syncSet.contains(token.getType()));
       }

       return token;
    }
}
