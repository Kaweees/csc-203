import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {

  public static class Node implements Comparable<Node> {
    private Point point;
    private Node prev;
    private int g;
    private int h;
    private int f;

    public Node(Point point, Node prev, int g, int h) {
      this.point = point;
      this.prev = prev;
      this.g = g;
      this.h = h;
      this.f = g + h;
    }

    public Point getPoint() {
      return point;
    }

    public Node getPrev() {
      return prev;
    }

    public int getG() {
      return g;
    }

    public int getH() {
      return h;
    }

    public int getF() {
      return f;
    }

    public int compareTo(Node other) {
      return Integer.compare(this.f, other.f);
    }
  }

  public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
      BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
    List<Point> path = new LinkedList<Point>();

    HashMap<Point, Node> closedList = new HashMap<Point, Node>();
    PriorityQueue<Node> openList = new PriorityQueue<Node>();

    Node startNode = new Node(start, null, 0, Math.abs(start.x - end.x) + Math.abs(start.y - end.y));
    openList.add(startNode);

    while (!openList.isEmpty()) {
      Node cur = openList.poll();
      if (withinReach.test(cur.getPoint(), end)) {
        while (cur.getPrev() != null) {
          path.add(0, cur.getPoint());
          cur = cur.getPrev();
        }
        break;
      } else {
        List<Point> neighbors = potentialNeighbors.apply(cur.getPoint()).collect(Collectors.toList());
        for (Point p : neighbors) {
          if (canPassThrough.test(p) && !closedList.containsKey(p)) {
            Node newNode = new Node(p, cur, cur.getG() + 1, Math.abs(p.x - end.x) + Math.abs(p.y - end.y));
            openList.add(newNode);
          }
        }
        closedList.put(cur.getPoint(), cur);
      }
    }

    return path;
  }
}
