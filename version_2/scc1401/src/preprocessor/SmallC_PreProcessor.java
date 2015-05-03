/*
	SmallC_PreProcessor.java

    The Small-C cross-compiler for IBM 1401

	April-16-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package preprocessor;

import compiler.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// SmallC_PreProcessor -----------------------------------------------------
public class SmallC_PreProcessor
{
	static PreProcSymTab symTable = new PreProcSymTab();
	static List<String> paths = new ArrayList<>();
	static List<String> files = new ArrayList<>();
	static String outputFile = null;
	static int verbose = 0;
	static int optimization = 0;
	static boolean keepComments = false;
	static boolean printOutput = false;
	static boolean keepPreprocessedFile = false;

    public static void main(String[] args) throws Exception
    {
		int result = 0;
		
		parseArguments(args);
		
		if (verbose != 0 || printOutput)
		{
			System.out.println("Small-C Cross-Compiler for IBM 1401");
			System.out.println("Version 1.0 - May 1 2015");
			System.out.println("Working Directory: " + System.getProperty("user.dir"));
		}
		
		if(files.isEmpty())
		{
			System.out.println("no input");
		}

		for(String file : files)
		{
			if (verbose != 0 || printOutput)
			{
				System.out.println("");
			}
			
			if (verbose != 0)
			{
				System.out.println("C file: " + file);
			}

			String preprocFile = SmallCPP.preprocessFile(file, paths, symTable, false, keepComments, verbose);
			if(preprocFile != null)
			{
				if (printOutput)
				{
					System.out.println("Preprocessed file: " + preprocFile);
					
					if(verbose != 0)
					{
						System.out.println("Preprocessor symbol table:\n" + symTable);
					}
				}
				
				if(outputFile == null)
				{
					if(file.contains(".")) 
					{
						outputFile = file.substring(0, file.lastIndexOf('.')) + ".s";
					}
				}
				else if(outputFile.equalsIgnoreCase("stdout"))
				{
					outputFile = null;
				}
				
				if(outputFile != null)
				{
					File outFile = new File(outputFile);
					outFile.delete();
				}
				else
				{
					System.out.println("Autocoder output:");
				}
				
				String value = symTable.getValue("STACK");
				int stackVal = value == null ? 0 : Integer.parseInt(value);

				value = symTable.getValue("CODE");
				int codeVal = value == null ? 0 : Integer.parseInt(value);

				value = symTable.getValue("DATA");
				int dataVal = value == null ? 0 : Integer.parseInt(value);

				result = SmallCC.compile(preprocFile, outputFile, stackVal, codeVal, dataVal, optimization);
				
				if(result == 0 && outputFile != null)
				{
					System.out.println("Autocoder file: " + outputFile);
					
					System.out.println("Compilation Successful!");
				}
				else if(result != 0)
				{
					System.out.println("Compilation Failed!");
				}
				
				if(!keepPreprocessedFile)
				{
					File processedFile = new File(preprocFile);
					processedFile.delete();
				}
			}
			else
			{
				System.out.println("Preprocessing Failed!");
				result = 1;
			}
		}

		System.exit(result);
    }

	static void parseArguments(String[] args)
	{
		int status = 0;

		for(String s : args)
		{
			switch(s)
			{
				case "-O0":
					optimization = 0;
					break;

				case "-O":
				case "-O1":
					optimization = 1;
					break;

				case "-v0":
					verbose = 0;
					break;

				case "-v":
				case "-v1":
					verbose = 1;
					break;

				case "-v2":
					verbose = 2;
					break;

				case "-k":
					keepPreprocessedFile = true;
					break;

				case "-C":
					keepComments = true;
					break;
					
				case "-X":	// This allows us to print out all preprocessor messages otherwise it just prints error messages
					printOutput = true;
					break;
					
				case "-D":	// Definitions
					status = 1;
					break;

				case "-h":	// Headers
					status = 2;
					break;

				case "-o":	// Output file
					status = 3;
					break;

				default:
				{
					switch(status)
					{
						case 0:
							files.add(s);
							break;

						case 1:
							String[] parts = s.split("=");
							symTable.setValue(parts[0], parts.length == 2 ? parts[1] : null);
							break;

						case 2:
							paths.add(s);
							break;

						case 3:
							outputFile = s;
							break;
					}

					status = 0;
					break;
				}
			}
		}
	}
}
