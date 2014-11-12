
/*
	SmallC.java

    Assignment #6 - CS153 - SJSU
	November-11-2014

	By Sean Papay, Matt Pleva, Luca Severini
*/

import java.util.ArrayList;
import java.util.List;


// SmallC -----------------------------------------------------
public class SmallC 
{
	static PreProcSymTab symTable = new PreProcSymTab();
	static List<String> paths = new ArrayList<>();
	static int filePos = 0;
	static int lastCopiedLine = 0;
	static boolean ignore = false;
	static FileCopier rw = null;

    public static void main(String[] args) throws Exception
    {
		System.out.println("Small-C Pre-processor");

		String inFile = parseArguments(args);
		if(inFile == null)
		{
			System.exit(1);
		}

		String outFile = SmallCPP.preprocessFile(inFile, symTable);
		
		System.out.println();
		System.out.println("Final Preprocessed file: " + outFile);
 		System.out.println("Final Symbol table:\n" + symTable);

		System.exit(0);
    }

	static String parseArguments(String[] args)
	{
		return args[0];
	}
}
