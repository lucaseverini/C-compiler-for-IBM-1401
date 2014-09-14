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
		File f = null;
		String fileContents = "";
		try {
			f = new File("test.txt");
			Scanner s = new Scanner(f);

			while(s.hasNextLine()) {
				fileContents += s.nextLine() + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Tokenizer t = new Tokenizer(fileContents);
		Token tok = t.nextToken();
		while(tok != null && tok.getType() != Token.TokenType.eof) {
			long lineAndCol = t.packLineAndCols(t.getLastPosition());
			t.printLineAndCol(lineAndCol);
			System.out.println(tok + "\n");
			tok = t.nextToken();
		}
		//System.out.println(tok);
	}
}
