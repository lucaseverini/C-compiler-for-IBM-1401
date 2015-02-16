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
	static String newLine = System.getProperty("line.separator");
 	
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

	int consumeCurrentLine()
	{
		if(colCount == 0)
		{
			return 0;
		}

		try
		{
			String theLine = reader.readLine();
			if(theLine != null)
			{
				writer.writeBytes(theLine + newLine);
				lineCount++;
			}

			colCount = 0;

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
			consumeCurrentLine();

			String theLine = null;		
			while (lineCount < lineNum && (theLine = reader.readLine()) != null) 
			{
				lineCount++;
				
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

	int jumpFromUntil(int startLine, int startCol, int endLine, int endCol)
	{
		copyUntilLine(startLine - 1);
		
		try
		{
			colCount = 0;
			while(colCount < startCol - 1)
			{
				char ch = (char)reader.read();
				if(ch < 256)
				{
					writer.write(ch);
					colCount++;
				}
				else
				{
					break;
				}
			}
			
			int lines = endLine - startLine;
			if(lines > 0)
			{
				while(lines-- > 0)
				{
					String theLine = reader.readLine();
					
					lineCount++;
				}
				
				colCount = 0;
			}
			
			while(colCount < endCol)
			{
				char ch = (char)reader.read();
				if(ch < 256)
				{
					colCount++;
				}
				else
				{
					break;
				}
			}
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
			consumeCurrentLine();
			
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
			consumeCurrentLine();

			String theLine = null;		
			while ((theLine = reader.readLine()) != null) 
			{
				lineCount++;
				
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
			consumeCurrentLine();

			String theLine = reader.readLine();
			if(theLine != null)
			{
				lineCount++;
				
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
			consumeCurrentLine();

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
				theLine = theLine.replace(toReplace, replacement);				
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
