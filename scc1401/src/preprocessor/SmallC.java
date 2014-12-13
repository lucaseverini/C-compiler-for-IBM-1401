/*
	SmallC.java

    Assignment #6 - CS153 - SJSU
	November-11-2014

	By Sean Papay, Matt Pleva, Luca Severini
*/

package preprocessor;

import compiler.*;
import java.util.ArrayList;
import java.util.List;

// SmallC -----------------------------------------------------
public class SmallC
{
	static PreProcSymTab symTable = new PreProcSymTab();
	static List<String> paths = new ArrayList<>();
	static List<String> files = new ArrayList<>();

    public static void main(String[] args) throws Exception
    {
		System.out.println("Small-C Pre-processor");

		parseArguments(args);
		if(paths.size() == 0)
		{
			System.exit(1);
		}

		for(String file : files)
		{
			String outFile = SmallCPP.preprocessFile(file, paths, symTable, false);
			if(outFile != null)
			{
				System.out.println("Final Preprocessed file: " + outFile);
				System.out.println("Final Symbol table:\n" + symTable);

				SmallCC.compile(outFile);
			}
		}

		System.exit(0);
    }

	static void parseArguments(String[] args)
	{
		int status = 0;

		for(String s : args)
		{
			switch(s)
			{
				case "-D":
					status = 1;
					break;

				case "-h":
					status = 2;
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
					}

					status = 0;
					break;
				}
			}
		}
	}
}
