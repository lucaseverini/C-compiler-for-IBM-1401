/*
	RetreeUtils.java

    Small-C compiler - SJSU
	March-1-2015

	By Sean Papay, Matt Pleva, Luca Severini
*/

package retree;

import compiler.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import retree.program.Initializer;
import retree.intermediate.*;

public class RetreeUtils
{
	private static final HashMap<String, String> snippetLabels = new HashMap<>();
	private static final HashMap<String, String> snippetCode = new HashMap<>();
	private static int staticInitializersSize = 0;
	private static int inlineCommentStart = 40;
	private static Boolean noComments = false;

	public static String INS(String comment, String label, String operation, String ... args)
	{
		String line = "     ";

		if(label != null && !label.isEmpty())
		{
			line += label;
		}

		while(line.length() < 15)
		{
			line += " ";
		}

		line += operation;

		while (line.length() < 20)
		{
			line += " ";
		}

		for (int i = 0; i < args.length; ++i)
		{
			if (i > 0)
			{
				line += ",";
			}

			line += args[i];
		}

		if(!noComments && comment != null && !comment.isEmpty())
		{
			while (line.length() < inlineCommentStart - 1)
			{
				line += " ";
			}

			// Remove any '@' from the inline comment because it can be considered part of the instruction...
			comment = comment.replace("@", "");

			if(comment.charAt(0) != '*')
			{
				comment = "* " + comment;
			}

			line += comment;
		}

		return line + "\n";
	}

	public static String COM(String comment)
	{
		if(noComments)
		{
			return "";
		}

		if(comment.length() > 0 && comment.charAt(0) != '*')
		{
			comment = "* " + comment;
		}

		return "     " + comment;// + "\n";
	}

	public static String NUM_CONST(int val, boolean arrayMember)
	{
		String str =  "@" + COD(val) + "@";
		// System.out.println("NUM_CONST: " + str + " : " + label + " : " + (arrayMember ? "ARR" : ""));

		return str;
	}

	public static String ADDR_CONST(int val, boolean arrayMember)
	{
		String str = "@" + ADDR_COD(val) + "@";
		// System.out.println("ADDR_CONST: " + str + " : " + label + " : " + (arrayMember ? "ARR" : ""));

		return str;
	}

	public static String CHAR_CONST(int value, boolean arrayMember)
	{
		// We take the given char as ascii, and return a 1401 constant
		// TODO - robustify - check for illegal characters n stuff

		if (value == 0)
		{
			return "EOS";
		}
		else if (value == '\n')
		{
			return "EOL";
		}
		else
		{
			String str =  "@" + Character.toUpperCase((char)value) + "@";
			// System.out.println("CHAR_CONST: " + str + " : " + label + " : " + (arrayMember ? "ARR" : ""));

			return str;
		}
	}

	public static String COD(int val)
	{
		String s;

		if (val < 0)
		{
			String[] negDigits = {"!", "J", "K", "L", "M", "N", "O", "P", "Q", "R"};
			val = -val;
			s = (val / 10) + negDigits[val % 10];
		}
		else
		{
			s = val + "";
		}

		while (s.length() < 5)
		{
			s = "0" + s;
		}

		return s;
	}

	public static int ADDR_DECOD(String addr)
	{
		addr = addr.replace("@", "");
		
		if(addr.length() != 3)
		{
			return Integer.getInteger(addr);
		}
		
		
		
		return 0;
	}

	public static String ADDR_COD(int addr)
	{
		String[] digits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"'", "/", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"!", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			 "?", "A", "B", "C", "D", "E", "F", "G", "H", "I"};
		
		// 0-3999 use 0-9 for units , 0-9 for tens , 0-I for hundreds
		// 4000-7999 use '-Z for units , 0-9 for tens , 0-I for hundreds
		// 8000-11999 use !-R for units , 0-9 for tens , 0-I for hundreds
		// 12000-15999 use ?-I for units , 0-9 for tens , 0-I for hundreds 

		addr = (16000 - (-addr) % 16000) % 16000; // addr = addr % 16000

		int lastDigitSet = 10 * (addr / 4000);
		int firstDigitSet = 10 * ((addr % 4000) / 1000);
		int decPart = addr % 1000;
		return digits[decPart / 100 + firstDigitSet] + digits[(decPart / 10) % 10] + digits[decPart % 10 + lastDigitSet];
	}

	public static String CHAR_COD(int val)
	{
		if (val == 0)			// EOS
		{
			return "'";
		}
		else if (val == '\n')	// EOL
		{
			return ";";
		}

		return "" + Character.toUpperCase((char)val);
	}

	public static String PUSH(int size, String location)
	{
		String code = "";
		String locValue;

		if(location.charAt(0) == 'L')
		{
			locValue = SmallCC.getVariableLabelValue(location);
		}
		else
		{
			locValue = location;
		}
		Optimizer.addInstruction("Push (" + location + ":" + size + ")","","");
		Optimizer.addInstruction("Add " + size + " to X2", "", "MA", ADDR_CONST(size, false), "X2");
		Optimizer.addInstruction("Load data at " + locValue + " to X2", "", "LCA", location, "0+X2");
		code += COM("Push (" + location + ":" + size + ")");
		code += INS("Add " + size + " to X2", null, "MA", ADDR_CONST(size, false), "X2");
		code += INS("Load data at " + locValue + " to X2", null, "LCA", location, "0+X2");

		return code;
	}

	public static String PUSH(int size)
	{

		String code = "";
		Optimizer.addInstruction("Push (" + size + ")","","");
		code += COM("Push (" + size + ")");
		Optimizer.addInstruction("Add " + size + " to X2", "", "MA", ADDR_CONST(size, false), "X2");
		code += INS("Add " + size + " to X2", null, "MA", ADDR_CONST(size, false), "X2");

		return code;
	}

	public static String POP(int size, String location)
	{
		String code = "";
		String locValue;

		if(location.charAt(0) == 'L')
		{
			locValue = SmallCC.getVariableLabelValue(location);
		}
		else
		{
			locValue = location;
		}

		Optimizer.addInstruction("Pop (" + location + ":" + size + ")","","");
		code += COM("Pop (" + location + ":" + size + ")");
		Optimizer.addInstruction("Load data at X2 to " + locValue, "", "LCA", "0+X2", location);
		code += INS("Load data at X2 to " + locValue, null, "LCA", "0+X2", location);
		Optimizer.addInstruction("Add " + -size + " to X2", "", "MA", ADDR_CONST(-size, false), "X2");
		code += INS("Add " + -size + " to X2", null, "MA", ADDR_CONST(-size, false), "X2");

		return code;
	}

	public static String POP(int size)
	{
		String code = "";
		Optimizer.addInstruction("Pop (" + size + ")","","");
		code += COM("Pop (" + size + ")");
		Optimizer.addInstruction("Add " + -size + " to X2", "", "MA", ADDR_CONST(-size, false), "X2");
		code += INS("Add " + -size + " to X2", null, "MA", ADDR_CONST(-size, false), "X2");

		return code;
	}

	//returns an address at the frame pointer + offset
	public static String OFF(int offset)
	{
		offset = (16000 + offset % 16000) % 16000; // offset :=  offset mod 16000
		return offset + "+X3";
	}

	// returns an address at the stack pointer + offset
	public static String STACK_OFF(int offset)
	{
		offset = (16000 + offset % 16000) % 16000; // offset := offset mod 16000
		return offset + "+X2";
	}

	public static String ADDR_LIT(int addr)
	{
		return "" + ((16000 + addr % 16000) % 16000);
	}

	private static String loadVariables()
	{
		String code = "\n";

		for(Map.Entry pair : SmallCC.labelTable.entrySet())
		{
			String label = "" + pair.getValue();
			String value = "" + pair.getKey();

			String comm = "";
			if(value.length() == 5)
			{
				comm = "Pointer " + ADDR_DECOD(value);
			}
			else
			{
				comm = "Value " + value;
			}

			Optimizer.addInstruction(comm, label, "DCW", value);
			code += INS(comm, label, "DCW", value);

			// code = code + ("     " + pair.getValue() + "    DCW  " + pair.getKey() + "\n");
		}

		code += "\n";

		return code;
	}

	private static String loadHeader()
	{
		String fileName = "snippets/header.s";
		String code = "";

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			String line;
			while((line = reader.readLine()) != null )
			{
				code += line + "\n";
			}
		}
		catch (Exception e)
		{
			return "";
		}

		return code;
	}

	private static String loadSnippet(String snippetName)
	{
		String line = "";
		String code = "";
		String fileName = "snippets/" + snippetName + ".s";

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			while((line = reader.readLine()) != null )
			{
				code += line + "\n";
			}

			// the snippet may contain labels of the form $ABC replace them with real labels.
			String mainLabel = "";
			Matcher m = Pattern.compile("\\$[A-Z ]{5}").matcher(code);
			while (m.find())
			{
				String group = m.group();
				String label = label(SmallCC.nextLabelNumber());
				//first replace occurrences in the label column, preserving spacing
				code = code.replace(group, label);
				//then replace occurrences in the arguments, ignoring surrounding whitespace
				code = code.replace(group.trim(), label);
				if (group.equals("$MAIN ")) mainLabel = label;

				m = Pattern.compile("\\$[A-Z0-9 ]{5}").matcher(code);
			}

			snippetLabels.put(snippetName, mainLabel);
			snippetCode.put(snippetName, code);
		}
		catch (Exception e)
		{
			return "";
		}

		return code;
	}

	public static String SNIP(String snippetName)
	{
		String label;

		if (!snippetLabels.containsKey(snippetName))
		{
			String s = loadSnippet(snippetName);
			if (s.equals(""))
			{
				System.out.println("Missing " + snippetName);
			}
		}

		label = snippetLabels.get(snippetName);
		Optimizer.addSnippet(snippetName);
		Optimizer.addInstruction("Jump to snippet " + snippetName, "", "B", Snippet.getSnippetLabel(snippetName));
		return INS("Jump to snippet " + snippetName, null, "B", label);
	}

	// this must be called AFTER everything else, including FOOTER.
	public static String HEADER()
	{
		String code = "     ****************************************************************\n";

		if(SmallCC.autocoderFile.length() > 0)
		{
			code += "     ***  " + SmallCC.autocoderFile + "\n";
		}

		code += "     ***  Generated by Small-C Compiler on " + SmallCC.compilationTime + "\n";

		code += "     ****************************************************************\n";
		Optimizer.addSnippet("header");
		code += loadHeader();

		return code;
	}

	public static String SET_STACK()
	{
		String code = "";
		Optimizer.addInstruction("SET THE STACK POINTER (STACK GROWS UPWARD)","","");
		code += COM("SET THE STACK POINTER (STACK GROWS UPWARD)");

		// The stack pointer must point to last used location or the previous one
		Optimizer.addInstruction("X2 is the stack pointer", "", "SBR", "X2", Integer.toString(SmallCC.stackMem - 1));
		code += INS("X2 is the stack pointer", null, "SBR", "X2", Integer.toString(SmallCC.stackMem - 1));
		Optimizer.addInstruction("Copy X2 to X3", "", "MCW", "X2", "X3");
		code += INS("Copy X2 to X3", null, "MCW", "X2", "X3");
		code += "\n";

		return code;
	}

	public static String SET_CODE()
	{
		String code = "";
		Optimizer.addInstruction("SET THE START POSITION OF CODE","","");
		code += COM("SET THE START POSITION OF CODE");

		if(SmallCC.codeMem == 0)
		{
			Optimizer.addInstruction("", "", "ORG", Integer.toString(SmallCC.dataMem + staticInitializersSize));
			code += INS(null, null, "ORG", Integer.toString(SmallCC.dataMem + staticInitializersSize));
		}
		else
		{
			Optimizer.addInstruction("", "", "ORG", Integer.toString(SmallCC.codeMem));
			code += INS(null, null, "ORG", Integer.toString(SmallCC.codeMem));
		}
		Optimizer.addInstruction("Program starts here", "START", "NOP");
		code += INS("Program starts here", "START", "NOP");
		code += "\n";

		return code;
	}

	public static String SET_VARDATA(List<Initializer> initializers) throws Exception
	{
		String code = "";
		Optimizer.addInstruction("SET THE START POSITION OF VARIABLES INITIALIZATION DATA","","");
		code += COM("SET THE START POSITION OF VARIABLES INITIALIZATION DATA");

		staticInitializersSize = 0;

		// Sort initializers by ascending memory location...
		int count = initializers.size();
		for (int idx = 0; idx < count - 1; idx++)
		{
			int offset = initializers.get(idx).getVariable().getOffset();
			for (int idx2 = idx + 1; idx2 < count; idx2++)
			{
				int offset2 = initializers.get(idx2).getVariable().getOffset();
				if(offset > offset2)
				{
					Initializer init = initializers.get(idx);
					Initializer init2 = initializers.get(idx2);

					initializers.set(idx, init2);
					initializers.set(idx2, init);

					offset = offset2;
				}
			}
		}

		for (Initializer init : initializers)
		{
			code += init.generateCode();

			staticInitializersSize += init.getInitializerSize();
		}

		code += "\n";

		return code;
	}

	public static String FOOTER()
	{
		String code = "";

		for (Map.Entry<String,String> entry : snippetCode.entrySet())
		{
			code += entry.getValue();
		}

		code += loadVariables();

		return code;
	}

	public static String label(int labelNumber)
	{
		// all our generated labels will start with a L

		String label = "L";
		while (label.length() < 6)
		{
			label += (char)('A' + labelNumber % 26);
			labelNumber /= 26;
		}

		return label;
	}
}
