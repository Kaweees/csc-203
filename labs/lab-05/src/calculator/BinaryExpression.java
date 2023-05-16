package calculator;

public abstract class BinaryExpression implements Expression {
  private final Expression lft;
  private final Expression rht;
  private final char op;

  public BinaryExpression(final Expression lft, final Expression rht, char op) {
    this.lft = lft;
    this.rht = rht;
    this.op = op;
  }

  public String toString() {
    return String.format("( %s %s %s )", lft, op, rht);
  }

  public double evaluate(final Bindings bindings) {
    return _applyOperator(lft.evaluate(bindings), rht.evaluate(bindings));
  }

  abstract double _applyOperator(double lft, double rht);
}
