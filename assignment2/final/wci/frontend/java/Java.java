/*
	Java.java

    Assignment #2 - CS153 - SJSU
	By Sean Papay, Matt Pleva, Luca Severini 
	September-14-2014
*/

package wci.frontend.java;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class Java {
	public static void main (String[] args) {
		File f = null;
		String fileContents = "";
		ArrayList<String> lines = new ArrayList<String>();
		try {
			f = new File(args[1]);
			Scanner s = new Scanner(f);

			while(s.hasNextLine()) {
				String l = s.nextLine();
				fileContents += l + "\n";
				lines.add(l);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Tokenizer t = new Tokenizer(fileContents);
		Token tok = t.nextToken();
		int ln = 0;
		while(tok != null && tok.getType() != Token.TokenType.eof) {
			long lineAndCol = t.packLineAndCols(t.getLastPosition());
			//t.printLineAndCol(lineAndCol);
			int line = (int) Tokenizer.getLine(lineAndCol);
			int col = (int) Tokenizer.getCol(lineAndCol);
			while (line > ln) {
				System.out.printf("%03d %s\n", ln + 1, lines.get(ln));
				ln++;
			}
			System.out.printf(">>> %s\tline=%3d, pos=%2d, text=%s\n", tok.typeString(), line, col, tok.textString());
			//System.out.println(tok + "\n");
			tok = t.nextToken();
		}
		//System.out.println(tok);
	}
}
