public abstract class Sentence {
  public interface Visitor<R> {
    R visitAtomicSentence(AtomicSentence sentence);
    R visitUnarySentence(Unary sentence);
    R visitBinarySentence(Binary sentence);
    R visitGroupingSentence(Grouping sentence);
  }

  public abstract String getString();

  public abstract <R> R accept(Visitor<R> visitor);

  public static class AtomicSentence extends Sentence {
    public final String value;

    public AtomicSentence(String value) {
      this.value = value;
    }

    public String getString() {
      return value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitAtomicSentence(this);
    }
  }

  public static class Unary extends Sentence {
    public final Token operator;
    public final Sentence right;

    public Unary(Token operator, Sentence right) {
      this.operator = operator;
      this.right = right;
    }

    public String getString() {
      return operator.getLexeme() + " " + right.getString();
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnarySentence(this);
    }
  }
  
  public static class Binary extends Sentence {
    public final Sentence left;
    public final Token operator;
    public final Sentence right;

    public Binary(Sentence left, Token operator, Sentence right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    public String getString() {
      return left.getString() + " " + operator.getLexeme() + " " + right.getString();
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinarySentence(this);
    }
  }

  public static class Grouping extends Sentence {
    public final Sentence expression;

    public Grouping(Sentence expression) {
      this.expression = expression;
    }

    public String getString() {
      return "(" + expression.getString() + ")";
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitGroupingSentence(this);
    }
  }
}