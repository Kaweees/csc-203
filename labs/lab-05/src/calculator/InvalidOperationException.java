package calculator;

public class InvalidOperationException extends Exception {
  public InvalidOperationException(final String msg) {
    super(msg);
  }
}
