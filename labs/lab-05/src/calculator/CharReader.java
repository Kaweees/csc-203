package calculator;

import java.io.IOException;
import java.io.Reader;

public class CharReader {
  private final Reader input;

  private boolean gotEOF = false;
  private boolean putBack = false;
  private int currentChar = '\0';

  public static final int EOF = -1;

  public CharReader(final Reader reader) {
    this.input = reader;
  }

  public int read() {
    if (putBack) {
      putBack = false;
    } else {
      currentChar = protectedRead();
    }

    if (currentChar == EOF) {
      gotEOF = true;
    }

    return currentChar;
  }

  public int lookahead() {
    if (putBack) {
      return currentChar;
    } else {
      int c = read();
      putBack = true;
      return c;
    }
  }

  public boolean gotEOF() {
    return gotEOF;
  }

  private int protectedRead() {
    try {
      return input.read();
    } catch (IOException e) {
      System.err.println("Unexpected I/O error.");
      System.exit(1);
    }
    return -1;
  }
}
