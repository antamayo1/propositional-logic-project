enum TokenType {
  // Left and Right Parentheses
  LEFT_PAREN, RIGHT_PAREN,

  // Literals: TRUE, FALSE
  // Identifiers: P, Q, R, S, T
  TRUE, FALSE, IDENTIFIERS,

  // Keywords for Operations
  NOT,                            // Unary Operations
  AND, OR, IMPLIES, EQUIVALENT,   // Binary Operations

  // Comment
  SLASH,

  // END OF LINE or FILE
  EOF
}