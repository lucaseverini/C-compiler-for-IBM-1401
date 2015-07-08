package retree.intermediate;

public interface ISnippetParser {
	// Returns a string that is a substring of line that got consumed in this stage
	// Takes an Instruction to modify while running
	public String parse(Instruction i,String line);
}
