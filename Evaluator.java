import java.util.*;

public class Evaluator {
  private final Sentence expression;
  private final Set<String> atomicPropositions;

  public Evaluator(Sentence expression) {
    this.expression = expression;
    this.atomicPropositions = extractAtomicPropositions(expression); // gets all atomic propositions like TRUE, FALSE, P, Q, S
  }

  // prints out the truth table
  public void generateTruthTable() {
    List<String> variables = new ArrayList<>(atomicPropositions);
    int rows;
    if (variables.contains("Q") || variables.contains("P") || variables.contains("S")) {
      int varCount = variables.size();
      if(variables.contains("TRUE")){ // TRUE and FALSE have fixed values, therefore does not affect row count
        varCount--;
      }
      if(variables.contains("FALSE")){
        varCount--;
      }
      rows = (int) Math.pow(2, varCount);
    } else {
      rows = 1;
    }
      
    // smaller proposition instances
    Set<String> intermediateColumns = new LinkedHashSet<>();

    // get the values of all the variables and intermediate propositions
    /*
     * Suppose P AND (Q OR S)
     * An intermediate column would be (Q OR S)
     */
    // getting the columns by pre-solving the problem may be innefficient
    for (int i = 0; i < rows; i++) {
      // Provide boolean values to variables
      Map<String, Boolean> truthAssignment = createTruthAssignment(variables, i);
      Map<String, Boolean> intermediateResults = new LinkedHashMap<>();

      // evaluate the expression and get intermediate results
      Interpreter interpreter = new Interpreter(truthAssignment, intermediateResults);
      expression.accept(interpreter);

      // save the column names
      intermediateColumns.addAll(intermediateResults.keySet());
    }

    // Prepare the table header
    List<String> headers = new ArrayList<>();
    headers.addAll(variables);
    headers.addAll(intermediateColumns);

    // Print the truth table
    printLine(headers);
    printRow(headers);
    printLine(headers);

    // Print each row
    for (int i = 0; i < rows; i++) {
      // Provide boolean values to variables
      Map<String, Boolean> truthAssignment = createTruthAssignment(variables, i);
      Map<String, Boolean> intermediateResults = new LinkedHashMap<>();
      
      // evaluate the expression and get intermediate results
      Interpreter interpreter = new Interpreter(truthAssignment, intermediateResults);
      expression.accept(interpreter);

      // instead of saving columns, we now print the actual values of the row
      List<String> row = new ArrayList<>();
      for (String var : variables) {
        row.add(truthAssignment.get(var) ? addPadding("T", var) : addPadding("F", var));
      }
      for (String column : intermediateColumns) {
        row.add(intermediateResults.get(column) ? addPadding("T", column) : addPadding("F", column));
      }
      printRow(row);
    }
    printLine(headers);
  }

  // adds padding to the values to view it better
  private String addPadding(String value, String goal) {
    int padding = (goal.length() - 1) / 2;
    String paddedValue = String.format("%" + (padding + 1) + "s", value);
    paddedValue = String.format("%-" + goal.length() + "s", paddedValue);
    return paddedValue;
  }

  // gives the values of the variables
  private Map<String, Boolean> createTruthAssignment(List<String> variables, int row) {
    Map<String, Boolean> truthAssignment = new HashMap<>();
    for (int j = 0; j < variables.size(); j++) {
      boolean value = (row & (1 << j)) != 0;
      if (variables.get(j).compareTo("TRUE") == 0){
        value = true;
      } else if (variables.get(j).compareTo("FALSE") == 0){
        value = false;
      }
      truthAssignment.put(variables.get(j), value);
    }
    return truthAssignment;
  }


  // gets all variables including TRUE, FALSE
  private Set<String> extractAtomicPropositions(Sentence sentence) {
    Set<String> atomicSet = new HashSet<>();
    sentence.accept(new Sentence.Visitor<Void>() {
      @Override
      public Void visitAtomicSentence(Sentence.Atomic sentence) {
        atomicSet.add(sentence.value);
        return null;
      }

      @Override
      public Void visitUnarySentence(Sentence.Unary sentence) {
        sentence.right.accept(this);
        return null;
      }

      @Override
      public Void visitBinarySentence(Sentence.Binary sentence) {
        sentence.left.accept(this);
        sentence.right.accept(this);
        return null;
      }

      @Override
      public Void visitGroupingSentence(Sentence.Grouping sentence) {
        sentence.expression.accept(this);
        return null;
      }
    });
    return atomicSet;
  }

  // prints the formatting +-------+
  private void printLine(List<String> headers) {
    for (String header : headers) {
      System.out.print("+");
      System.out.print("-".repeat(header.length() + 2));
    }
    System.out.println("+");
  }

  // prints the rows
  private void printRow(List<String> values) {
    for (String value : values) {
      System.out.print("| ");
      System.out.print(value);
      System.out.print(" ");
    }
    System.out.println("|");
  }
}