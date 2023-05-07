/**
 * A class representing a doubly linked implementation of a linked list.
 *
 * @see Node
 */
public class LinkedList {
  private Node head;
  private Node tail;
  private int size;

  /**
   * Constructs an empty linked list.
   */
  public LinkedList() {
    this.head = null; // beginning of a linked list
    this.tail = null; // end of a linked list
    this.size = 0;
  }

  public void add(int value) {
    if (this.head == null) {
      this.head = new Node(value);
      this.tail = this.head;
    } else {
      this.tail.setNext(new Node(value));
      this.tail.getNext().setPrev(this.tail);
      this.tail = this.tail.getNext();
    }
    this.size++;
  }

  public Node getHead() {
    return this.head;
  }

  public Node getTail() {
    return this.tail;
  }

  public int getSize() {
    return this.size;
  }

  public void remove() {
    if (this.size != 0) {
      this.tail = this.tail.getPrev();
      this.tail.setNext(null);
      this.size--;
    }
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    Node current = this.head;
    while (current != null) {
      result.append(current.getValue());
      if (current.getNext() != null) {
        result.append(" -> ");
      } else {
        result.append(" -> null");
      }
      current = current.getNext();
    }
    return result.toString();
  }
}