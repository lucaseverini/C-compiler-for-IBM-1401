import java.io.BufferedReader;
import java.io.FileReader;

import wci.frontend.*;
import wci.intermediate.*;
import wci.backend.*;
import wci.message.*;

import static wci.frontend.pascal.PascalTokenType.STRING;
import static wci.message.MessageType.*;

/**
 * <h1>Pascal</h1>
 *
 * <p>Compile or interpret a Pascal source program.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class Main
{
    private static final String FLAGS = "[-ix]";
    private static final String USAGE =
        "Usage: Java|Pascal parse|execute|compile " + FLAGS + " <source file path>";

    /**
     * The main method.
     * @param args command-line arguments: "compile" or "execute" followed by
     *             optional flags followed by the source file path.
     */
    public static void main(String args[])
    {
        try {
			String language = args[0];
            String operation = args[1];

            // Operation.
            if (!(operation.equalsIgnoreCase("parse") || operation.equalsIgnoreCase("compile") || operation.equalsIgnoreCase("execute"))) 
			{
                throw new Exception();
            }

            int i = 1;
            String flags = "";

            // Flags.
            while ((++i < args.length) && (args[i].charAt(0) == '-')) {
                flags += args[i].substring(1);
            }

            // Source path.
            if (i < args.length) 
			{
				String path = args[i];
	
				if(language.equalsIgnoreCase("pascal"))
				{
					new Pascal(operation, path, flags);
				}
				else if(language.equalsIgnoreCase("java"))
				{
					new Java(operation, path, flags);
				}
				else
				{
					throw new Exception();
				}
            }
            else 
			{
                throw new Exception();
            }
        }
        catch (Exception ex) {
            System.out.println(USAGE);
        }
    }
 }
