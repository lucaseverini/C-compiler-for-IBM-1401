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

			while(s.hasNextLine())
				fileContents += s.nextLine() + "\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		Tokenizer t = new Tokenizer(fileContents);
		Token tok = t.nextToken();
		while(tok == null || tok.getType() != Token.TokenType.eof) {
			if(tok == null) {
				int line = 
				int col = 
				System.out.println("Invalid token at line " + line + " col " + col);
			}
			else {
				System.out.println(tok);
			}
			tok = t.nextToken();
		}
		System.out.println(tok);
	}
}
