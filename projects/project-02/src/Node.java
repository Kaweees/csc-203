/**
 * A class representing a node in a linked list. Each node contains a value and a reference to the next node in the
 * list.
 */
public class Node {

  private int value;
  private Node next;
  private Node prev = null;

  /**
   * Constructs a new node with the specified value and a null reference to the next node.
   *
   * @param value The value of the node. Cannot be null.
   */
  public Node(int value) {
    this.value = value;
    this.next = null;
  }

  @Override
  public String toString() {
    return "Node (value=" + this.value + " -> " + this.next + ")";
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Node getNext() {
    return this.next;
  }

  public void setNext(Node next) {
    this.next = next;
  }

  public Node getPrev() {
    return this.prev;
  }

  public void setPrev(Node prev) {
    this.prev = prev;
  }
}
