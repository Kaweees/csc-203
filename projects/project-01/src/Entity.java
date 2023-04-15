import java.util.List;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the different kinds of
 * entities that exist.
 */
public final class Entity {
  private EntityKind kind;
  private String id;
  private Point position;
  private List<PImage> images;
  private int imageIndex;
  private int resourceLimit;
  private int resourceCount;
  private double actionPeriod;
  private double animationPeriod;
  private int health;
  private int healthLimit;

  public Entity(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
      double actionPeriod, double animationPeriod, int health, int healthLimit) {
    this.kind = kind;
    this.id = id;
    this.position = position;
    this.images = images;
    this.imageIndex = 0;
    this.resourceLimit = resourceLimit;
    this.resourceCount = resourceCount;
    this.actionPeriod = actionPeriod;
    this.animationPeriod = animationPeriod;
    this.health = health;
    this.healthLimit = healthLimit;
  }

  public EntityKind getKind() {
    return kind;
  }

  public String getId() {
    return id;
  }

  public Point getPosition() {
    return position;
  }

  public List<PImage> getImages() {
    return images;
  }

  public int getImageIndex() {
    return imageIndex;
  }

  public void setImageIndex(int imageIndex) {
    this.imageIndex = imageIndex;
  }

  public int getResourceLimit() {
    return resourceLimit;
  }

  public int getResourceCount() {
    return resourceCount;
  }

  public double getActionPeriod() {
    return actionPeriod;
  }

  public double getAnimationPeriod() {
    return animationPeriod;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getHealthLimit() {
    return healthLimit;
  }

  /**
   * Helper method for testing. Preserve this functionality while refactoring.
   */
  public String log() {
    return this.id.isEmpty() ? null
        : String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
  }
}
