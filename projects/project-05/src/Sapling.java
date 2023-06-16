import java.util.List;

import processing.core.PImage;

public class Sapling extends hasActions implements canTransform {
  private static final double TREE_ANIMATION_MAX = 0.600;
  private static final double TREE_ANIMATION_MIN = 0.050;
  private static final double TREE_ACTION_MAX = 1.400;
  private static final double TREE_ACTION_MIN = 1.000;
  private static final int TREE_HEALTH_MAX = 3;
  private static final int TREE_HEALTH_MIN = 1;
  private int health;
  private final int healthLimit;

  Sapling(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int health,
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
    health++;
    if (!transform(world, scheduler, imageStore)) {
      scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    }
  }

  public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
    scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
  }

  public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    if (this.health <= 0) {
      Entity stump = new WorldModel().createStump(WorldModel.STUMP_KEY + "_" + getId(), getPosition(),
          imageStore.getImageList(WorldModel.STUMP_KEY));

      world.removeEntity(scheduler, this);

      world.addEntity(stump);

      return true;
    } else if (this.health >= this.healthLimit) {
      Entity tree = new WorldModel().createTree(WorldModel.TREE_KEY + "_" + getId(), getPosition(),
          getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN), getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
          getIntFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN), imageStore.getImageList(WorldModel.TREE_KEY));

      world.removeEntity(scheduler, this);

      world.addEntity(tree);
      ((hasAnimations) tree).scheduleActions(scheduler, world, imageStore);

      return true;
    }

    return false;
  }
}
