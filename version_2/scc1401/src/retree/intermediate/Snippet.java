package retree.intermediate;

import java.io.*;
import java.util.*;
import static retree.RetreeUtils.*;

public class Snippet
{
    private static HashMap<String, ArrayList<Instruction>> snips = new HashMap<String, ArrayList<Instruction>>();
    private static final HashMap<String, String> snippetLabels = new HashMap<String, String>();

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
                        list.add(new Instruction("","     "+line));
                    } else {
                        String[] parts;
                        // Handles the case where we have a constant space
                        // otherwise it will split incorrectly and lead to bad code
                        if (line.contains("@ @"))
                        {
                            char[] tmp = line.toCharArray();
                            int posAt = line.indexOf('@');
                            tmp[posAt + 1] = '_';
                            line = new String(tmp);
                            parts = line.split(" ");
                            for(int i = 0; i < parts.length; i++)
                            {
                                if (parts[i].contains("@_@"))
                                {
                                    parts[i] = parts[i].replace("@_@","@ @");
                                }
                            }
                        } else {
                            parts = line.split(" ");
                        }
                        if (parts.length > 2)
                        {
                            String[] instrArgs = new String[parts.length - 2];
                            for (int i = 0; i < instrArgs.length; i++)
                            {
                                instrArgs[i] = parts[i+2];
                            }
                            list.add(new Instruction(parts[0],parts[1],instrArgs));
                        }
                        else if (parts.length == 2)
                        {
                            list.add(new Instruction("",parts[0],parts[1]));
                        }
                        else if (parts.length == 1)
                        {
                            list.add(new Instruction("",parts[0]));
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
