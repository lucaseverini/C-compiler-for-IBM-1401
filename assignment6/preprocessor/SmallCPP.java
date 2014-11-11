/* SmallCPP.java */
/* Generated By:JavaCC: Do not edit this line. SmallCPP.java */
import java.io.*;
import java.util.*;

public class SmallCPP implements SmallCPPConstants {
        static PreProcSymTab symTable = null;
        static boolean ignore = false;
        static int filePos = 0;
        static ReaderWriter rw;

    public static void main(String[] args) throws Exception
    {
                System.out.println("Small-C Pre-processor");

                String inFile = args[0];

                String outFile = preprocessFile(inFile);

                System.out.println("Preprocessed file to be parsed: " + outFile);
    }

        static String preprocessFile(String inFile) throws Exception
        {
                filePos = 0;

                String outFile = inFile + ".preproc";

                File file = new File(inFile);

                System.out.println("Parsing file " + inFile + " size:" + file.length());

                Reader sr = new FileReader(file)
                {
                        private int[] lookahead = {-1, -1};

                        public int read() throws IOException
                        {
                                if (lookahead[0] >= 0)
                                {
                                        int ch = lookahead[0];
                                        lookahead[0] = lookahead[1];
                                        lookahead[1] = -1;
                                        return ch;
                                }

                                int ch = super.read();
                                if (ch >= 0)
                                {
                                        filePos++;
                                }

                                // possible line continuation
                                if (ch == '\u005c\u005c')
                                {
                                        lookahead[0] = super.read();
                                        if(lookahead[0] >= 0)
                                        {
                                                filePos++;
                                        }

                                        if (lookahead[0] == '\u005cr')
                                        {
                                                lookahead[1] = super.read();
                                                if(lookahead[1] >= 0)
                                                {
                                                        filePos++;
                                                }

                                                if (lookahead[1] != '\u005cn')
                                                {
                                                        return ch;
                                                }
                                                else
                                                {
                                                        lookahead[0] = '\u005cn';
                                                }
                                        }

                                        if (lookahead[0] == '\u005cn')
                                        {
                                                lookahead[0] = lookahead[1] = -1;
                                                do
                                                {
                                                        ch = super.read();
                                                        if(ch >= 0)
                                                        {
                                                                filePos++;
                                                        }

                                                        if (ch < 0)
                                                        {
                                                                return ch;
                                                        }
                                                }
                                                while(Character.isWhitespace((char) ch));

                                                return ch;
                                        }
                                }
                                else if (ch == '\u005cr')
                                {
                                        lookahead[0] = super.read();
                                        if(lookahead[0] >= 0)
                                        {
                                                filePos++;
                                        }

                                        if (lookahead[0] == '\u005cn')
                                        {
                                                lookahead[0] = lookahead[1] = -1;
                                                return '\u005cn';
                                        }
                                }

                                return ch;
                        }

                        public int read(char[] cbuf, int offset, int length) throws IOException
                        {
                                int read = 0;
                                while (read < length && offset + read < cbuf.length)
                                {
                                        int ch = read();
                                        if (ch < 0)
                                        {
                                                break;
                                        }

                                        if(!ignore)
                                        {
                                                cbuf[offset + (read++)] = (char)ch;
                                        }
                                }

                                if (read == 0 && read < length)
                                {
                                        return -1;
                                }

                                return read;
                        }
        };

                SmallCPP parser = new SmallCPP(sr);

                symTable = new PreProcSymTab();

                rw = new ReaderWriter(inFile, outFile);

        try
                {
            parser.Tokens();

                        System.out.println("### File parsed successfully. " + filePos + " char(s) read.");

                        System.out.println("### Symbol table:\u005cn" + symTable);
        }
        catch (ParseException ex)
                {
                        System.out.println("### Parsing error! " + filePos + " char(s) read.");

                        ex.printStackTrace();
                }

                return outFile;
        }

  static final public void Comment(StringBuffer buf) throws ParseException {
    jj_consume_token(COMMENTS);

  }

  static final public void Statement(StringBuffer buf) throws ParseException {
    jj_consume_token(STATEMENTS);
int beginLine = token.beginLine;
                int endLine = token.endLine;
                int beginCol = token.beginColumn;
                int endCol = token.endColumn;
                String position = String.format("%d:%d - %d:%d", beginLine, beginCol, endLine, endCol);

                String statement = null, name = null, value = null;
        StringTokenizer tokenizer = new StringTokenizer(token.image);

                statement = tokenizer.nextToken(" ").trim();

                if(tokenizer.hasMoreTokens())
                {
                        name = tokenizer.nextToken(" ").trim();
                }

                if(tokenizer.hasMoreTokens())
                {
                        value = tokenizer.nextToken("\u005cr\u005cn").trim();
                }
/*
		System.out.println("position: " + position);
		System.out.println("statement: " + statement);
		System.out.println("name: " + name);
		System.out.println("value: " + value);
*/
                if(statement.equals("#include"))
                {
                        if(!ignore)
                        {
                                while((value = symTable.getValue(name)) != null)
                                {
                                        name = value;
                                }

                                System.out.println("##### INCLUDE " + name + " at " + position + " #####");
                        }
                }
                else if(statement.equals("#define"))
                {
                        symTable.setValue(name, value);
                }
                else if(statement.equals("#undef"))
                {
                        symTable.remove(name);
                }
                else if(statement.equals("#ifdef"))
                {
                        if(!ignore)
                        {
                                ignore = !symTable.isDefined(name);
                                if(ignore)
                                {
                                        System.out.println("##### BEGIN IGNORE at " + position + " #####");
                                }
                        }
                }
                else if(statement.equals("#ifndef"))
                {
                        if(!ignore)
                        {
                                ignore = symTable.isDefined(name);
                                if(ignore)
                                {
                                        System.out.println("##### BEGIN IGNORE at " + position + " #####");
                                }
                        }
                }
                else if(statement.equals("#else"))
                {
                        if(!ignore)
                        {
                                ignore = true;
                                System.out.println("##### BEGIN IGNORE at " + position + " #####");
                        }
                        else
                        {
                                ignore = false;
                                System.out.println("##### END IGNORE at " + position + " #####");
                        }
                }
                else if(statement.equals("#endif"))
                {
                        if(ignore)
                        {
                                ignore = false;
                                System.out.println("##### END IGNORE at " + position + " #####");
                        }
                }

                // System.out.println();

                buf.append("Type: Reserved word").append(", Value: ").append(token.image).append("\u005cn");
  }

  static final public void Identifier(StringBuffer buf) throws ParseException {
    jj_consume_token(IDENTIFIER);
int beginLine = token.beginLine;
                int endLine = token.endLine;
                int beginCol = token.beginColumn;
                int endCol = token.endColumn;
                String position = String.format("%d:%d - %d:%d", beginLine, beginCol, endLine, endCol);

                if(!ignore)
                {
                        String value = symTable.getValue(token.image);
                        if(value != null)
                        {
                                System.out.println("##### REPLACE \u005c"" + token.image + "\u005c" with \u005c"" + value + "\u005c" at " + position + " #####");
                        }
                }

        buf.append("Type: Identifier").append(", Value: ").append(token.image).append("\u005cn");
  }

  static final public void String(StringBuffer buf) throws ParseException {
    jj_consume_token(STRINGS);
buf.append("Type: String").append(", Value: ").append(token.image).append("\u005cn");
  }

  static final public void Asm_block(StringBuffer buf) throws ParseException {
    jj_consume_token(ASM_BLOCK);
buf.append("Type: AsmBlock").append(", Value: ").append(token.image).append("\u005cn");
  }

  static final public void Value(StringBuffer buf) throws ParseException {
    jj_consume_token(VALUE);
buf.append("Type: Number").append(", Value: ").append(token.image).append("\u005cn");
  }

  static final public void Tokens() throws ParseException {StringBuffer sb = new StringBuffer();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case COMMENTS:
      case STRINGS:
      case STATEMENTS:
      case IDENTIFIER:
      case ASM_BLOCK:
      case VALUE:
      case WHITESPACE:
      case SYMBOLS:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case STATEMENTS:{
        Statement(sb);
        break;
        }
      case IDENTIFIER:{
        Identifier(sb);
        break;
        }
      case STRINGS:{
        String(sb);
        break;
        }
      case VALUE:{
        Value(sb);
        break;
        }
      case ASM_BLOCK:{
        Asm_block(sb);
        break;
        }
      case COMMENTS:{
        Comment(sb);
        break;
        }
      case SYMBOLS:{
        jj_consume_token(SYMBOLS);
        break;
        }
      case WHITESPACE:{
        jj_consume_token(WHITESPACE);
        break;
        }
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(0);
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public SmallCPPTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[2];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x2b132,0x2b132,};
   }

  /** Constructor with InputStream. */
  public SmallCPP(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public SmallCPP(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new SmallCPPTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public SmallCPP(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new SmallCPPTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public SmallCPP(SmallCPPTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(SmallCPPTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[18];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 2; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 18; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}
