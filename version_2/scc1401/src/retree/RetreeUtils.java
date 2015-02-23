package retree;

import compiler.*;

import java.util.*;
import java.util.regex.*;
import java.io.*;

public class RetreeUtils 
{
	private static final HashMap<String, String> snippetLabels = new HashMap<>();
	private static final HashMap<String, String> snippetCode = new HashMap<>();

	public static String LBL_INS(String label, String operation, String ... args) 
	{
		String line = "     ";
		line += label;
		
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
		
		return line + "\n";
	}

	public static String INS(String operation, String ... args)
	{
		return LBL_INS("", operation, args);
	}

	public static String COM(String comment) 
	{
		return "     * " + comment + "\n";
	}

	public static String NUM_CONST(int val, boolean arrayMember) 
	{
		String str =  "@" + COD(val) + "@";	

		String label = SmallCC.getLabelForVariable(str);
		// System.out.println("NUM_CONST: " + str + " : " + label + " : " + (arrayMember ? "ARR" : ""));

		return label;
	}

	public static String ADDR_CONST(int val, boolean arrayMember)
	{
		String str = "@" + ADDR_COD(val) + "@";
		
		String label = SmallCC.getLabelForVariable(str);
		// System.out.println("ADDR_CONST: " + str + " : " + label + " : " + (arrayMember ? "ARR" : ""));
		
		return label;
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
			
			String label = SmallCC.getLabelForVariable(str);
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

	public static String ADDR_COD(int addr)
	{
		String[] digits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"'", "/", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"!", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			 "?", "A", "B", "C", "D", "E", "F", "G", "H", "I"};

		addr = (16000 - (-addr) % 16000) % 16000; // addr :=  addr mod 16000
		
		int lastDigitSet = 10 * (addr / 4000);
		int firstDigitSet = 10 * ((addr%4000) / 1000);
		int decPart = addr % 1000;
		
		return digits[decPart/100 + firstDigitSet] + digits[(decPart/10) % 10] + digits[decPart % 10 + lastDigitSet];

		// return digits[(addr  4000) / 100] + (addr / 10) % 10 + digits[10 * lastDigitSet + addr%10];
	}

	public static String PUSH(int size, String a) 
	{
		// remember we need to set the word mark for the stack
		return COM("Push(" + a + ":" + size + ")") +
			   INS("MA", ADDR_CONST(size, false), "X2") +
			   INS("LCA", a, "0+X2");
	}

	public static String PUSH(int size) 
	{
		// remember we need to set the word mark for the stack
		return COM("Push(" + size + ")") + 
			   INS("MA", ADDR_CONST(size, false), "X2");
	}

	public static String POP(int size, String location)
	{
		return COM("Pop(" + location + ":" + size + ")") +
			   INS("LCA", "0+X2", location) +
			   INS("MA", ADDR_CONST(-size, false), "X2");
	}

	public static String POP(int size) 
	{
		return COM("Pop(" + size + ")") + 
			   INS("MA", ADDR_CONST(-size, false), "X2");
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
		offset = (16000 + offset % 16000) % 16000; // offset :=  offset mod 16000
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
			code = code + ("     " + pair.getValue() + "    DCW  " + pair.getKey() + "\n");
		}

		code = code + "\n";	
		
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
		
		return INS("B", label);
	}

	// this must be called AFTER everything else, including FOOTER.
	public static String HEADER() 
	{		
		String code = "     ****************************************************************\n";
		
		if(SmallCC.autocoderFile.length() > 0)
		{
			code = code + "     ***  " + SmallCC.autocoderFile + "\n";
		}
		
		code = code + "     ***  Generated by Small-C Compiler on " + SmallCC.compilationTime + "\n";
		
		code = code + "     ****************************************************************\n";
		
		code = code + loadSnippet("header");
				
		code = code + COM("SET THE START POSITION OF CODE");
		code = code + INS("ORG", Integer.toString(SmallCC.codeMem));
		code = code + LBL_INS("START", "NOP");
		code = code + "\n";
		
		code = code + COM("SET THE STACK POINTER");
		code = code + INS("SBR", "X2", Integer.toString(SmallCC.stackMem));
		code = code + "\n";
		
		code = code + INS("MCW", "X2", "X3");
		
		return code;
	}

	public static String FOOTER() 
	{
		String code = "";
		for (Map.Entry<String,String> entry : snippetCode.entrySet()) 
		{
			code += entry.getValue();
		}
		
		code = code + loadVariables();

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
