import java.util.Map;

public class Interpreter implements Sentence.Visitor<Boolean> {
  private final Map<String, Boolean> truthAssignment;
  private final Map<String, Boolean> intermediateResults;

  public Interpreter(Map<String, Boolean> truthAssignment, Map<String, Boolean> intermediateResults) {
    this.truthAssignment = truthAssignment;
    this.intermediateResults = intermediateResults;
  }
  
  // if the sentence is TRUE, FALSE, P, Q, S, then just get the value
  // Note that intermediate results is not used here because this are the initial values
  @Override
  public Boolean visitAtomicSentence(Sentence.AtomicSentence sentence) {
    return truthAssignment.get(sentence.value);
  }

  // if the sentence is NOT sentence, get the value of the right which is the sentence then invert
  @Override
  public Boolean visitUnarySentence(Sentence.Unary sentence) {
    Boolean right = sentence.right.accept(this);
    Boolean result = !right;
    String rightString = sentence.right.getString();
    rightString = rightString.replaceAll("\\(([^()]*)\\)", "$1");
    intermediateResults.put("NOT " + rightString, result);
    return result;
  }

  // if the sentence is binary, get the value of the left and right then do the operation according to type
  @Override
  public Boolean visitBinarySentence(Sentence.Binary sentence) {
    Boolean left = sentence.left.accept(this);
    Boolean right = sentence.right.accept(this);
    Boolean result;

    switch (sentence.operator.getType()) {
      case AND:
        result = left && right;
        break;
      case OR:
        result = left || right;
        break;
      case IMPLIES:
        result = !left || right;
        break;
      case EQUIVALENT:
        result = left == right;
        break;
      default: // default may not be achieved because of the scanner first
        result = false;
        break;
    }
    String rightString = sentence.right.getString();
    rightString = rightString.replaceAll("\\(([^()]*)\\)", "$1");
    String key = sentence.left.getString() + " " + sentence.operator.getLexeme() + " " + rightString;

    intermediateResults.put(key, result);

    return result;
  }

  // we just get the sentence passed in the grouping
  @Override
  public Boolean visitGroupingSentence(Sentence.Grouping sentence) {
    return sentence.expression.accept(this);
  }
}