/**
 * <h1>CrossReferencer</h1>
 *
 * <p>Generate a cross-reference listing.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */

package wci.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import wci.intermediate.*;
import wci.intermediate.symtabimpl.DefinitionImpl;
import static wci.intermediate.symtabimpl.SymTabKeyImpl.*;
import static wci.intermediate.typeimpl.TypeKeyImpl.RECORD_SYMTAB;

public class CrossReferencer
{
    private static final int NAME_WIDTH = 16;

    private static final String NAME_FORMAT        = "%-" + NAME_WIDTH + "s";
    private static final String NAME_LABEL         = "Identifier      ";
    private static final String NAME_UNDERLINE     = "----------      ";
	private static final String TYPE_LABEL         = "Type Information   ";
    private static final String TYPE_UNDERLINE     = "----------------   ";
	private static final String VALUE_LABEL        = "[Value]   ";
    private static final String VALUE_UNDERLINE    = "-------   ";
    private static final String NUMBERS_LABEL      = "Line numbers";
    private static final String NUMBERS_UNDERLINE  = "------------";
    private static final String NUMBER_FORMAT = "%03d ";
    private static final String TYPE_FORMAT = "%s   ";
 
    private static final int LABEL_WIDTH  = NUMBERS_LABEL.length();
    private static final int INDENT_WIDTH = NAME_WIDTH + LABEL_WIDTH;
	
	private static final String CONSTANT_FORMAT = "{CONSTANT_VALUE=%s}   ";
	private static final String VALUE_FORMAT = "{DATA_VALUE=%s}   ";

    private static final StringBuilder INDENT = new StringBuilder(INDENT_WIDTH);
    static {
        for (int i = 0; i < INDENT_WIDTH; ++i) INDENT.append(" ");
    }

    /**
     * Print the cross-reference table.
     * @param symTabStack the symbol table stack.
     */
    public void print(SymTabStack symTabStack)
    {
        System.out.println("\n===== CROSS-REFERENCE TABLE =====");
        printColumnHeadings();

        printSymTab(symTabStack.getLocalSymTab());
    }

    /**
     * Print column headings.
     */
    private void printColumnHeadings()
    {
        System.out.println();
        System.out.println(NAME_LABEL + TYPE_LABEL + VALUE_LABEL + NUMBERS_LABEL);
        System.out.println(NAME_UNDERLINE + TYPE_UNDERLINE + VALUE_UNDERLINE + NUMBERS_UNDERLINE);
    }

    /**
     * Print the entries in a symbol table.
     * @param SymTab the symbol table.
     */
    private void printSymTab(SymTab symTab)
    {
        // Loop over the sorted list of symbol table entries.
        ArrayList<SymTabEntry> sorted = symTab.sortedEntries();
        for (SymTabEntry entry : sorted)
		{
            ArrayList<Integer> lineNumbers = entry.getLineNumbers();

            // For each entry, print the identifier name
            // followed by the line numbers.
		
           System.out.print(String.format(NAME_FORMAT, entry.getName()));

			TypeSpec type = entry.getTypeSpec();			
			System.out.print(String.format(TYPE_FORMAT, entry.getTypeSpec().toString()));

			Definition def = entry.getDefinition();
			if(def == DefinitionImpl.CONSTANT)
			{
				Object value = entry.getAttribute(CONSTANT_VALUE);
				if(value != null)
				{
					System.out.print(String.format(CONSTANT_FORMAT, value.toString()));			
				}
			}
			else if(def == DefinitionImpl.VARIABLE)
			{
				Object value = entry.getAttribute(DATA_VALUE);
				if(value != null)
				{
					System.out.print(String.format(VALUE_FORMAT, value.toString()));			
				}
			}

			if (lineNumbers != null)
			{
                for (Integer lineNumber : lineNumbers)
				{
                    System.out.print(String.format(NUMBER_FORMAT, lineNumber));
                }
            }
            System.out.println();
			
			SymTab recTab = (SymTab)type.getAttribute(RECORD_SYMTAB);
			if(recTab != null)
			{
				ArrayList<SymTabEntry> arr = recTab.sortedEntries();
				for (SymTabEntry e : arr)
				{
					String fldName = e.getName();
					TypeSpec fldtype = e.getTypeSpec();
					System.out.println("                    " + fldName + " " + fldtype.toString());
				}
			}
         }
    }
}
