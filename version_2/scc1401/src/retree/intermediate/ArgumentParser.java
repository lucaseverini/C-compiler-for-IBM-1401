package retree.intermediate;

public class ArgumentParser implements ISnippetParser {

	@Override
	public String parse(Instruction i, String line) {
		// early exit condition for full line comment
		if (line.equals(""))
		{
			return "";
		}
		int index = 0;
		String arg = "";
		// replace windows line endings with unix line endings
		line = line.replace("\r\n", "\n");
		// while we are not at the end of the string and we have not passed the arguments
		while (index < line.length() && line.charAt(index) != '\n' && line.charAt(index) != ' ') {
			// reset the argument contents
			arg = "";
			// keep building the string until we hit a seperator
			while (line.charAt(index) != ',' && line.charAt(index) != ' ' && line.charAt(index) != '\n') {
				arg += line.charAt(index);
				index++;
			}
			// then add that argument to the instruction
			i.addArgument(arg);
			// only increase index if index is less than the line length
			if (index < line.length())
				index++;
		}
		// now checking for special cases
		
		// if we are not at the end of the line and the char is " " and the previous one is ","
		// like in: BCE  LNEAAA,5+X2, | notice the space at the end
		// then we add the space to the arg list as it is super important
		// otherwise incorrect and really hard to debug behavior ahoy!
		if (index < line.length() && line.charAt(index) == ' ' && line.charAt(index - 1) == ',') {
			arg = " ";
			i.addArgument(arg);
			index++;
		// Adds line comments to the end of the the arg lists
		} else if (index < line.length() && line.charAt(index) == ' ') {

			arg = "";
			while (line.charAt(index) == ' ')
				index++;
			while (line.charAt(index++) != '\n')
				arg += line.charAt(index - 1);
			i.addArgument(arg);
			return "";
		}
		return "";
	}

}
