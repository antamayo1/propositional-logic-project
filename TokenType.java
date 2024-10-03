enum TokenType {
  // Left and Right Parentheses
  LEFT_PAREN, RIGHT_PAREN,

  // Literals: P, Q, S
  IDENTIFIER,

  // Keywords
  AND, NOT, OR, IMPLIES, EQUIVALENT,

  // Comment
  SLASH,

  EOF
}