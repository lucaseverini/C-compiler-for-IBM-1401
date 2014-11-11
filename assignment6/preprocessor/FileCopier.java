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
	
	FileCopier(String fileToRead, String fileToWrite)
	{
		lineCount = 0;

		try
		{
			File in = new File(fileToRead);
			reader = new BufferedReader(new FileReader(in));

			File out = new File(fileToWrite);
			writer = new BufferedWriter(new FileWriter(out));
		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }		
	}

	int jumpUntilLine(int lineNum)
	{
		try
		{
			String theLine = null;		
			while (lineCount < lineNum && (theLine = reader.readLine()) != null) 
			{
				lineCount++;
			}
		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
		
		return 0;
	}

	int copyUntilEnd()
	{
		try
		{
			String theLine = null;		
			while ((theLine = reader.readLine()) != null) 
			{
				lineCount++;
				
				theLine = theLine.trim();
				if(theLine.length() > 0)
				{
					writer.write(theLine);
					writer.newLine();
				}
			}
			writer.flush();

		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
		
		return 0;
	}

	int copyLine()
	{
		try
		{
			String theLine = reader.readLine();
			if(theLine != null)
			{
				lineCount++;
				
				theLine = theLine.trim();
				if(theLine.length() > 0)
				{
					writer.write(theLine);
					writer.newLine();
					writer.flush();
				}
			}

		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
		
		return 0;
	}

	int copyLineAndReplace(String toReplace, String replacement)
	{
		try
		{
			String theLine = reader.readLine();
			if(theLine != null)
			{
				lineCount++;
				
				theLine = theLine.trim().replace(toReplace, replacement);
				if(theLine.length() > 0)
				{
					writer.write(theLine);
					writer.newLine();
					writer.flush();
				}
			}

		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
		
		return 0;
	}

	int copyUntilLine(int lineNum)
	{
		try
		{
			String theLine = null;		
			while (lineCount < lineNum && (theLine = reader.readLine()) != null) 
			{
				lineCount++;
				
				theLine = theLine.trim();
				if(theLine.length() > 0)
				{
					writer.write(theLine);
					writer.newLine();
				}
			}
			writer.flush();

		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
		
		return 0;
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
				theLine = theLine.trim();
				if(theLine.length() > 0)
				{
					writer.write(theLine);
					writer.newLine();
				}
			}			
			writer.flush();

			rd.close();
		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

		return 0;
	}
	
	void close()
	{
		try
		{
			reader.close();
			writer.close();
		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
	}
}
