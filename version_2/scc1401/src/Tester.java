import retree.expression.AddExpression;
import retree.expression.AssignmentExpression;
import retree.expression.ConstantExpression;
import retree.expression.VariableExpression;
import retree.regalloc.RegisterAllocator;
import retree.statement.BlockStatement;
import retree.statement.ExpressionStatement;
import retree.statement.Statement;
import retree.type.Type;

import java.util.ArrayList;

/**
 * Created by mplv on 7/29/15.
 */
public class Tester {
    public static void main(String[] args) throws Exception {
        RegisterAllocator registerAllocator = new RegisterAllocator(16);
        VariableExpression tmp1 = new VariableExpression(Type.intType,0,false,false, "i");
        VariableExpression tmp2 = new VariableExpression(Type.intType,0,false,false, "j");
        Statement s1 = new ExpressionStatement(
                new AssignmentExpression(
                        tmp1,
                        new ConstantExpression(Type.intType, 10)
                )
        );
        Statement s2 = new ExpressionStatement(
                new AssignmentExpression(
                        tmp2,
                        new AddExpression(
                                new AddExpression(
                                        new ConstantExpression(Type.intType,16),
                                        tmp1
                                ),
                                        new ConstantExpression(Type.intType, 6)
                        )
                )
        );

        BlockStatement blockStatement = new BlockStatement("","",null);
        ArrayList<Statement> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        blockStatement.setStatements(list);

        registerAllocator.calculatePlacements(blockStatement);
        for (Statement s : blockStatement.getStatements())
        {
            registerAllocator.linearScanRegisterAllocation(s.expressionList);
        }
        System.out.println();
    }
}
