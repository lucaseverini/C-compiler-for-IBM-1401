/*
	SwitchStatement.java

    The Small-C cross-compiler for IBM 1401

	April-17-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree.statement;

import compiler.SmallCC;
import java.util.ArrayList;
import java.util.List;
import static retree.RetreeUtils.*;
import retree.expression.ConstantExpression;
import retree.expression.Expression;
import retree.type.PointerType;
import retree.type.Type;

public class SwitchStatement implements Statement
{
	private final Expression expression;
	private final BlockStatement body;
	private final String bottomLabel;
	
	public SwitchStatement(Expression expression, Statement block) throws Exception
	{
		this.expression = expression;
		
		if(block instanceof BlockStatement)
		{
			this.body = (BlockStatement)block;
		}
		else
		{
			throw new Exception("Switch statement without block"); 
		}
		
		this.bottomLabel = label(SmallCC.nextLabelNumber());
	}
	
	@Override
	public String generateCode() throws Exception
	{
		String code = "\r";
		
		List<CaseStatement> cases = new ArrayList<>();
		CaseStatement defaultCase = null;
		
		for(Statement stat : body.getStatements())
		{
			if(stat instanceof CaseStatement)
			{
				CaseStatement caseStat = (CaseStatement)stat;
				if(caseStat.isDefault())
				{
					defaultCase = caseStat;
				}
				else
				{
					cases.add(caseStat);
				}
			}
		}

		code += COM("Switch " + this.toString());
		
		code += expression.generateCode(true);

		if(!cases.isEmpty())
		{	
			if (expression.getType().equals(Type.charType))
			{
				code += SNIP("char_to_number");
			}
			else if(expression.getType() instanceof PointerType)
			{
				code += SNIP("pointer_to_number");
			}
					
			int size = Type.intType.sizeof();

			String nextCaseLabel = null;
			for(CaseStatement aCase : cases)
			{			
				Expression caseExp = aCase.getExpression();
				
				if(!(caseExp instanceof ConstantExpression))
				{
					throw new Exception("Case expression not constant");
				}
				
				if(nextCaseLabel != null)
				{
					code += INS("Compare to Case: " + caseExp, nextCaseLabel, "NOP");
				}
				
				code += caseExp.generateCode(true);

				code += INS("Compare stack to stack at " + -size, null, "C", STACK_OFF(0), STACK_OFF(-size));
			
				code += POP(size);
				
				nextCaseLabel = label(SmallCC.nextLabelNumber());
				
				code += INS("If different jump to next Case compare", null, "BU", nextCaseLabel);
				
				code += POP(size);
				
				code += INS("Jump to Case: " + caseExp, null, "B", aCase.getLabel());
			}
			
			code += INS("End of Switch Cases", nextCaseLabel, "NOP");
			code += POP(size);
			
			if(defaultCase != null)
			{
				code += INS("Jump to Default", null, "B", defaultCase.getLabel());
			}
			else
			{
				code += INS("Jump to End of Switch block", null, "B", bottomLabel);
			}
						
			code += body.generateCode();
		}
		
		code += INS("Last switch block instruction", bottomLabel, "NOP");
		
		code += COM("End Switch (" + expression + ")");
		code += "\r";
		
		return code;
	}

	Statement getBody()
	{
		return body;
	}

	String getBottomLabel()
	{
		return bottomLabel;
	}

	@Override
	public String toString()
    {
        return "[switch (" + expression + ")]";
    }

	public Expression getExpression() {
		return expression;
	}
}
