package retree.intermediate;

import java.util.*;

public class Instruction{
    private boolean comment;
    private String label;
    private String mnemonic;
    private String[] operands;
    private int numOperands;
    public Instruction(String label,String mnemonic, String ... args)
    {
        this.label = label;
        this.mnemonic = mnemonic;
        if (mnemonic.contains("*"))
        {
            comment = true;
        }
        operands = new String[args.length];
        for (int i = 0; i < args.length; i++)
        {
            operands[i] = args[i];
        }
    }

    public boolean isComment()
    {
        return this.comment;
    }

    public String getLabel()
    {
        return this.label;
    }

    public void setLabel(String lbl)
    {
        this.label = lbl;
    }

    public String getMnemonic()
    {
        return this.mnemonic;
    }

    public void setOperand(String opr, int place)
    {
        operands[place] = opr;
    }

    public String getOperand(int place)
    {
        return operands[place];
    }

    public int getOperandListSize()
    {
        return operands.length;
    }

    public String generateCode()
    {
        if (comment)
        {
            return mnemonic + "\n";
        }
        String line = "     ";
		line += label;

		while(line.length() < 15)
		{
			line += " ";
		}
        if (mnemonic.length() == 0)
        {
            return "";
        }
        line += mnemonic;

		while (line.length() < 20)
		{
			line += " ";
		}
        String ops = "";
        for (int i = 0 ; i < operands.length; i++)
        {
            if (operands[i] != null)
            {
                    line += operands[i]+",";
            }
        }
        return line.substring(0,line.length()-1) + "\n" ;
    }

    @Override
    public String toString()
    {
        String op = "";
        for(int i = 0 ; i < operands.length; i++)
        {
            op += " op"+i +": "+ operands[i] + ",";
        }
        return "lbl: " + label + ", mnemonic: " + mnemonic + (op.length() > 0 ? op.substring(0,op.length() - 1):"") +", is comment: " + comment;
    }
}
