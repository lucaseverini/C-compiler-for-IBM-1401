/*
    Optimizer.java

    The Small-C cross-compiler for IBM 1401

    April-9-2015

    By Matt Pleva, Luca Severini
*/

package retree.intermediate;

import java.util.*;
import static retree.RetreeUtils.*;
import compiler.*;
import retree.RetreeUtils;

public class Optimizer
{
    private final static HashSet<String> snippetsUsed = new HashSet<String>();
    public final static HashMap<String,String> constantLabelTransform = new HashMap<String,String>();
    public final static ArrayList<Instruction> instr = new ArrayList<Instruction>();
    public final static HashMap<String,String> labelTransform = new HashMap<String,String>();
    private static Instruction codeAndDataPosition = new Instruction();
    private static boolean dropComments = false;

    public static void setORGPosition(Instruction i)
    {
        codeAndDataPosition = i;
    }

    public static Instruction getLastInstruction()
    {
        return instr.get(instr.size()-1);
    }

    public static void CollapseInstrcs()
    {
        for(int i = 0; i< instr.size()-1; i++)
        {
            switch(instr.get(i).getMnemonic())
            {
                case "NOP":
                {
                    if(!instr.get(i).getLabel().equals(""))
                    {
                        if (!instr.get(i+1).getLabel().equals(""))
                        {
                            labelTransform.put(instr.get(i).getLabel(), instr.get(i+1).getLabel());
                        }
                        else
                        {
                            instr.get(i+1).setLabel(instr.get(i).getLabel());
                        }
                    }
                    instr.remove(i);
                    i -= 1;
                    break;
                }
                case "B":
                {
                    if (instr.get(i+1).getMnemonic().equals("B") && instr.get(i+1).getOperand(0).equals(instr.get(i).getOperand(0)))
                    {
                        instr.remove(i);
                        i -= 1;
                        break;
                    }
                    break;
                }
            }
            if (instr.get(i).isLineComment() && !instr.get(i).getLabel().equals(""))
            {
                if (!instr.get(i+1).getLabel().equals(""))
                {
                    labelTransform.put(instr.get(i).getLabel(), instr.get(i+1).getLabel());
                }
                else
                {
                    instr.get(i+1).setLabel(instr.get(i).getLabel());
                    instr.get(i).setLabel("");
                }
            }
        }
    }

    public static void ReLabel()
    {
        for(int i = 0; i< instr.size(); i++)
        {
            if (labelTransform.containsKey(instr.get(i).getLabel()))
            {
                while (labelTransform.containsKey(instr.get(i).getLabel()))
                {
                    instr.get(i).setLabel(labelTransform.get(instr.get(i).getLabel()));
                }
            }
            for(int j = 0;j < instr.get(i).getOperandListSize(); j++)
            {
                if (labelTransform.containsKey(instr.get(i).getOperand(j)))
                {
                    while (labelTransform.containsKey(instr.get(i).getOperand(j)))
                    {
                        instr.get(i).setOperand(labelTransform.get(instr.get(i).getOperand(j)),j);
                    }
                }
            }
        }
    }

    public static void ConstantLabel()
    {
        boolean afterStart = false;
        for(int i = 0; i< instr.size(); i++)
        {
            if (afterStart)
            {
                Instruction in = instr.get(i);
                for (int j = 0 ; j < in.getOperandListSize(); j++)
                {
                    if (in.getOperand(j) != null)
                    {
                        if (in.getOperand(j).length() > 0 && ((in.getOperand(j).charAt(0) == '@' && in.getOperand(j).charAt(in.getOperand(j).length() - 1) == '@')
                                && !constantLabelTransform.containsKey(in.getOperand(j))))
                        {
                            String label = label(SmallCC.nextLabelNumber());
                            constantLabelTransform.put(in.getOperand(j), label);
                            in.setOperand(constantLabelTransform.get(in.getOperand(j)), j);
                        }
                        else if (in.getOperand(j).length() > 0 && ((in.getOperand(j).charAt(0) == '@' && in.getOperand(j).charAt(in.getOperand(j).length() - 1) == '@')
                                && constantLabelTransform.containsKey(in.getOperand(j))))
                        {
                            in.setOperand(constantLabelTransform.get(in.getOperand(j)), j);
                        }
                    }
                }
            }
            else if (instr.get(i).getLabel() != null && instr.get(i).getLabel().equals("START"))
            {
                afterStart = true;
            }
        }
    }

    public static int getListSize()
    {
        return instr.size();
    }

    public static void addInstruction(Instruction i)
    {
        instr.add(i);
    }

    public static void addInstruction(String comment, String label, String op, String ... args) {
        if ((label.length() > 0 || op.length() > 0)) {

            instr.add(new Instruction(comment, label, op, args));
        } else if (comment.length() > 0 && !(label.length() > 0 && op.length() > 0)) {
            instr.add(new Instruction(RetreeUtils.makeLineComment(comment)));
        }

    }

    public static void addInstructionAtPos(int pos, Instruction i)
    {
        instr.add(pos, i);
    }

    public static void addSnippet(String snippetName)
    {
        if (!snippetsUsed.contains(snippetName))
        {
            snippetsUsed.add(snippetName);
        }
    }

    private static void addSnippetInstructions(String snippetName)
    {
        ArrayList<Instruction> list = Snippet.getSnippet(snippetName);
        if (list != null)
        {
            if (snippetName.equals("header"))
            {
                int pos = 0;
                for (Instruction i : list)
                {
                    addInstructionAtPos(pos++,i);
                }
            }
            else
            {
                for (Instruction i : list)
                {
                    addInstruction(i);
                }
            }
        }
    }

    private static void PutSnippets()
    {
        Iterator i = snippetsUsed.iterator();
        while(i.hasNext())
        {
            Object item = i.next();
            if (item instanceof String)
            {
                String s = (String)item;
                if (!s.equals("SNIP_DIV") && !s.equals("header"))
                    addSnippetInstructions(s);
            }
        }
    }

    private static void PutPosSensitivSnippets()
    {
        addSnippetInstructions("SNIP_DIV");
    }

    private static void PutHeader()
    {
        addSnippetInstructions("header");
    }

    private static void PutConstants()
    {
        Set<String> keys = constantLabelTransform.keySet();
        Iterator ksIterator = keys.iterator();
        while (ksIterator.hasNext())
        {
            Object keyobj = ksIterator.next();
            if (keyobj instanceof String)
            {
                String key = (String) keyobj;
                String value = constantLabelTransform.get(key);
                addInstruction(new Instruction("",value,"DCW",key));
            }
        }
    }

    public static void printInstrList()
    {
        for (Instruction i: instr)
        {
            System.out.println(i);
        }
    }

    public static void DropComments(boolean b)
    {
        dropComments = b;
    }

    private static void removeComments()
    {
        for (int i = 0; i < instr.size(); i++)
        {
            if (instr.get(i).isComment())
            {
                instr.remove(i);
                i -= 1;
            }
        }
    }

    private static int findCodePosition()
    {
        int ret = 0;
        int len = 0;
        String pos = codeAndDataPosition.getOperand(0);
        while(len < pos.length())
        {
            ret += pos.charAt(len++)-'0';
            ret *= 10;
        }
        return ret/10;
    }

    private static int parseORGPos(String orgpos)
    {
        int ret = 0;
        int len = 0;
        while(len < orgpos.length())
        {
            ret += orgpos.charAt(len++)-'0';
            ret *= 10;
        }
        return ret/10;
    }

    public static String GenerateCode()
    {
        if (dropComments)
        {
            removeComments();
        }
        
        System.out.println("Collapse instructions");
        CollapseInstrcs();

        System.out.println("Moving labels");
        ReLabel();

        System.out.println("Adding snippets");
        PutSnippets();

        System.out.println("Labeling constants");
        ConstantLabel();

        System.out.println("Adding position sensitive snippets");
        PutPosSensitivSnippets();

        System.out.println("Putting labels and constants in");
        PutConstants();

        addInstruction(new Instruction("End of program code.", "","END", "START"));

        System.out.println("Starting to generate code ...");
        String code = "";
        int size = 0;
        for (Instruction i: instr)
        {
            size += i.getSize();
        }

        System.out.println("Size: " + (size));
        boolean aboveStart = true;
        // set all of the orgs above the code org to have correct values
//        for (Instruction i: instr)
//        {
//            if (i.getLabel().equals("START"))
//                break;
//            if (aboveStart && i.getMnemonic().equals("ORG"))
//            {
//                i.setArgument(0,""+(15999 - (size)));
//            }
//            size -= i.getSize();
//        }

        System.out.println("Adding in header");
        PutHeader();

        for (Instruction i : instr)
        {
            code += i.generateCode();
        }

        return code;
    }
}
