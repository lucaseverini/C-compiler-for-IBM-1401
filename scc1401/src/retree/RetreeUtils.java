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
			line += args[i];
		}
		return line + "\n";
	}

	public static String INS(String operation, String ... args) {
		return LBL_INS("", operation, args);
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
		String[] digits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "'", "/", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"!", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "?", "A", "B", "C", "D", "E", "F", "G", "H", "I"};
		int lastDigitSet = addr / 4000;
		return digits[addr / 100] + (addr / 10) % 10 + digits[10 * lastDigitSet + addr%10];
	}

	public static String PUSH(String a) {
		return INS("MCW", a, "X2") +
			INS("SBR", "X2", "5+X2");
	}

	public static String POP(String location) {
		/*
		return INS("MCW", "0+X2", location) +
			INS("A", "@15995@", "X2");
			*/
			return "";
	}

	public static String STACK_REF(int back) {
		// return RELADDR(
		return "";
	}

}
