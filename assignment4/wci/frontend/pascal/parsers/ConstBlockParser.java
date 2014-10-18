package wci.frontend.pascal.parsers;

import wci.frontend.Token;
import wci.frontend.Parser;
import wci.frontend.pascal.PascalParserTD;
import wci.backend.interpreter.executors.ExpressionExecutor;

import static wci.frontend.pascal.PascalTokenType.*;
import static wci.frontend.pascal.PascalErrorCode.*;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;

import wci.intermediate.*;


public class ConstBlockParser extends PascalParserTD {
	public ConstBlockParser(PascalParserTD parent)
    {
        super(parent);
    }
    
    //parses a const block, and returns the token immediately succeeding it
    public Token parse(Token token) throws Exception {
			if (token.getType() == CONST) token = nextToken();
			while (token.getType() == IDENTIFIER) {
				SymTabEntry entry = Parser.symTabStack.getLocalSymTab().enter(token.getText());
				entry.appendLineNumber(token.getLineNumber());
				token = nextToken();
				if (token.getType() != EQUALS) errorHandler.flag(token, UNEXPECTED_TOKEN, this);
				token = nextToken();
				ExpressionParser expressionParser = new ExpressionParser(this);
				ICodeNode expression = expressionParser.parse(token);
				ExpressionExecutor executor = new ExpressionExecutor(null);
				Object value = executor.execute(expression);
				entry.setAttribute(CONSTANT_VALUE, value);
				System.out.println(value);
				token = nextToken();
			}
			return token;
		}
}
