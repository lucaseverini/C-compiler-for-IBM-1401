package retree.intermediate;

import java.io.*;
import java.util.*;
import static retree.RetreeUtils.*;

public class Snippet
{
    private static HashMap<String, ArrayList<Instruction>> snips = new HashMap<String, ArrayList<Instruction>>();
    private static final HashMap<String, String> snippetLabels = new HashMap<String, String>();

    private static int countStars(String in)
    {
        int count = 0;
        for (int i = 0; i < in.length(); i++)
        {
            if(in.charAt(i) == '*') count++;
        }
        return count;
    }

    private static void loadSnippet(String snippet)
    {
        try {
            ArrayList<Instruction> list = new ArrayList<Instruction>();
            File f = new File("snippets/"+snippet+".s");
            ArrayList<String> filecon = new ArrayList<String>();
            Scanner s = new Scanner(f);
            while (s.hasNextLine())
            {
                String line = s.nextLine().trim().replaceAll(" +", " ");
                if (line.length() > 0)
                {
                    if (line.charAt(0) == '*')
                    {
                        list.add(new Instruction("","     "+line + "\n"));
                    } else {
                        String[] parts;
                        String[] comparts;
                        // Handles the case where we have a constant space
                        // otherwise it will split incorrectly and lead to bad code
                        if (line.contains("@ @"))
                        {
                            char[] tmp = line.toCharArray();
                            int posAt = line.indexOf('@');
                            tmp[posAt + 1] = '_';
                            line = new String(tmp);
                            int commentpos = line.lastIndexOf("*");
                            if (commentpos != -1)
                            {
                                if (countStars(line) == 1)
                                {
                                    comparts = line.split("\\*");
                                } else {
                                    String part1 = line.substring(0,commentpos);
                                    String part2 = line.substring(commentpos);
                                    comparts = new String[2];
                                    comparts[0] = part1;
                                    comparts[1] = part2;
                                }
                            } else {
                                comparts = new String[1];
                                comparts[0] = line;
                            }
                            parts = comparts[0].split(" ");
                            for(int i = 0; i < parts.length; i++)
                            {
                                if (parts[i].contains("@_@"))
                                {
                                    parts[i] = parts[i].replace("@_@","@ @");
                                }
                            }
                        } else {
                            int commentpos = line.lastIndexOf("*");
                            if (commentpos != -1)
                            {
                                if (countStars(line) == 1)
                                {
                                    comparts = line.split("\\*");
                                } else {
                                    String part1 = line.substring(0,commentpos);
                                    String part2 = line.substring(commentpos+2);
                                    comparts = new String[2];
                                    comparts[0] = part1;
                                    comparts[1] = part2;
                                }
                            } else {
                                comparts = new String[1];
                                comparts[0] = line;
                            }
                            parts = comparts[0].split(" ");
                        }
                        if (parts.length > 2)
                        {
                            String[] instrArgs = new String[comparts.length > 1 ? parts.length - 1 : parts.length - 2];
                            for (int i = 0; i < instrArgs.length - (comparts.length > 1 ? 1 : 0); i++)
                            {
                                instrArgs[i] = parts[i+2];
                            }
                            if (comparts.length > 1)
                            {
                                instrArgs[instrArgs.length-1] = "* " + comparts[1];
                            }
                            list.add(new Instruction(parts[0],parts[1],instrArgs));
                        }
                        else if (parts.length == 2)
                        {
                            list.add(new Instruction("",parts[0],parts[1], comparts.length > 1 ? "* " + comparts[1] : "" ));
                        }
                        else if (parts.length == 1)
                        {
                            list.add(new Instruction("",parts[0], comparts.length > 1 ? "* " + comparts[1] : ""));
                        }
                    }
                }
            }
            snips.put(snippet, list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Snippet "+snippet+" failed to load.");
        }
    }

    public static void Init()
    {

        loadSnippet("char_to_pointer");
        snippetLabels.put("char_to_pointer", "CHRPTR");
        loadSnippet("clean_number");
        snippetLabels.put("clean_number", "CLNNUM");
        loadSnippet("number_to_pointer");
        snippetLabels.put("number_to_pointer", "NUMPTR");
        loadSnippet("pointer_to_number");
        snippetLabels.put("pointer_to_number", "PTRNUM");
        loadSnippet("header");
        loadSnippet("SNIP_DIV");
        snippetLabels.put("SNIP_DIV","DIV");
    }

    public static String getSnippetLabel(String snippet)
    {
        return snippetLabels.get(snippet);
    }

    public static ArrayList<Instruction> getSnippet(String snippet)
    {
        return snips.get(snippet);
    }
}
