package wci.frontend.pascal.parsers;

import java.util.ArrayList;
import java.util.EnumSet;

import wci.frontend.*;
import wci.frontend.pascal.*;
import wci.intermediate.*;

import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.frontend.pascal.parsers.ConstantDefinitionsParser.CONSTANT_START_SET;
import wci.intermediate.symtabimpl.DefinitionImpl;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;
import static wci.intermediate.symtabimpl.DefinitionImpl.*;
import static wci.intermediate.typeimpl.TypeFormImpl.*;
import static wci.intermediate.typeimpl.TypeKeyImpl.*;

/**
 * <h1>VariableDeclarationsParser</h1>
 *
 * <p>Parse Pascal variable declarations.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class VariableDeclarationsParser extends DeclarationsParser
{
    private Definition definition;  // how to define the identifier

    /**
     * Constructor.
     * @param parent the parent parser.
     */
    public VariableDeclarationsParser(PascalParserTD parent)
    {
        super(parent);
    }

    /**
     * Setter.
     * @param definition the definition to set.
     */
    protected void setDefinition(Definition definition)
    {
        this.definition = definition;
    }

    // Synchronization set for a variable identifier.
    static final EnumSet<PascalTokenType> IDENTIFIER_SET = DeclarationsParser.VAR_START_SET.clone();
    static {
        IDENTIFIER_SET.add(IDENTIFIER);
        IDENTIFIER_SET.add(END);
        IDENTIFIER_SET.add(SEMICOLON);
    }

    // Synchronization set for the start of the next definition or declaration.
    static final EnumSet<PascalTokenType> NEXT_START_SET = DeclarationsParser.ROUTINE_START_SET.clone();
    static {
        NEXT_START_SET.add(IDENTIFIER);
        NEXT_START_SET.add(SEMICOLON);
    }

    /**
     * Parse variable declarations.
     * @param token the initial token.
     * @throws Exception if an error occurred.
     */
	@Override
    public void parse(Token token) throws Exception
    {
        token = synchronize(IDENTIFIER_SET);

        // Loop to parse a sequence of variable declarations
        // separated by semicolons.
        while (token.getType() == IDENTIFIER) 
		{
            // Parse the identifier sublist and its type specification.
            ArrayList<SymTabEntry> vars = parseIdentifierSublist(token);
			if(vars.isEmpty())
			{
				break;
			}
			
			SymTabEntry var = vars.get(0);
				
            token = currentToken();
            TokenType tokenType = token.getType();

			// Look for a possible assignment after a definition.
            if (tokenType == EQUALS) 
			{
				token = nextToken();  // consume the =
				
				// Parse the constant value.
				Token constantToken = token;
				Object value = parseConstant(token);
				// System.out.println("VAR value: " + value.toString());
				var.setAttribute(DATA_VALUE, value);
				
				token = currentToken();
				tokenType = token.getType();
			}
 
            // Look for one or more semicolons.
            if (tokenType == SEMICOLON) 
			{
                while (token.getType() == SEMICOLON) 
				{
                    token = nextToken();  // consume the ;
                }
            }
            // If at the start of the next definition or declaration, then missing a semicolon.
            else if (NEXT_START_SET.contains(tokenType)) {
                errorHandler.flag(token, MISSING_SEMICOLON, this);
            }

            token = synchronize(IDENTIFIER_SET);
        }
    }

    // Synchronization set to start a sublist identifier.
    static final EnumSet<PascalTokenType> IDENTIFIER_START_SET = EnumSet.of(IDENTIFIER, COMMA);

    // Synchronization set to follow a sublist identifier.
    private static final EnumSet<PascalTokenType> IDENTIFIER_FOLLOW_SET = EnumSet.of(COLON, SEMICOLON);
    static {
        IDENTIFIER_FOLLOW_SET.addAll(DeclarationsParser.VAR_START_SET);
    }

    // Synchronization set for the , token.
    private static final EnumSet<PascalTokenType> COMMA_SET = EnumSet.of(COMMA, COLON, IDENTIFIER, SEMICOLON);

    /**
     * Parse a sublist of identifiers and their type specification.
     * @param token the current token.
     * @return the sublist of identifiers in a declaration.
     * @throws Exception if an error occurred.
     */
    protected ArrayList<SymTabEntry> parseIdentifierSublist(Token token) throws Exception
    {
        ArrayList<SymTabEntry> sublist = new ArrayList<>();

        do {
            token = synchronize(IDENTIFIER_START_SET);
            SymTabEntry id = parseIdentifier(token);

            if (id != null) {
                sublist.add(id);
            }

            token = synchronize(COMMA_SET);
            TokenType tokenType = token.getType();

            // Look for the comma.
            if (tokenType == COMMA) {
                token = nextToken();  // consume the comma

                if (IDENTIFIER_FOLLOW_SET.contains(token.getType())) {
                    errorHandler.flag(token, MISSING_IDENTIFIER, this);
                }
            }
            else if (IDENTIFIER_START_SET.contains(tokenType)) {
                errorHandler.flag(token, MISSING_COMMA, this);
            }
        } while (!IDENTIFIER_FOLLOW_SET.contains(token.getType()));

        // Parse the type specification.
        TypeSpec type = parseTypeSpec(token);

        // Assign the type specification to each identifier in the list.
        for (SymTabEntry variableId : sublist) {
            variableId.setTypeSpec(type);
        }

        return sublist;
    }

    /**
     * Parse an identifier.
     * @param token the current token.
     * @return the symbol table entry of the identifier.
     * @throws Exception if an error occurred.
     */
    private SymTabEntry parseIdentifier(Token token) throws Exception
    {
        SymTabEntry id = null;

        if (token.getType() == IDENTIFIER) {
            String name = token.getText().toLowerCase();
            id = symTabStack.lookupLocal(name);

            // Enter a new identifier into the symbol table.
            if (id == null) {
                id = symTabStack.enterLocal(name);
                id.setDefinition(definition);
                id.appendLineNumber(token.getLineNumber());
            }
            else {
                errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
            }

            token = nextToken();   // consume the identifier token
        }
        else {
            errorHandler.flag(token, MISSING_IDENTIFIER, this);
        }

        return id;
    }

    // Synchronization set for the : token.
    private static final EnumSet<PascalTokenType> COLON_SET = EnumSet.of(COLON, SEMICOLON);

    /**
     * Parse the type specification.
     * @param token the current token.
     * @return the type specification.
     * @throws Exception if an error occurs.
     */
    protected TypeSpec parseTypeSpec(Token token) throws Exception
    {
        // Synchronize on the : token.
        token = synchronize(COLON_SET);
        if (token.getType() == COLON) {
            token = nextToken(); // consume the :
        }
        else {
            errorHandler.flag(token, MISSING_COLON, this);
        }

        // Parse the type specification.
        TypeSpecificationParser typeSpecificationParser =
            new TypeSpecificationParser(this);
        TypeSpec type = typeSpecificationParser.parse(token);

        return type;
    }

   /**
     * Parse a constant value.
     * @param token the current token.
     * @return the constant value.
     * @throws Exception if an error occurred.
     */
    protected Object parseConstant(Token token) throws Exception
    {
        TokenType sign = null;

        // Synchronize at the start of a constant.
        token = synchronize(CONSTANT_START_SET);
        TokenType tokenType = token.getType();

        // Plus or minus sign?
        if ((tokenType == PLUS) || (tokenType == MINUS)) {
            sign = tokenType;
            token = nextToken();  // consume sign
        }

        // Parse the constant.
        switch ((PascalTokenType) token.getType()) {

            case IDENTIFIER: {
                return parseIdentifierConstant(token, sign);
            }

            case INTEGER: {
                Integer value = (Integer) token.getValue();
                nextToken();  // consume the number
                return sign == MINUS ? -value : value;
            }

            case REAL: {
                Float value = (Float) token.getValue();
                nextToken();  // consume the number
                return sign == MINUS ? -value : value;
            }

            case STRING: {
                if (sign != null) {
                    errorHandler.flag(token, INVALID_CONSTANT, this);
                }

                nextToken();  // consume the string
                return (String) token.getValue();
            }

            default: {
                errorHandler.flag(token, INVALID_CONSTANT, this);
                return null;
            }
        }
    }

   /**
     * Parse an identifier constant.
     * @param token the current token.
     * @param sign the sign, if any.
     * @return the constant value.
     * @throws Exception if an error occurred.
     */
    protected Object parseIdentifierConstant(Token token, TokenType sign) throws Exception
    {
        String name = token.getText().toLowerCase();
        SymTabEntry id = symTabStack.lookup(name);

        nextToken();  // consume the identifier

        // The identifier must have already been defined
        // as an constant identifier.
        if (id == null) {
            errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
            return null;
        }

        Definition definition = id.getDefinition();

        if (definition == CONSTANT) {
            Object value = id.getAttribute(CONSTANT_VALUE);
            id.appendLineNumber(token.getLineNumber());

            if (value instanceof Integer) {
                return sign == MINUS ? -((Integer) value) : value;
            }
            else if (value instanceof Float) {
                return sign == MINUS ? -((Float) value) : value;
            }
            else if (value instanceof String) {
                if (sign != null) {
                    errorHandler.flag(token, INVALID_CONSTANT, this);
                }

                return value;
            }
            else {
                return null;
            }
        }
        else if (definition == ENUMERATION_CONSTANT) {
            Object value = id.getAttribute(CONSTANT_VALUE);
            id.appendLineNumber(token.getLineNumber());

            if (sign != null) {
                errorHandler.flag(token, INVALID_CONSTANT, this);
            }

            return value;
        }
        else if (definition == null) {
            errorHandler.flag(token, NOT_CONSTANT_IDENTIFIER, this);
            return null;
        }
		else if (definition == DefinitionImpl.TYPE) {
            Object value = id.getAttribute(CONSTANT_VALUE);
            return value;
		}
        else {
            errorHandler.flag(token, INVALID_CONSTANT, this);
            return null;
        }
    }
}
