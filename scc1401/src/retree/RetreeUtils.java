package retree;

public class RetreeUtils {
	public static String LBL_INS(String label, String operation, String ... args) {
		String line = "     ";
		line += label;
		while(line.length() < 15) line += " ";
		line += operation;
		while (line.length() < 20) line += " ";
		for (int i = 0; i < args.length; ++i) {
			if (i > 0) line += ",";
			line += arg;
		}
		return line + "\n";
	}

	public static String INS(String operation, String ... args) {
		return LBL_INS("", operation, args);
	}

	public static String CONST(int val) { return "$" + COD(val) + "$";}
	public static String REG(String val) { return "R" + val;}

	public static String OFF(int offset) {
		if (offset > 0) {
			return "SP+" + COD(offset);
		} else {
			return COD(-offset);
		}
	}

	public static String COD(int val) {
		return val + "";
		//todo

	}
	public static String ADDR_REL(int base, int offset); {
		return COD(base) + "+" + COD(offset);
}
