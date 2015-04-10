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

	public static String INS(String lineComment, String label, String operation, String ... args)
	{
		if(SmallCC.optimize)
		{
			if(lineComment != null && !lineComment.isEmpty())
			{
				String[] newArgs = new String[args.length + 1];
				
				for(int idx = 0; idx < args.length; idx++)
				{
					newArgs[idx] = args[idx];
				}
				newArgs[args.length] = "* " + lineComment;
				
				Optimizer.addInstruction("", label != null ? label : "", operation, newArgs);
			}
			else
			{
				Optimizer.addInstruction("", label != null ? label : "", operation, args);
			}
		}

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

		if(!noComments && lineComment != null && !lineComment.isEmpty())
		{
			while (line.length() < inlineCommentStart - 1)
			{
				line += " ";
			}

			// Remove any '@' from the inline comment because it can be considered part of the instruction...
			lineComment = lineComment.replace("@", "");

			if(lineComment.charAt(0) != '*')
			{
				lineComment = "* " + lineComment;
			}

			line += lineComment;
		}
		
		return line + "\n";
	}

	public static String COM(String comment)
	{
		if(SmallCC.optimize)
		{
			Optimizer.addInstruction(comment, "", "");
		}

		return makeLineComment(comment);
	}

	public static String NUM_CONST(int val, boolean arrayMember)
	{
		String str = "@" + COD(val) + "@";
		
		if(SmallCC.optimize)
		{
			return str;
		}
		else
		{
			String label = SmallCC.getLabelForVariable(str);
			// System.out.println("NUM_CONST: " + str + " : " + label + " : " + (arrayMember ? "ARR" : ""));

			return label;
		}
	}

	public static String ADDR_CONST(int val, boolean arrayMember)
	{
		String str = "@" + ADDR_COD(val) + "@";

		if(SmallCC.optimize)
		{
			return str;
		}
		else
		{
			String label = SmallCC.getLabelForVariable(str);
			return label;
		}
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
			String str = "@" + Character.toUpperCase((char)value) + "@";
			
			if(SmallCC.optimize)
			{
				return str;
			}
			else
			{
				String label = SmallCC.getLabelForVariable(str);
				return label;
			}
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

	public static String PUSH_MCW(int size, String location) 
	{
		String code = "";
		String locValue;
		
		if(size == 0)
		{
			return code;
		}

		if(location.charAt(0) == 'L')
		{
			locValue = SmallCC.getVariableLabelValue(location);
		}
		else
		{
			locValue = location;
		}
		
		code += COM("Push Special (" + location + ":" + size + ")");
		code += INS("Add " + size + " to stack pointer", null, "MA", ADDR_CONST(size, false), "X2");
		code += INS("Move data " + locValue + " in stack", null, "MCW", location, "0+X2");
				
		return code;
	}

	public static String PUSH(int size, String location)
	{
		String code = "";
		String locValue;
		Boolean isMemLocation = false;
		Boolean isRegister = false;
		
		if(size == 0)
		{
			return code;
		}

		if(location.charAt(0) == 'L')
		{
			locValue = SmallCC.getVariableLabelValue(location);
		}
		else
		{
			locValue = location;
			
			if(location.charAt(0) == 'X')
			{
				isRegister = true;
			}
			else if(location.charAt(0) != '@')
			{
				isMemLocation = true;
			}
		}

		code += COM("Push (" + location + ":" + size + ")");
		code += INS("Add " + size + " to stack pointer", null, "MA", ADDR_CONST(size, false), "X2");
		
		if(isRegister)
		{
			code += INS("Load " + locValue + " in stack", null, "LCA", location, "0+X2");
		}
		else if(isMemLocation)
		{
			code += INS("Load memory " + locValue + " in stack", null, "LCA", location, "0+X2");
		}
		else
		{
			code += INS("Load data " + locValue + " in stack", null, "LCA", location, "0+X2");
		}

		return code;
	}

	public static String PUSH(int size)
	{
		String code = "";
		
		if(size == 0)
		{
			return code;
		}

		code += COM("Push (" + size + ")");
		code += INS("Add " + size + " to stack pointer", null, "MA", ADDR_CONST(size, false), "X2");

		return code;
	}

	public static String POP(int size, String location)
	{
		String code = "";
		String locValue;

		if(size == 0)
		{
			return code;
		}

		if(location.charAt(0) == 'L')
		{
			locValue = SmallCC.getVariableLabelValue(location);
		}
		else
		{
			locValue = location;
		}

		code += COM("Pop (" + location + ":" + size + ")");
		code += INS("Load stack in " + locValue, null, "LCA", "0+X2", location);
		code += INS("Add " + -size + " to stack pointer", null, "MA", ADDR_CONST(-size, false), "X2");

		return code;
	}

	public static String POP(int size)
	{
		String code = "";
		
		if(size == 0)
		{
			return code;
		}

		code += COM("Pop (" + size + ")");
		code += INS("Add " + -size + " to stack pointer", null, "MA", ADDR_CONST(-size, false), "X2");

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

	public static Boolean IS_LABEL(String str) 
	{
		return (str.charAt(0) == 'L');
	}
		
	public static String makeLineComment(String comment)
	{
		if(comment.length() == 0 || noComments)
		{
			return "";
		}

		if(comment.charAt(0) != '*')
		{
			comment = "* " + comment;
		}

		return "     " + comment + "\n";
	}

	private static String loadVariables()
	{
		String code = "\n";
		
		Map<String, String> sortedMap = sortMapByValue(SmallCC.labelTable);
		for(Map.Entry pair : sortedMap.entrySet())
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

			code += INS(comm, label, "DCW", value);
		}

		code += "\n";

		return code;
	}

	private static String loadHeader()
	{
		String fileName = "snippets/header.s";
		String code = "";

		if(SmallCC.optimize)
		{
			Optimizer.addSnippet("header");
		}

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
		
		if(SmallCC.optimize)
		{
			Optimizer.addSnippet(snippetName);
		}
		
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

		// label = snippetLabels.get(snippetName);
		label = Snippet.getSnippetLabel(snippetName);
		
		String code = INS("Jump to snippet " + snippetName, null, "B", label);
		code += "\n";
		
		return code;
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
		
		code += loadHeader();

		return code;
	}

	public static String SET_STACK()
	{
		String code = "";

		code += COM("SET X2 TO BE THE STACK POINTER (STACK GROWS UPWARD)");
		
		// The stack pointer must point to last used location or the previous one
		code += INS("Set X2 to stack pointer value", null, "SBR", "X2", Integer.toString(SmallCC.stackMem - 1));
		code += INS("Copy stack pointer in X3", null, "MCW", "X2", "X3");
		code += "\n";

		return code;
	}

	public static String SET_CODE()
	{
		String code = "";

		code += COM("START POSITION OF PROGRAM CODE");

		if(SmallCC.codeMem == 0)
		{
			code += INS(null, null, "ORG", Integer.toString(SmallCC.dataMem + staticInitializersSize));
		}
		else
		{
			code += INS(null, null, "ORG", Integer.toString(SmallCC.codeMem));
		}

		code += INS("Program starts here", "START", "NOP");
		code += "\n";

		return code;
	}

	public static String SET_VARDATA(List<Initializer> initializers) throws Exception
	{
		String code = "";

		code += COM("GLOBAL/STATIC DATA AND VARIABLES");

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
	
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValue(Map<K, V> map)
	{
        List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<K, V>>() 
		{
			@Override
			@SuppressWarnings("unchecked")
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) 
			{
                return o1.getValue().compareTo(o2.getValue());
            }
        });
      
        // LinkedHashMap will keep the keys in the order they are inserted which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K, V>();
      
        for(Map.Entry<K, V> entry: entries)
		{
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }
	
	@SuppressWarnings("unchecked")
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortMapByKey(Map<K, V> map)
	{
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);
      
        // LinkedHashMap will keep the keys in the order they are inserted which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        for(K key: keys)
		{
            sortedMap.put(key, map.get(key));
        }
      
        return sortedMap;
    }
}
