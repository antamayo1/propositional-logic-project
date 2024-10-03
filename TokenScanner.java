import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TokenScanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("AND",         TokenType.AND);
    keywords.put("NOT",         TokenType.NOT);
    keywords.put("OR",          TokenType.OR);
    keywords.put("IMPLIES",     TokenType.IMPLIES);
    keywords.put("EQUIVALENT",  TokenType.EQUIVALENT);
  }

  private int start = 0;
  private int current = 0;
  private int line = 1;

  TokenScanner(String source) {
    this.source = source;
  }

  List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(TokenType.EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(': addToken(TokenType.LEFT_PAREN); break;
      case ')': addToken(TokenType.RIGHT_PAREN); break;
      case 'P': addToken(TokenType.IDENTIFIER); break;
      case 'Q': addToken(TokenType.IDENTIFIER); break;
      case 'S': addToken(TokenType.IDENTIFIER); break;
      case '/':
        if (match('/')) {
          while (peek() != '\n' && !isAtEnd()) advance();
        } else {
          addToken(TokenType.SLASH);
        }
        break;
      case ' ':
      case '\r':
      case '\t': break;
      case '\n':
        line++;
        break;
      default:
        if (isAlpha(c)) {
          identifier();
        }
        else {
          Main.error(line, "Unexpected character.");
          break;
        }
    }
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c);
  }

  private void identifier() {
    while (isAlphaNumeric(peek())) advance();

    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null) type = TokenType.IDENTIFIER;
    addToken(type);
  }

  private boolean match(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;

    current++;
    return true;
  }

  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }

  // private char peekNext() {
  //   if (current + 1 >= source.length()) return '\0';
  //   return source.charAt(current + 1);
  // } 

  private boolean isAtEnd() {
    return current >= source.length();
  }

  private char advance() {
    return source.charAt(current++);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }
}