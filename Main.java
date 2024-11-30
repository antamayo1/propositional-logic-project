import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.io.File; 
import java.io.FileNotFoundException; 
import java.util.Scanner; 

public class Main {

  // Main function that runs the prompt
  public static void main(String[] args) throws IOException {
    System.out.println("-----------------------------------------------------------------");
    System.out.println("Welcome to LOGIC");
    if (args.length > 0){
      System.out.println("Currently running file: " + args[0]);
      System.out.println("-----------------------------------------------------------------");
      try {
        String filename = args[0];
        if(filename.substring(filename.length()-2).compareTo("pl") != 0){
          reportError(0, "File Finder", "File extension not supported.");
        }
        File myObj = new File(args[0]);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
          String data = myReader.nextLine();
          System.out.println("> " + data);
          run(data);
        }
        myReader.close();
        System.out.println("-----------------------------------------------------------------");
        System.out.println("LOGIC terminated...");
      } catch (FileNotFoundException e) {
        System.out.println(args[0] + " not found");
        System.out.println("-----------------------------------------------------------------");
        System.out.println("LOGIC terminated...");
      }
    } else {
      runPrompt();
    } 
  }

  // Function that gets input prompt
  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    System.out.println("Please Enter 'EXIT' to terminate");
    System.out.println("-----------------------------------------------------------------");
    for (;;) { 
      System.out.print("> ");
      String line = reader.readLine();
      if (line.compareTo("EXIT") == 0) break;
      if (line.compareTo("") == 0) continue;
      run(line);
    }
    System.out.println("-----------------------------------------------------------------");
    System.out.println("LOGIC terminated...");
  }

  // Function that runs the given propositional string
  private static void run(String source) {
    TokenScanner scanner = new TokenScanner(source);         // initialize scanner
    List<Token> tokens = scanner.scanTokens();     // tokenize the input string
    Parser parser = new Parser(tokens);            // initialse parser
    Sentence sentence = parser.Parse();            // parse the tokens into a sentence representence by a tree
    Evaluator evaluator = new Evaluator(sentence, source); // initialize evaluator
    evaluator.generateTruthTable();                // generate the truth table from the sentence tree
  }

  static void reportError(int line, String where,String message) {
    System.err.println("[Last prompt] Error at " + where + ": " + message);
    System.exit(65);
  }
}