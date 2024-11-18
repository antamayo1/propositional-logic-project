import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

  // Main function that runs the prompt
  public static void main(String[] args) throws IOException {
    runPrompt();
  }

  // Function that gets input prompt
  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    System.out.println("-----------------------------------------------------------------");
    System.out.println("Welcome to LOGIC sentence.pl");
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
    Scanner scanner = new Scanner(source);         // initialize scanner
    List<Token> tokens = scanner.scanTokens();     // tokenize the input string
    Parser parser = new Parser(tokens);            // initialse parser
    Sentence sentence = parser.Parse();            // parse the tokens into a sentence representence by a tree
    Evaluator evaluator = new Evaluator(sentence); // initialize evaluator
    evaluator.generateTruthTable();                // generate the truth table from the sentence tree
  }

  static void reportError(int line, String where,String message) {
    System.err.println("[line " + line + "] Error at " + where + ": " + message);
    System.exit(65);
  }

}