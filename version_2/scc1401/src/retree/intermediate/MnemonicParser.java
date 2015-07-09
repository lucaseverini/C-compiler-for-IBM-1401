package retree.intermediate;

public class MnemonicParser implements ISnippetParser {

    @Override
    public String parse(Instruction i, String line) {
        
        String mnemonic = "";
        int index = 0;
        // full line comments handler
        if (line.charAt(index) == '*')
        {
            i.setLineComment("     " + line.substring(1));
            return "";
        }
        while(Character.isUpperCase(line.charAt(index)))
        {
            mnemonic += line.charAt(index);
            index ++;
        }
        // make sure that the mnemonic is less than or eq to 3 chars
        if (index > 3)
        {
            System.out.println(mnemonic + " : " +index);
            return null;
        } else 
        {
            int numSpaces = 5 - mnemonic.length();
            index += numSpaces;
            i.setMnemonic(mnemonic);
            if (mnemonic.equals("H"))
            {
                return "";
            }
            return line.substring(index);
        }
    }

}
