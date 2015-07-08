package retree.intermediate;

public class LabelParser implements ISnippetParser {

	@Override
	public String parse(Instruction i,String line) {
		// index into the string
		int index = 0;
		// label for the instruction 
		String lbl = "";
		// is the start of line valid? yes.
		boolean passed = true;
		// go through the 1st 5 chars and make sure that the line is valid
		while (index < 5) {
			// does the line start with spaces?
			if (line.charAt(index) == ' ') {
				index++;
			} else {
				passed = false;
				break;
			}
		}
		if (!passed) {
			// line is not valid
			return null;
		} else {
			if (line.charAt(index) == '*')
			{
				return line.substring(5);
			}
			// line is valid so far now check the label
			int labelSize = index; // setup label size
			while (Character.isDigit(line.charAt(index)) || Character.isUpperCase(line.charAt(index))) {
				lbl += line.charAt(index); // add each char to label
				index++;
			}
			// find label size
			labelSize = index - labelSize;
			if (line.charAt(index) != ' ' && labelSize < 6) {
				// invalid label
				return null;
			} else {
				// valid label skip to end of max label size
				int numSpaces = 4 + (6 - labelSize) + index;
				// go to start of mnemonic
				while (line.charAt(index) == ' ' && index < numSpaces) {
					index++;
				}
				if (index < numSpaces) {
					// make sure we reach that position
					return null;
				} else {
					// valid line
					// set label
					i.setLabel(lbl);
					// return string missing 1st 15 chars
					return line.substring(15);
				}
			}
		}
	}

}
