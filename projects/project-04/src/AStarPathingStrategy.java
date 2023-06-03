import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {

  public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
      BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
    List<Point> path = new LinkedList<>();
    /*
     * define closed list define open list while (true){ Filtered list
     * containing neighbors you can actually move to Check if any of the
     * neighbors are beside the target set the g, h, f values add them to open
     * list if not in open list add the selected node to close list return path
     */
    List<Point> closedList = new LinkedList<>();
    List<Point> openList = new LinkedList<>();
    openList.add(start);

  }
}
