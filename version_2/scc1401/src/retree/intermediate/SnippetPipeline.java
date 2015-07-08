package retree.intermediate;

import java.util.ArrayList;

public class SnippetPipeline {
	// This class starts and runs the snippet parser and takes care of metadata
	// about the instructions
	public static ArrayList<ISnippetParser> stages = new ArrayList<ISnippetParser>();

	public static void main(String[] args) {
		ArrayList<String> file = new ArrayList<String>();
		file.add("     READ      EQU  001\n");
		file.add("     PUNSIZ    DCW  @080@\n");
		file.add("               C    0+X2,15999+X2\n");
		file.add("               BCE  LNEAAA,5+X2, \n");
		file.add("               MZ   *-22,*-21\n");
		file.add("     * Less (value < 0)\n");
		stages.add(new LabelParser());
		stages.add(new MnemonicParser());
		stages.add(new ArgumentParser());
		ArrayList<Instruction> l = start(file);
		for(int i = 0; i < l.size(); i ++)
		{
			System.out.println(l.get(i));
		}
	}

	public static ArrayList<Instruction> start(ArrayList<String> filecon) {
		ArrayList<Instruction> list = new ArrayList<Instruction>();
		for (int i = 0; i < filecon.size(); i++) {
			Instruction in = new Instruction();
			String line = filecon.get(i);
			for (int j = 0; j < stages.size(); j++) {
				if (line == null)
				{
					return null;
				}
				line = stages.get(j).parse(in, line);
				System.out.println(line);
			}
			list.add(in);
		}
		return list;
	}
}
