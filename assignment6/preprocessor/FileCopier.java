/*
	FileCopier.java

    Assignment #6 - CS153 - SJSU
	November-10-2014

	By Sean Papay, Matt Pleva, Luca Severini
*/

import java.io.*;

// ReaderWriter -----------------------------------------------------
public class FileCopier
{
	BufferedReader reader;
	BufferedWriter writer;
	int lineCount = 0;
	
	FileCopier(String fileToRead, String fileToWrite) throws Exception
	{
		File in = new File(fileToRead);
		reader = new BufferedReader(new FileReader(in));

		File out = new File(fileToWrite);
		writer = new BufferedWriter(new FileWriter(out));
		
		lineCount = 0;
	}
	
	int copyUntilLine(int lineNum)
	{
		try
		{
			String theLine = null;		
			while (lineCount < lineNum && (theLine = reader.readLine()) != null) 
			{
				writer.write(theLine + "\n");

				lineCount++;
			}
			writer.flush();

		}
		catch(Exception ex) {}
		
		return lineCount;
	}

	int copyFile(String file)
	{
		try
		{
			File in = new File(file);
			BufferedReader rd = new BufferedReader(new FileReader(in));

			String theLine = null;		
			while ((theLine = rd.readLine()) != null) 
			{
				writer.write(theLine + "\n");
			}			
			writer.flush();

			rd.close();
		}
		catch(Exception ex) {}

		return 0;
	}
	
	void close() throws Exception
	{
		reader.close();
		writer.close();
	}
}
