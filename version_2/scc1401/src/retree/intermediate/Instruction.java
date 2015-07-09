/*
    Instruction.java

    The Small-C cross-compiler for IBM 1401

    April-9-2015

    By Matt Pleva, Luca Severini
*/

package retree.intermediate;

public class Instruction
{
    private boolean hasComment = false;
    private boolean lineComment = false;
    private String label = "";
    private String mnemonic = "";
    private String commentStr = "";
    private String[] operands = new String[0];
    private int numOperands = 0;

    public Instruction(){}

    // Use this to create only line comments with no code
    public Instruction(String comm)
    {
        lineComment = true;
        commentStr = comm;
    }

    // use this to create code with a comment at the end
    public Instruction(String lineComment,String label, String mnemonic, String... args)
    {
        this.label = label;
        this.mnemonic = mnemonic;

        if (lineComment != null && !lineComment.isEmpty()) {
            commentStr = lineComment;
            hasComment = true;
        }

        operands = args;
        numOperands = args.length;
    }

    public int getSize()
    {
        if ((this.lineComment) || this.mnemonic.equals("ORG") || this.mnemonic.equals("END") || this.mnemonic.equals("EQU"))
        {
            return 0;
        }
        if (this.mnemonic.equals("DSA"))
        {
            return 3;
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
        if (this.mnemonic.charAt(0) == 'B')
        {

            if (numOperands > 2)
            {
                return 1 + 3 * (numOperands-1) + 1;
            }
            if (this.mnemonic.length() > 1)
            {
                // 1 for the B, 3 for the address, 1 for the d-character
                return 1 + 3 + 1;
            }
            return 1 + 3 * numOperands;
        }
        return 1 + 3 * numOperands;
    }
    
    public void setMnemonic(String m)
    {
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
        this.operands = newOperands;
        this.numOperands = this.operands.length;
    }
    
    public boolean isComment()
    {
        return this.lineComment || this.hasComment;
    }

    public boolean isLineComment()
    {
        return this.lineComment;
    }

    public void setLineComment(String comm)
    {
        this.commentStr = comm;
        this.lineComment = true;
    }

    public void addComment(String comm)
    {
        this.hasComment = true;
        this.commentStr = comm;
    }

    public boolean containsComment()
    {
        return this.hasComment;
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
        if (lineComment)
        {
            return commentStr.contains("*") ? commentStr : "     * " + commentStr.replace("     ","");
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
        
        for (int i = 0 ; i < operands.length; i++)
        {
            if (i + 1 < operands.length)
            {
                line += operands[i]+",";
            } else {
                line += operands[i];
            }
        }

        if(hasComment)
        {
            while(line.length() < 39) line += " ";
            line += "*" + commentStr;
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
        return "comment: "+commentStr+", lbl: " + label + ", mnemonic: " + mnemonic + (op.length() > 0 ? op.substring(0,op.length() - 1):"") + ", hasComment: "+hasComment+" , line_com: "+lineComment+" , size: " + getSize();
    }

}
