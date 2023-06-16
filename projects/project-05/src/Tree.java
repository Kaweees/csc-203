import java.util.List;

import processing.core.PImage;

public class Tree extends hasActions implements canTransform {
  private int health;
  private final int healthLimit;

  Tree(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int health,
      int healthLimit) {
    super(id, position, images, animationPeriod, actionPeriod);
    this.health = health;
    this.healthLimit = healthLimit;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
    if (!transform(world, scheduler, imageStore)) {
      scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    }
  }

  public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    if (this.health <= 0) {
      Entity stump = new WorldModel().createStump(WorldModel.STUMP_KEY + "_" + getId(), getPosition(),
          imageStore.getImageList(WorldModel.STUMP_KEY));
      world.removeEntity(scheduler, this);
      world.addEntity(stump);
      return true;
    }
    return false;
  }

  public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
    scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
  }
}
