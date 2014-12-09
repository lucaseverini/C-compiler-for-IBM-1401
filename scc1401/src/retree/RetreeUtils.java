package retree;
import compiler.*;

import java.util.*;
import java.util.regex.*;
import java.io.*;

public class RetreeUtils {

	public static String LBL_INS(String label, String operation, String ... args) {
		String line = "     ";
		line += label;
		while(line.length() < 15) line += " ";
		line += operation;
		while (line.length() < 20) line += " ";
		for (int i = 0; i < args.length; ++i) {
			if (i > 0) line += ",";
			line += args[i];
		}
		return line + "\n";
	}

	public static String INS(String operation, String ... args) {
		return LBL_INS("", operation, args);
	}
	
	public static String COM(String comment) {
		return "     * " + comment + "\n";
		
	}

	public static String CONST(int val) {
			return "@" + COD(val) + "@";
		}

	public static String ADDR_CONST(int val) {
			return "@" + ADDR_COD(val) + "@";
	}


	public static String COD(int val) {
		String s;
		if (val < 0) {
			String[] negDigits = {"!", "J", "K", "L", "M", "N", "O", "P", "Q", "R"};
			val = -val;
			s = (val / 10) + negDigits[val % 10];
		} else {
			s = val + "";
		}
		while (s.length() < 5) s = "0" + s;
		return s;
	}

	public static String ADDR_COD(int addr) {
		String[] digits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"'", "/", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"!", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			 "?", "A", "B", "C", "D", "E", "F", "G", "H", "I"};
		int lastDigitSet = 10 * (addr / 4000);
		int firstDigitSet = 10 * ((addr%4000) / 1000);
		int decPart = addr % 1000;
		return digits[decPart/100 + firstDigitSet] + digits[(decPart/10)%10] + digits[decPart % 10 + lastDigitSet];
		
		//return digits[(addr  4000) / 100] + (addr / 10) % 10 + digits[10 * lastDigitSet + addr%10];
	}

	public static String PUSH(String a) {
		//remember we need to set the word mark for the stack
		return COM("Push(" + a + ")") +
			INS("SW","15996+X2") +
			INS("MCW", a, "0+X2") +
			INS("MA", ADDR_CONST(5), "X2");
	}

	public static String PUSH() {
		//remember we need to set the word mark for the stack
		return COM("Push") + 
			INS("SW","15996+X2") +
			INS("MA", ADDR_CONST(5), "X2");
	}

	public static String POP(String location) {

		//for now we'll just leave the word mark in place, we might remove it later...
		return COM("Pop(" + location + ")") +
			INS("MA", ADDR_CONST(15995), "X2") +
			INS("MCW", "0+X2", location);
	}
	
	public static String POP() {
		return COM("Pop") +
			INS("MA", ADDR_CONST(15995), "X2");

	}

	public static String PUSH_FRAME() {
		return PUSH("X3") +
			INS("MCW", "X2", "X3");
	}

	

	public static String STACK_REF(int back) {

		return (16000-5*back) + "+X2";
	}

	public static String OFF(int offset) {
		if (offset < 0) {
			offset += 16000;
		}
		return offset + "+X3";
	}

	private static HashMap<String, String> snippetLabels = new HashMap<String, String>();

	//this needs to be tested
	private static String loadSnippet(String snippetName) {
		String line;
		String code = "";
		String fileName = "snippets/" + snippetName + ".s";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			while((line = reader.readLine()) != null ) {
				code += line + "\n";
			}
			//the snippet may contain labels of the form $ABC
			//replace them with real labels.
			String mainLabel = "";
			Matcher m = Pattern.compile("\\#[A-Z ]{5}").matcher(code);
			while (m.find()) {
				String group = m.group();
				String label = SmallCC.nextLabel();
				code = code.replaceAll(group, label);
				if (group.equals("#MAIN ")) mainLabel = label;

				m = Pattern.compile("\\#[A-Z ]{5}").matcher(code);
			}
			snippetLabels.put(snippetName, mainLabel);
		} catch (Exception e) {
			return "";
		}
		return code;
	}

	public static String SNIP(String snippetName) {
		String label;
		if (!snippetLabels.containsKey(snippetName)) {
			loadSnippet(snippetName);
		}
		label = snippetLabels.get(snippetName);
		return INS("B", label);
	}

}
