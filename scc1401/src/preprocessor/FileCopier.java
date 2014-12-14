/*
	FileCopier.java

    Assignment #6 - CS153 - SJSU
	November-10-2014

	By Sean Papay, Matt Pleva, Luca Severini
*/

package preprocessor;

import java.io.*;

// ReaderWriter -----------------------------------------------------
public class FileCopier
{
	RandomAccessFile reader;
	RandomAccessFile writer;
	int lineCount;
	int colCount;
	long lastWriteStart;
	String newLine = System.getProperty("line.separator");
	
	FileCopier(String fileToRead, String fileToWrite)
	{
		lineCount = 0;
		colCount = 0;
		lastWriteStart = 0;

		try
		{
			File in = new File(fileToRead);
			reader = new RandomAccessFile(in, "rws");

			File out = new File(fileToWrite);
			writer = new RandomAccessFile(out, "rws");
			
			writer.setLength(0);
		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }		
	}

	int completeCurrentLine()
	{
		try
		{
			int ch = 0;
			while(ch != 10)
			{
				ch = (char)reader.read();
				writer.write((char)ch);
			}

			lineCount++;
			colCount = 0;
		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
		
		return 0;
	}

	int jumpUntilLine(int lineNum)
	{
		try
		{
			if(colCount != 0)
			{
				completeCurrentLine();
			}
			
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

	int jumpFromUntil(int startLine, int startCol, int endLine, int endCol)
	{
		copyUntilLine(startLine - 1);
		
		try
		{
			int lines = endLine - startLine;
			colCount = 0;
			while(colCount < startCol - 1)
			{
				char ch = (char)reader.read();
				writer.write(ch);
				colCount++;
			}
			
			if(lines > 0)
			{
				colCount = 0;
			}
			
			while(colCount < endCol)
			{
				char ch = (char)reader.read();
				colCount++;
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
			if(colCount != 0)
			{
				completeCurrentLine();
			}

			String theLine = null;		
			while ((theLine = reader.readLine()) != null) 
			{
				lineCount++;
				
				theLine = theLine.trim();
				if(theLine.length() > 0)
				{
					lastWriteStart = writer.getFilePointer();
					writer.writeBytes(theLine + newLine);
				}
			}
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
			if(colCount != 0)
			{
				completeCurrentLine();
			}

			String theLine = reader.readLine();
			if(theLine != null)
			{
				lineCount++;
				
				theLine = theLine.trim();
				if(theLine.length() > 0)
				{
					lastWriteStart = writer.getFilePointer();
					writer.writeBytes(theLine + newLine);
				}
			}

		}
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
		
		return 0;
	}

	int copyUntilLineAndReplace(int lineNum, String toReplace, String replacement)
	{		
		try
		{
			if(colCount != 0)
			{
				completeCurrentLine();
			}

			if(lineCount < lineNum - 1)
			{
				copyUntilLine(lineNum - 1);
			}
			
			String theLine;			
			if(lineCount == lineNum)
			{
				writer.seek(lastWriteStart);
				long filePos = writer.getFilePointer();
				theLine = writer.readLine();
				writer.setLength(filePos);
			}
			else
			{
				theLine = reader.readLine();
				if(theLine != null)
				{
					lineCount++;
				}
			}
			
			if(theLine != null)
			{
				theLine = theLine.trim().replace(toReplace, replacement);				
				if(theLine.length() > 0)
				{
					lastWriteStart = writer.getFilePointer();
					writer.writeBytes(theLine + newLine);
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
			if(colCount != 0)
			{
				completeCurrentLine();
			}

			String theLine = null;		
			while (lineCount < lineNum && (theLine = reader.readLine()) != null) 
			{
				lineCount++;
				
				theLine = theLine.trim();
				if(theLine.length() > 0)
				{
					lastWriteStart = writer.getFilePointer();
					writer.writeBytes(theLine + newLine);
				}
			}
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
					lastWriteStart = writer.getFilePointer();
					writer.writeBytes(theLine + newLine);
				}
			}			

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
