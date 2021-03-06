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
import wci.intermediate.typeimpl.TypeChecker;
import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.*;
import wci.intermediate.symtabimpl.Predefined;
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
		Predefined.initialize(symTabStack);

        try {
				Token token = nextToken();
				ICodeNode rootNode = null;

				do {
				if (token.getType() == BEGIN) {
					StatementParser statementParser = new StatementParser(this);
					rootNode = statementParser.parse(token);
					token = currentToken();
				}
				else if (token.getType() == CONST || token.getType() == TYPE || token.getType() == VAR) {
					DeclarationsParser declarParser = new DeclarationsParser(this);
					declarParser.parse(token);
					token = currentToken();
				}
				else if(token.getType() == DOT) {
					token = nextToken();
				}
				else
				{
					errorHandler.flag(token, UNEXPECTED_TOKEN, this);
				}
 			}
			while(!(token instanceof EofToken));

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


    private boolean CompareTypesHelper(ICodeNode parent, ICodeNode child)
    {
        boolean b = false;
        int ind = 0;
        TypeSpec t_parent = parent.getTypeSpec();
        while (child.getChildren().size() > ind)
        {
            TypeSpec t_child = child.getChildren().get(ind).getTypeSpec();
            if (t_child != null)
            {
                return TypeChecker.areAssignmentCompatible(t_parent,t_child);
            } else {
                ind += 1;
            }
        }

        return b;
    }


    /*
        Compares the parent to the child types returns true if they can be assigned
        returns false if they cannot be assigned
    */
    public boolean CompareTypes(ICodeNode i)
    {
        boolean b = false;
        int ind = 0;
        TypeSpec t_parent = i.getTypeSpec();
        while (i.getChildren().size() > ind)
        {
            TypeSpec t_child = i.getChildren().get(ind).getTypeSpec();
            if (t_child != null)
            {
                return TypeChecker.areAssignmentCompatible(t_parent,t_child);
            } else {
                if (i.getChildren().get(ind).getChildren().size() > 0 && i.getChildren().get(ind).getChildren().size() > ind) {
                    return CompareTypesHelper(i,i.getChildren().get(ind).getChildren().get(ind));
                }
            }
            ind += 1;
        }
        return b;
    }
}
