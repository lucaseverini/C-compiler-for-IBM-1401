/*
	Tester.java

    Assignment #2 - CS153 - SJSU
	By Sean Papay, Matt Pleva, Luca Severini
	September-14-2014
*/

package wci.frontend.java;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;

public class Tester {
	public static void main (String[] args) {

		// Get a file and a place to store the file contents
		File f = null;
		String fileContents = "";
		try {
			// open the file
			f = new File(args[0]);
			Scanner s = new Scanner(f);
			// populate the string with the file contents
			while(s.hasNextLine()) {
				fileContents += s.nextLine() + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Create a new tokenizer and use the fileContents as a source of tokens
		Tokenizer t = new Tokenizer(fileContents);
		// Keep getting the next token and print the token out so we can see / debug it
		Token tok = t.nextToken();
		while(tok != null && tok.getType() != Token.TokenType.eof) {
			long lineAndCol = t.packLineAndCols(t.getLastPosition());
			t.printLineAndCol(lineAndCol);
			System.out.println(tok + "\n");
			tok = t.nextToken();
		}
	}
}
