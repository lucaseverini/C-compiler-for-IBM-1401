/*
    Instruction.java

    The Small-C cross-compiler for IBM 1401

    April-9-2015

    By Matt Pleva, Luca Severini
*/

package retree.intermediate;

import java.util.*;

public class Instruction
{
    private boolean comment;
    private String label;
    private String mnemonic;
    private String[] operands;
    private int numOperands;

    public Instruction(){
        operands = new String[0];
    }
    
    public Instruction(String label, String mnemonic, String ... args)
    {
        this.label = label;
        this.mnemonic = mnemonic;
        
        if (mnemonic.contains("*"))
        {
            comment = true;
        }

        operands = args;
        numOperands = args.length;
        for(int i = 0 ; i < operands.length; i++)
        {
            if (operands[i].contains("* "))
            {
                numOperands --;
            }
        }
    }
    
    public int getSize()
    {
        if (this.mnemonic.contains("*"))
        {
            return 0;
        }
        if (this.mnemonic.contains("DC"))
        {
            if (this.operands[0].contains("@"))
            {
                return operands[0].substring(1,operands[0].length()-1).length();
            } else {
                return operands[0].length();
            }
        }
        if (this.mnemonic.contains("B"))
        {
            if (numOperands > 2)
            {
                return 1 + 3 * (numOperands-1) + 1;
            }
            return 1 + 3 * numOperands;
        }
        return 1 + 3 * numOperands;
    }
    
    public void setMnemonic(String m)
    {
        if (m.contains("*"))
        {
            comment = true;
        }
        this.mnemonic = m;
    }

    public void addArgument(String arg)
    {
        String[] newOperands = new String[this.operands.length + 1];
        for(int i = 0; i < this.operands.length; i++)
        {
            newOperands[i] = operands[i];
        }
        newOperands[operands.length] = arg;
        if (!arg.contains("* "))
            numOperands ++;
        this.operands = newOperands;
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
            return mnemonic;
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
                if (operands[i].contains("* ") && i == operands.length - 1)
                {
                    line = line.substring(0,line.length() - 1);
                    while(line.length() < 38)
                    {
                        line += " ";
                    }
                    line += operands[i];
                } else {
                    line += operands[i]+",";
                }
            }
        }
        
        if (line.charAt(line.length() - 1) == ',')
        {
            line = line.substring(0,line.length()-1);
        }
        
        return line + "\n" ;
    }

    @Override
    public String toString()
    {
        String op = "";
        if (operands != null)
        {
            for(int i = 0 ; i < operands.length; i++)
            {
                op += " op"+i +": "+ operands[i] + ",";
            }
        }        
        return "lbl: " + label + ", mnemonic: " + mnemonic + (op.length() > 0 ? op.substring(0,op.length() - 1):"") +", is comment: " + comment + " , size: " + getSize();
    }

}
