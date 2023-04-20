/**
 * A simple class representing a location in 2D space.
 */
public final class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point) other).getX() == this.x && ((Point) other).getY() == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + this.x;
        result = result * 31 + this.y;
        return result;
    }

    public int distanceSquared(Point p2) {
        int deltaX = this.x - p2.getX();
        int deltaY = this.y - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }

    public boolean adjacent(Point p2) {
        return (this.x == p2.getX() && Math.abs(this.y - p2.getY()) == 1) || (this.y == p2.getY() && Math.abs(this.x - p2.getX()) == 1);
    }

}
