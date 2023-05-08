/**
 * A class representing numbers that are too large to be stored in a primitive data type.
 */
public class BigNum {
  private final LinkedList list;

  /**
   * Constructs an empty BigNum.
   */
  public BigNum() {
    this.list = new LinkedList();
  }

  /**
   * Constructs a new BigNum with the specified value.
   *
   * @param value The value of the BigNum. Cannot be null.
   */
  public BigNum(String value) {
    this.list = new LinkedList();
    int result = 0;
    for (int i = value.length() - 1; i >= 0; i--) {
      if (!Character.isDigit(value.charAt(i))) {
        throw new IllegalArgumentException("Value must be a number.");
      }
      result += (value.charAt(i) - '0') * Math.pow(10, value.length() - 1 - i);
    }

    while (result > 0) {
      this.list.add(result % 10);
      result /= 10;
    }

    if (this.list.getSize() == 0) {
      this.list.add(0);
    }
  }

  /**
   * Adds two BigNums together.
   *
   * @param num1 a BigNum to add. Cannot be null. Cannot be negative. Cannot be zero. Cannot be empty.
   * @param num2 an additional BigNum to add. Cannot be null. Cannot be negative. Cannot be zero. Cannot be empty.
   * @return The sum of the two BigNums.
   */
  public static BigNum add(BigNum num1, BigNum num2) {
    BigNum result = new BigNum();
    Node cur1 = num1.getList().getHead();
    Node cur2 = num2.getList().getHead();
    int carry = 0;
    while (cur1 != null && cur2 != null) {
      result.getList().add((cur1.getValue() + cur2.getValue() + carry) % 10);
      carry = (cur1.getValue() + cur2.getValue() + carry) / 10;
      cur1 = cur1.getNext();
      cur2 = cur2.getNext();
    }
    Node extra = cur1 == null ? cur2 : cur1;
    while (extra != null) {
      result.getList().add((extra.getValue() + carry) % 10);
      carry = (extra.getValue() + carry) / 10;
      extra = extra.getNext();
    }
    return result;
  }

  /**
   * Multiples two BigNums together.
   *
   * @param operand1 a BigNum to multiply. Cannot be null. Cannot be negative. Cannot be zero. Cannot be empty.
   * @param operand2 another BigNum to multiply. Cannot be null. Cannot be negative. Cannot be zero. Cannot be empty.
   * @return The product of the two BigNums.
   */
  public static BigNum multiply(BigNum operand1, BigNum operand2) {
    BigNum result = new BigNum();
    int shiftOp2 = 0;
    result.getList().add(0);
    Node cur1;
    Node cur2 = operand2.getList().getHead();
    Node cur3;
    int carry = 0;
    while (cur2 != null) {
      int sum;
      carry = 0;
      cur3 = result.getList().getHead();
      for (int i = 0; i < shiftOp2; i++) {
        cur3 = cur3.getNext();
      }
      cur1 = operand1.getList().getHead();
      while (cur1 != null) {
        sum = (cur3.getValue() + (cur1.getValue() * cur2.getValue()) + carry) % 10;
        carry = (cur3.getValue() + (cur1.getValue() * cur2.getValue()) + carry) / 10;
        cur3.setValue(sum);
        cur1 = cur1.getNext();
        if (cur3.getNext() == null) {
          result.getList().add(0);
        }
        cur3 = cur3.getNext();
      }
      cur2 = cur2.getNext();
      shiftOp2++;
    }
    if (carry != 0) {
      result.getList().getTail().setValue(result.getList().getTail().getValue() + carry);
    }
    return result;
  }

  public static BigNum exponent(BigNum operand1, int num) {
    if (num == 0) {
      return new BigNum("1");
    } else if (num == 1) {
      return operand1;
    } else {
      BigNum square = BigNum.multiply(operand1, operand1);
      BigNum result = square;
      boolean isOdd = num % 2 != 0;
      num = (isOdd ? ((num - 1) / 2) : (num / 2));
      for (int i = 1; i < num; i++) {
        result = BigNum.multiply(result, square);
      }
      if (isOdd) {
        result = BigNum.multiply(result, operand1);
      }
      return result;
    }
  }

  public LinkedList getList() {
    return this.list;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    Node cur = this.list.getTail();
    while (cur.getNext() == null && cur.getPrev() != null && cur.getValue() == 0) {
      this.list.remove();
      cur = this.list.getTail();
    }
    cur = this.list.getTail();
    while (cur != null) {
      result.append(cur.getValue());
      cur = cur.getPrev();
    }
    return result.toString();
  }
}
