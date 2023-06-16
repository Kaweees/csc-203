import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class AStarPathingVirtual implements PathingStrategy {

  public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
      BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
    List<Point> path = new LinkedList<>();
    List<Point> closedList = new LinkedList<>();
    List<Point> openList = new LinkedList<>();
    openList.add(start);

    while (!openList.isEmpty()) {
      Point current = openList.get(0);
      // set current to point with lowest f value
      for (Point p : openList) {
        if (calcF(p, start, end) < calcF(current, start, end)) {
          current = p;
        }
      }
      openList.remove(current);
      closedList.add(current);
      System.out.println(current);
      if (withinReach.test(current, end)) {
        path.add(current);
        return path; // Found the goal, return the complete path
      }
      List<Point> neighbors = potentialNeighbors.apply(current).filter(canPassThrough)
          .filter(p -> !closedList.contains(p)).toList();
      Point bestNeighbor = null;
      for (Point p : neighbors) {
        if (bestNeighbor == null || calcF(p, start, end) < calcF(bestNeighbor, start, end)) {
          bestNeighbor = p;
        }
        // if child is not in open list, add to open list
        if (!openList.contains(p)) {
          openList.add(p);
        }
      }
      if (bestNeighbor != null) {
        // Update the path with the best neighbor found
        path.add(bestNeighbor);
      }
    }
    return path; // Return the complete path
  }

  private double calcF(Point cur, Point start, Point end) {
    return calcG(cur, start) + calcH(cur, end);
  }

  private double calcG(Point cur, Point start) {
    return Math.sqrt(Math.pow(cur.x - start.x, 2) + Math.pow(cur.y - start.y, 2));
  }

  private double calcH(Point cur, Point end) {
    return Math.sqrt(Math.pow(cur.x - end.x, 2) + Math.pow(cur.y - end.y, 2));
  }
}