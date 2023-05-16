package calculator;

public class NegationExpression implements Expression {
  private final Expression operand;

  public NegationExpression(final Expression operand) {
    this.operand = operand;
  }

  public String toString() {
    return "-" + operand;
  }

  public double evaluate(final Bindings bindings) {
    return -operand.evaluate(bindings);
  }

}
