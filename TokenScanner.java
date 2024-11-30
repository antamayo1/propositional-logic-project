import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TokenScanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private static final Map<String, TokenType> keywords;
  private static int left_parentheses = 0;
  private static int right_parentheses = 0;

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
  }

  private int start = 0;
  private int current = 0;
  private int line = 1;

  // Constructor that takes the source expression
  TokenScanner(String source) {
    this.source = source;
  }

  // Loops through the source string
  List<Token> scanTokens() {
    // Group the whole expression as (P (NOT P)) to handle errors
    tokens.add(new Token(TokenType.LEFT_PAREN, "(", line));
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }
    tokens.add(new Token(TokenType.RIGHT_PAREN, ")", line));
    // adds and EOF token at the end to indicate that end of tokens
    tokens.add(new Token(TokenType.EOF, "", line));
    if (left_parentheses != right_parentheses) {
      Main.reportError(line, "Scanner", source + " has unbalanced parentheses.");
    }
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(': addToken(TokenType.LEFT_PAREN); left_parentheses++; break;
      case ')': addToken(TokenType.RIGHT_PAREN); right_parentheses++; break;
      case 'P':
        addToken(TokenType.LEFT_PAREN);
        addToken(TokenType.IDENTIFIERS);
        addToken(TokenType.RIGHT_PAREN);
        break;
      case 'Q':
        addToken(TokenType.LEFT_PAREN);
        addToken(TokenType.IDENTIFIERS);
        addToken(TokenType.RIGHT_PAREN);
        break;
      case 'S':
        addToken(TokenType.LEFT_PAREN);
        addToken(TokenType.IDENTIFIERS);
        addToken(TokenType.RIGHT_PAREN);
        break;
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
          checkKeywork();
        }
        else {
          Main.reportError(line, "Scanner", source + " has an invalid token.");
          break;
        }
        break;
    }
  }

  // May change this to accomodate all placeholders for statements
  private void checkKeywork() {
    while (isAlpha(peek())) advance();
    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null){
      Main.reportError(line, "Scanner", source + " has an invalid token.");
    }
    if(type == TokenType.TRUE){
      addToken(TokenType.LEFT_PAREN);
      addToken(type);
      addToken(TokenType.RIGHT_PAREN);
    } else if(type == TokenType.FALSE){
      addToken(TokenType.LEFT_PAREN);
      addToken(type);
      addToken(TokenType.RIGHT_PAREN);
    } else {
      addToken(type);
    }  
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
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, line));
  }

}