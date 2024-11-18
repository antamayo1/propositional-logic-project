import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private static final Map<String, TokenType> keywords;

  // Define a hashmap that saves the compiler's keywords
  static {
    keywords = new HashMap<>();
    keywords.put("AND",         TokenType.AND);
    keywords.put("NOT",         TokenType.NOT);
    keywords.put("OR",          TokenType.OR);
    keywords.put("IMPLIES",     TokenType.IMPLIES);
    keywords.put("EQUIVALENT",  TokenType.EQUIVALENT);
    keywords.put("TRUE",        TokenType.TRUE);
    keywords.put("FALSE",       TokenType.FALSE);
    keywords.put("P",           TokenType.P);
    keywords.put("Q",           TokenType.Q);
    keywords.put("R",           TokenType.R);
    keywords.put("S",           TokenType.S);
    keywords.put("T",           TokenType.T);
  }


  private int start = 0;
  private int current = 0;
  private int line = 1;

  // Constructor that takes the source expression
  Scanner(String source) {
    this.source = source;
  }

  // Loops through the source string
  List<Token> scanTokens() {
    // Funny Solution for cases like P (NOT P)
    // Group the whole expression as (P (NOT P)) to handle errors
    tokens.add(new Token(TokenType.LEFT_PAREN, "(", null, line));
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }
    tokens.add(new Token(TokenType.RIGHT_PAREN, ")", null, line));
    // adds and EOF token at the end to indicate that end of tokens
    tokens.add(new Token(TokenType.EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(': addToken(TokenType.LEFT_PAREN); break;
      case ')': addToken(TokenType.RIGHT_PAREN); break;
      case 'P': addToken(TokenType.P); break;
      case 'Q': addToken(TokenType.Q); break;
      case 'S': addToken(TokenType.S); break;
      case '/':
        if (match('/')) {
          while (peek() != '\n' && !isAtEnd()) advance();
        } else {
          Main.reportError(line, "Scanner", source + " has an invalid token.");
          break;
        }
        break;
      case ' ':
      case '\t': break;
      default:
        if (isAlpha(c)) {
          identifier();
        }
        else {
          Main.reportError(line, "Scanner", source + " has an invalid token.");
          break;
        }
        break;
    }
  }

  // May change this to accomodate all placeholders for statements
  private void identifier() {
    while (isAlpha(peek())) advance();
    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null){
      Main.reportError(line, "Scanner", source + " has an invalid token.");
    }
    addToken(type);
  }

  private boolean isAlpha(char c) {
    return (c >= 'A' && c <= 'Z');
  }

  // matches the next character for a token
  private boolean match(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;

    current++;
    return true;
  }

  // checks for the current character
  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }


  // returns if current is at the end
  private boolean isAtEnd() {
    return current >= source.length();
  }

  // moves the current while returns the character
  private char advance() {
    return source.charAt(current++);
  }

  // adds token
  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }
}