import java.util.List;

public class Parser {
  private final List<Token> tokens;
  private int current = 0;

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  public Sentence Parse() {
    Sentence sentence = Sentence();
    if (sentence == null) {
      Main.reportError(1, "Parser", "Sentence not valid.");
    }
    return sentence;
  }

  // <Sentence> -> <AtomicSentence> | <ComplexSentence>
  private Sentence Sentence() {
    Sentence sentence = ComplexSentence();
    return sentence != null ? sentence : AtomicSentence();
  }

  private Sentence ComplexSentence() {
    if (match(TokenType.LEFT_PAREN)) {
      // "(" <Sentence> ")"
      Sentence sentence = Sentence();
      if (sentence == null) {
        if(peek().getType() == TokenType.RIGHT_PAREN) {
          Main.reportError(1, "Parser", "Sentence expected inside parenthesis.");
        } else {
          Main.reportError(1, "Parser", "Incomplete Binary Operation: " + peek().getLexeme());
        }
      }
      if (!match(TokenType.RIGHT_PAREN)) {
        Main.reportError(1, "Parser", "Invalid Sentence.");
      }
      if (Connective()) {
        Token operator = previous();
        if (operator.getType() == TokenType.NOT) {
          Main.reportError(1, "Parser", "NOT cannot be used a connective.");
        }
        Sentence right = Sentence();
        if (right == null) {
          Main.reportError(1, "Parser", "Incomplete Binary Operation: " + previous().getLexeme());
        }
        return new Sentence.Binary(sentence, operator, right);
      }
      return new Sentence.Grouping(sentence);
    }

    if (match(TokenType.NOT)) {
      // "NOT" <Sentence>
      Token operator = previous();
      Sentence right = Sentence();
      if (right == null) {
        Main.reportError(1, "Parser", "Expected sentence after NOT.");
      }
      return new Sentence.Unary(operator, right);
    }

    return null;
}

  // <AtomicSentence> -> "TRUE" | "FALSE" | "P" | "Q" | "S"
  private Sentence AtomicSentence() {
    if (match(TokenType.TRUE, TokenType.FALSE, TokenType.IDENTIFIERS)) {
      Token token = previous();
      return new Sentence.AtomicSentence(token.getLexeme());
    }
    return null;
  }

  // <Connective> -> "AND" | "OR" | "IMPLIES" | "EQUIVALENT"
  private boolean Connective() {
    return match(TokenType.AND) || match(TokenType.OR) || match(TokenType.IMPLIES) || match(TokenType.EQUIVALENT) || match(TokenType.NOT);
  }

  // Check if the current token matches any of the given types
  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }
    return false;
  }

  // Check if the current token matches the given type
  private boolean check(TokenType type) {
    return !isAtEnd() && peek().getType() == type;
  }

  // Consume and return the current token
  private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  // Return true if at the end of the token list
  private boolean isAtEnd() {
    return peek().getType() == TokenType.EOF;
  }

  // Get the current token
  private Token peek() {
    return tokens.get(current);
  }

  // Get the previous token
  private Token previous() {
    return tokens.get(current - 1);
  }

}
