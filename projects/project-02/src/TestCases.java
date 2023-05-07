import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestCases {
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
  public void testRemoveLeadingZeros() {
    BigNum num1 = new BigNum("00100");
    assertEquals("100", num1.toString());
    BigNum num2 = new BigNum("000000000");
    assertEquals("0", num2.toString());
  }
}