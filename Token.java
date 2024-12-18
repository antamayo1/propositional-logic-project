class Token {
  final private TokenType type;
  final private String lexeme;
  final private int line; 

  // Constructor
  Token(TokenType type, String lexeme, int line) {
    this.type = type;
    this.lexeme = lexeme;
    this.line = line;
  }

  // returns type
  public TokenType getType() {
    return type;
  }

  // returns lexeme
  public String getLexeme() {
    return lexeme;
  }

  // returns line number <- though always 1
  public int getLine(){
    return line;
  }

  // returns string form of token
  public String toString() {
    return type + " " + lexeme;
  }
}