import java.util.List;
/*
[Original BNF]
<Sentence>        -> <AtomicSentence> | <ComplexSentence>
<AtomicSentence>  -> "TRUE" | "FALSE" | "P" | "Q’ | "S"
<ComplexSentence> -> "(" <Sentence> ")" | <Sentence> <Connective> <Sentence> | "NOT" <Sentence>
<Connective>      ->  "NOT" | "AND" | "O" | "IMPLIES" | "EQUIVALENT"

[New Extended BNF with Precedence]
<Sentence>    -> <Equivalent>
<Equivalent>  -> <Implication> {"EQUIVALENT" <Implication>}   // Precedence: EQUIVALENT > IMPLIES
<Implication> -> <Or> {"IMPLIES" <Or>}                        // Precedence: IMPLIES > OR
<Or>          -> <And> {"OR" <And>}                           // Precedence: OR > AND
<And>         -> <Unary> {"AND" <Unary>}                      // Precedence: AND > NOT
<Unary>       -> "NOT" <Unary> | <Primary>                    // Precedence: NOT > Atomic Sentences
<Primary>     -> "TRUE" | "FALSE" | "P" | "Q" | "S" | "(" <Sentence> ")"
 */

public class Parser {
  private final List<Token> tokens;
  private int current = 0;

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  // <Sentence> start of recursive descent
  public Sentence Parse() {
    return Sentence();
  }

  // <Sentence> -> <Equivalent>
  private Sentence Sentence() {
    return Equivalent();
  }

  // <Equivalent> -> <Implication> {"EQUIVALENT" <Implication>}
  private Sentence Equivalent() {
    Sentence sentence = Implication();                             
    while (match(TokenType.EQUIVALENT)) {                   
      Token operator = previous();
      Sentence right = Implication();
      sentence = new Sentence.Binary(sentence, operator, right);
    }
    return sentence;                                             
  }

  // <Implication> -> <Or> {"IMPLIES" <Or>}
  private Sentence Implication() {
    Sentence expr = Or();
    while (match(TokenType.IMPLIES)) {
      Token operator = previous();
      Sentence right = Or();
      expr = new Sentence.Binary(expr, operator, right);
    }
    return expr;
  }

  // <Or> -> <And> {"OR" <And>}
  private Sentence Or() {
    Sentence sentence = And();
    while (match(TokenType.OR)) {
      Token operator = previous();
      Sentence right = And();
      sentence = new Sentence.Binary(sentence, operator, right);
    }
    return sentence;
  }

  // <And> -> <Unary> {"AND" <Unary>}
  private Sentence And() {
    Sentence sentence = Unary();
    while (match(TokenType.AND)) {
      Token operator = previous();
      Sentence right = Unary();
      sentence = new Sentence.Binary(sentence, operator, right);
    }
    return sentence;
  }

  // <Unary> -> "NOT" <Unary> | <Primary>
  private Sentence Unary() {
    if (match(TokenType.NOT)) {
      Token operator = previous();
      Sentence right = Unary();
      return new Sentence.Unary(operator, right);
    }
    return Primary();
  }

  // <Primary> -> "TRUE" | "FALSE" | "P" | "Q" | "S" | "(" <Sentence> ")"
  private Sentence Primary() {
    if (match(TokenType.TRUE, TokenType.FALSE, TokenType.P, TokenType.Q, TokenType.S)) {
      return new Sentence.Atomic(previous().getLexeme());
    }

    if (match(TokenType.LEFT_PAREN)) {
      Sentence sentence = Sentence();
      if (match(TokenType.RIGHT_PAREN)) {
        return new Sentence.Grouping(sentence);
      }
    }

    // Error handling for invalid sentences
    if (peek().getType() == TokenType.LEFT_PAREN) {
      Main.reportError(peek().getLine(), "Parser", "Sentence is invalid.\n\t Invalid Grouping.");
    } else {
      Main.reportError(peek().getLine(), "Parser", "Sentence is invalid.\n\t The Token " + peek().getLexeme() + " is not used in proper context.");
    }
    return null;
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }
    return false;
  }

  private boolean check(TokenType type) {
    return !isAtEnd() && peek().getType() == type;
  }

  private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  private boolean isAtEnd() {
    return peek().getType() == TokenType.EOF;
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }
}