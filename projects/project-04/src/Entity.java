import java.util.List;
import java.util.Random;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the different kinds of
 * entities that exist.
 */
public abstract class Entity {
  private final String id;
  private Point position;
  private final List<PImage> images;
  private int imageIndex;

  public String getId() {
    return id;
  }

  public Point getPosition() {
    return position;
  }

  public void setPosition(Point pos) {
    position = pos;
  }

  public List<PImage> getImages() {
    return images;
  }

  public Entity(String id, Point position, List<PImage> images) {
    this.id = id;
    this.position = position;
    this.images = images;
    this.imageIndex = 0;
  }

  public boolean adjacent(Point p1, Point p2) {
    return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
  }

  public int getIntFromRange(int max, int min) {
    Random rand = new Random();
    return min + rand.nextInt(max - min);
  }

  public double getNumFromRange(double max, double min) {
    Random rand = new Random();
    return min + rand.nextDouble() * (max - min);
  }

  public PImage getCurrentImage() {
    return images.get(imageIndex % images.size());
  }

  public void nextImage() {
    imageIndex = imageIndex + 1;
  }

  /**
   * Helper method for testing. Preserve this functionality while refactoring.
   */
  public String log() {
    return this.id.isEmpty() ? null
        : String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
  }
}
