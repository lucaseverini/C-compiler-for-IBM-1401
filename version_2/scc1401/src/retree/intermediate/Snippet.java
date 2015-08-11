/*
    Snippet.java

    The Small-C cross-compiler for IBM 1401

    April-9-2015

    By Matt Pleva, Luca Severini
*/

package retree.intermediate;

import compiler.SmallCC;

import java.io.*;
import java.util.*;

public class Snippet
{
    private static final HashMap<String, ArrayList<Instruction>> snips = new HashMap<String, ArrayList<Instruction>>();
    private static final HashMap<String, String> snippetLabels = new HashMap<String, String>();
    private static final ArrayList<ISnippetParser> stages = new ArrayList<ISnippetParser>();

    private static void loadSnippet(String snippet)
    {
        try {
            ArrayList<Instruction> list;
            File f = new File((SmallCC.nostack ? "snippets/nostack/" : "snippets/stack/")+snippet+".s");
            ArrayList<String> filecon = new ArrayList<String>();
            Scanner s = new Scanner(f);
            while (s.hasNextLine())
            {
                String line = s.nextLine();
                if (!line.equals(""))
                {
                    filecon.add(line+"\n");
                }
            }
            list = startParser(filecon);
            if (list == null)
            {
                throw new Exception("Snippet: "+snippet);
            }
            snips.put(snippet, list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Snippet " + snippet + " failed to load.");
        }
    }

    public static ArrayList<Instruction> startParser(ArrayList<String> filecon) {
        
        ArrayList<Instruction> list = new ArrayList<Instruction>();
        for (int i = 0; i < filecon.size(); i++) {
            Instruction in = new Instruction();
            String line = filecon.get(i);
            for (int j = 0; j < stages.size(); j++) {
                if (line == null)
                {
                    return null;
                }
                line = stages.get(j).parse(in, line);
            }
            list.add(in);
        }
        return list;
    }


    public static void Init()
    {
        stages.add(new LabelParser());
        stages.add(new MnemonicParser());
        stages.add(new ArgumentParser());
        loadSnippet("header");

        loadSnippet("char_to_number");
        snippetLabels.put("char_to_number", "CHRNMN");
 
        loadSnippet("char_to_pointer");
        snippetLabels.put("char_to_pointer", "CHRPTR");
        
        loadSnippet("clean_number");
        snippetLabels.put("clean_number", "CLNNMN");
        
        loadSnippet("number_to_pointer");
        snippetLabels.put("number_to_pointer", "NMNPTR");
        
        loadSnippet("pointer_to_number");
        snippetLabels.put("pointer_to_number", "PTRNMN");

        loadSnippet("pointer_to_char");
        snippetLabels.put("pointer_to_char", "PTRCHR");

        loadSnippet("SNIP_DIV");
        snippetLabels.put("SNIP_DIV","SNPDIV");

        loadSnippet("number_to_char");
        snippetLabels.put("number_to_char", "NMNCHR");
        System.out.println("Finished snippet init");
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
