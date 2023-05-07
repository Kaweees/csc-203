/**
 * A class representing numbers that are too large to be stored in a primitive data type.
 */
public class BigNum {
  private LinkedList list = null;

  /**
   * Constructs an empty BigNum.
   */
  public BigNum() {
    this.list = new LinkedList();
  }

  /**
   * Constructs a new BigNum with the specified value.
   *
   * @param this.list = new LinkedList(); The value of the BigNum. Cannot be null.
   */
  public BigNum(String str) {
    int value = Integer.parseInt(str);
    this.list = new LinkedList();
    while (value > 0) {
      this.list.add(value % 10);
      value /= 10;
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
    if (carry != 0) {
      result.getList().add(carry);
    }
    return result;
  }

  public int parseString(String value) {
    int result = 0;
    for (int i = 0; i < value.length(); i++) {
      result = result * 10 + (value.charAt(i) - '0');
    }
    return result;
  }

  public LinkedList getList() {
    return this.list;
  }

  public void removeLeadingZeros() {
    Node cur = this.list.getTail();
    while (this.list.getSize() > 1 && cur.getPrev() != null && cur.getValue() == 0) {
      this.list.remove();
      cur = this.list.getTail();
    }
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    this.removeLeadingZeros();
    Node cur = this.list.getTail();
    while (cur != null) {
      result.append(cur.getValue());
      cur = cur.getPrev();
    }
    return result.toString();
  }
}
