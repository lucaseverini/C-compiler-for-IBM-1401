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

		while(true) {
			if(tok == null) {
				long tokenStart = t.getStartPositon();
				long lac = ((65536L << 32) | 583);
				long line = (lac >> 32);
				long col = ((lac << 32) >> 32);
				System.out.println("Invalid token at line:" + line + " col:" + col);
				System.out.println("tokenStart:" + tokenStart);
			}
			else {
				if(tok.getType() == Token.TokenType.eof) {
					break;
				}
				else {
					System.out.println(tok);				
				}
			}
			tok = t.nextToken();
		}
		System.out.println(tok);

		long lac = ((65536L << 32) | 583);
		Tokenizer.printLineAndCol(lac);
	}
}
