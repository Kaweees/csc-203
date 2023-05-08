import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestCases {
  // Begin tests for BigNumArithmetic class
  /**
   * The actual standard output stream.
   */
  private PrintStream old;

  /**
   * The streams we're using to capture printed output.
   */
  private ByteArrayOutputStream baos;

  /**
   * Gets called before each test method. Need to do this so that we can capture the printed output from each method.
   */
  @BeforeEach
  public void setUp() {
    this.old = System.out; // Save a reference to the original stdout stream.
    this.baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
  }

  @Test
  public void testBadArgsArithmetic() {
    assertThrows(IllegalArgumentException.class, () -> BigNumArithmetic.main(new String[] { "foo", "bar" }));
  }

  @Test
  public void testMissingArgsArithmetic() {
    assertThrows(FileNotFoundException.class, () -> BigNumArithmetic.main(new String[] { "missingfile.txt" }));
  }

  @Test
  public void testSampleFile() {
    BigNumArithmetic.main(new String[] { "SampleInput.txt" });
    String output = this.baos.toString();
    assertEquals("""
      1 + 2 = 3
      2 ^ 4 = 16
      3 * 5 = 15
      2 ^ 40 = 1099511627776""", output);
  }

  /**
   * Gets called after each test method. Need to do this so that we are no longer capturing all printed output and
   * printed stuff appears like normal.
   */
  @AfterEach
  public void tearDown() {
    System.out.flush();
    System.setOut(this.old);
  }

  // Begin tests for Node class
  @Test
  public void testNodeConstructor() {
    Node node = new Node(123);
    assertEquals(123, node.getValue());
    assertNull(node.getNext());
    assertNull(node.getPrev());
  }

  @Test
  public void testNodeToString() {
    Node node = new Node(123);
    assertEquals("Node (value=123 -> null)", node.toString());
  }

  // Begin tests for LinkedList class
  @Test
  public void testLinkedListConstructor() {
    LinkedList list = new LinkedList();
    assertNull(list.getHead());
    assertNull(list.getTail());
    assertEquals(0, list.getSize());
  }

  @Test
  public void testAddRemove() {
    LinkedList list = new LinkedList();
    list.add(123);
    assertEquals(123, list.getHead().getValue());
    assertEquals(123, list.getTail().getValue());
    assertEquals(1, list.getSize());
    list.add(456);
    assertEquals(123, list.getHead().getValue());
    assertEquals(456, list.getTail().getValue());
    assertEquals(2, list.getSize());
    list.remove();
    assertEquals(123, list.getHead().getValue());
    assertEquals(123, list.getTail().getValue());
    assertEquals(1, list.getSize());
  }

  @Test
  public void testLinkedListToString() {
    LinkedList list = new LinkedList();
    list.add(123);
    list.add(456);
    assertEquals("123 -> 456 -> null", list.toString());
  }

  // Begin tests for BigNum class
  @Test
  public void testBigNumConstructor() {
    BigNum num = new BigNum("123");
    assertEquals("123", num.toString());
  }

  @Test
  public void testBadBigNumConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new BigNum("sass"));
  }

  @Test
  public void testConstructor() {
    Node cur = new BigNum("123").getList().getTail();
    for (int i = 0; i < 3; i++) {
      assertEquals(i + 1, cur.getValue());
      cur = cur.getPrev();
    }
    cur = new BigNum("456").getList().getTail();
    for (int i = 4; i < 7; i++) {
      assertEquals(i, cur.getValue());
      cur = cur.getPrev();
    }
  }

  @Test
  public void testToString() {
    BigNum num1 = new BigNum("123");
    BigNum num2 = new BigNum("456");
    assertEquals("123", num1.toString());
    assertEquals("456", num2.toString());
  }

  @Test
  public void testAdd() {
    BigNum num1 = new BigNum("3");
    BigNum num2 = new BigNum("6");
    BigNum result = BigNum.add(num1, num2);
    assertEquals("9", result.toString());
  }

  @Test
  public void testBigNumAdd() {
    BigNum num1 = new BigNum("123");
    BigNum num2 = new BigNum("456");
    BigNum result = BigNum.add(num1, num2);
    assertEquals("579", result.toString());
  }

  @Test
  public void testBigNumAdd2() {
    BigNum num1 = new BigNum("0");
    BigNum num2 = new BigNum("456");
    BigNum result = BigNum.add(num2, num1);
    assertEquals("456", result.toString());
  }

  @Test
  public void testBigNumAdd3() {
    BigNum num1 = new BigNum("123");
    BigNum num2 = new BigNum("456789");
    BigNum result = BigNum.add(num1, num2);
    assertEquals("456912", result.toString());
  }

  @Test
  public void testBigNumAdd4() {
    BigNum num1 = new BigNum("456789");
    BigNum num2 = new BigNum("123");
    BigNum result = BigNum.add(num1, num2);
    assertEquals("456912", result.toString());
  }

  @Test
  public void testBigNumMultiply() {
    BigNum num1 = new BigNum("123");
    BigNum num2 = new BigNum("456");
    BigNum result = BigNum.multiply(num1, num2);
    assertEquals("56088", result.toString());
  }

  @Test
  public void testBigNumMultiply2() {
    BigNum num1 = new BigNum("123");
    BigNum num2 = new BigNum("0");
    BigNum result = BigNum.multiply(num1, num2);
    assertEquals("0", result.toString());
    result = BigNum.multiply(num2, num1);
    assertEquals("0", result.toString());
  }

  @Test
  public void testBigNumMultiply3() {
    BigNum num1 = new BigNum("123");
    BigNum num2 = new BigNum("1");
    BigNum result = BigNum.multiply(num1, num2);
    assertEquals("123", result.toString());
    result = BigNum.multiply(num2, num1);
    assertEquals("123", result.toString());
  }

  @Test
  public void testBigNumExponentEven() {
    BigNum num1 = new BigNum("2");
    BigNum result = BigNum.exponent(num1, 4);
    assertEquals("16", result.toString());
  }

  @Test
  public void testBigNumExponentOdd() {
    BigNum num1 = new BigNum("2");
    BigNum result = BigNum.exponent(num1, 5);
    assertEquals("32", result.toString());
  }

  @Test
  public void testBigNumExponentZero() {
    BigNum num1 = new BigNum("2");
    BigNum result = BigNum.exponent(num1, 0);
    assertEquals("1", result.toString());
  }

  @Test
  public void testBigNumExponentOne() {
    BigNum num1 = new BigNum("2");
    BigNum result = BigNum.exponent(num1, 1);
    assertEquals("2", result.toString());
  }

  @Test
  public void testRemoveLeadingZeros() {
    BigNum num1 = new BigNum("00100");
    assertEquals("100", num1.toString());
    BigNum num2 = new BigNum("000000000");
    assertEquals("0", num2.toString());
  }
}