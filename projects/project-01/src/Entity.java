import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the different kinds of
 * entities that exist.
 */
public final class Entity {
  private final EntityKind kind;
  private final String id;
  private Point position;
  private final List<PImage> images;
  private int imageIndex;
  private final int resourceLimit;
  private int resourceCount;
  private final double actionPeriod;
  private final double animationPeriod;
  private int health;
  private final int healthLimit;

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

  /**
   * Helper method for testing. Preserve this functionality while refactoring.
   */
  public String log() {
    return this.id.isEmpty() ? null
        : String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
  }

  public EntityKind getKind() {
    return this.kind;
  }

  public String getId() {
    return this.id;
  }

  public Point getPosition() {
    return position;
  }

  public void setPosition(Point position) {
    this.position = position;
  }

  public List<PImage> getImages() {
    return images;
  }

  public int getImageIndex() {
    return imageIndex;
  }

  public int getResourceCount() {
    return resourceCount;
  }

  public int getResourceLimit() {
    return resourceLimit;
  }

  public double getActionPeriod() {
    return actionPeriod;
  }

  public PImage getCurrentImage() {
    return this.images.get(this.imageIndex % this.images.size());
  }

  public double getAnimationPeriod() {
    return switch (this.kind) {
    case DUDE_FULL, DUDE_NOT_FULL, OBSTACLE, FAIRY, SAPLING, TREE -> this.animationPeriod;
    default -> throw new UnsupportedOperationException(
        String.format("getAnimationPeriod not supported for %s", this.kind));
    };
  }

  public int getHealth() {
    return this.health;
  }

  public int getHealthLimit() {
    return this.healthLimit;
  }

  public void nextImage() {
    this.imageIndex = this.imageIndex + 1;
  }

  public void executeSaplingActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
    this.health++;
    if (!transformPlant(world, scheduler, imageStore)) {
      scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.actionPeriod);
    }
  }

  public void executeTreeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    if (!transformPlant(world, scheduler, imageStore)) {

      scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.actionPeriod);
    }
  }

  public void executeFairyActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
    Optional<Entity> fairyTarget = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.STUMP)));

    if (fairyTarget.isPresent()) {
      Point tgtPos = fairyTarget.get().position;

      if (this.moveToFairy(world, fairyTarget.get(), scheduler)) {

        Entity sapling = world.createSapling(WorldModel.SAPLING_KEY + "_" + fairyTarget.get().id, tgtPos,
            imageStore.getImageList(WorldModel.SAPLING_KEY), 0);

        world.addEntity(sapling);
        sapling.scheduleActions(scheduler, world, imageStore);
      }
    }

    scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.actionPeriod);
  }

  public void executeDudeNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
    Optional<Entity> target = world.findNearest(this.position,
        new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

    if (target.isEmpty() || !moveToNotFull(world, target.get(), scheduler)
        || !transformNotFull(world, scheduler, imageStore)) {
      scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.actionPeriod);
    }
  }

  public void executeDudeFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
    Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.HOUSE)));

    if (fullTarget.isPresent() && moveToFull(world, fullTarget.get(), scheduler)) {
      transformFull(world, scheduler, imageStore);
    } else {
      scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.actionPeriod);
    }
  }

  public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
    switch (this.kind) {
    case DUDE_FULL, DUDE_NOT_FULL, FAIRY, SAPLING, TREE -> {
      scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.actionPeriod);
      scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
    }
    case OBSTACLE -> scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
    default -> {
    }
    }
  }

  public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    if (this.resourceCount >= this.resourceLimit) {
      Entity dude = world.createDudeFull(this.id, this.position, this.actionPeriod, this.animationPeriod,
          this.resourceLimit, this.images);

      world.removeEntity(scheduler, this);
      scheduler.unscheduleAllEvents(this);

      world.addEntity(dude);
      scheduleActions(scheduler, world, imageStore);

      return true;
    }

    return false;
  }

  public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    Entity dude = world.createDudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod,
        this.resourceLimit, this.images);

    world.removeEntity(scheduler, this);

    world.addEntity(dude);
    scheduleActions(scheduler, world, imageStore);
  }

  public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    if (this.kind == EntityKind.TREE) {
      return transformTree(world, scheduler, imageStore);
    } else if (this.kind == EntityKind.SAPLING) {
      return transformSapling(world, scheduler, imageStore);
    } else {
      throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
    }
  }

  public boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    if (this.health <= 0) {
      Entity stump = world.createStump(WorldModel.STUMP_KEY + "_" + this.id, this.position,
          imageStore.getImageList(WorldModel.STUMP_KEY));

      world.removeEntity(scheduler, this);

      world.addEntity(stump);

      return true;
    }

    return false;
  }

  public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    if (this.health <= 0) {
      Entity stump = world.createStump(WorldModel.STUMP_KEY + "_" + this.id, this.position,
          imageStore.getImageList(WorldModel.STUMP_KEY));

      world.removeEntity(scheduler, this);

      world.addEntity(stump);

      return true;
    } else if (this.health >= this.healthLimit) {
      Entity tree = world.createTree(WorldModel.TREE_KEY + "_" + this.id, this.position,
          getNumFromRange(WorldModel.TREE_ACTION_MAX, WorldModel.TREE_ACTION_MIN),
          getNumFromRange(WorldModel.TREE_ANIMATION_MAX, WorldModel.TREE_ANIMATION_MIN),
          getIntFromRange(WorldModel.TREE_HEALTH_MAX, WorldModel.TREE_HEALTH_MIN),
          imageStore.getImageList(WorldModel.TREE_KEY));

      world.removeEntity(scheduler, this);

      world.addEntity(tree);
      scheduleActions(scheduler, world, imageStore);

      return true;
    }

    return false;
  }

  public boolean moveToFairy(WorldModel world, Entity target, EventScheduler scheduler) {
    if (this.position.adjacent(target.position)) {
      world.removeEntity(scheduler, target);
      return true;
    } else {
      Point nextPos = nextPositionFairy(world, target.position);

      if (!this.position.equals(nextPos)) {
        world.moveEntity(scheduler, this, nextPos);
      }
      return false;
    }
  }

  public boolean moveToNotFull(WorldModel world, Entity target, EventScheduler scheduler) {
    if (this.position.adjacent(target.position)) {
      this.resourceCount += 1;
      target.health--;
      return true;
    } else {
      Point nextPos = nextPositionDude(world, target.position);

      if (!this.position.equals(nextPos)) {
        world.moveEntity(scheduler, this, nextPos);
      }
      return false;
    }
  }

  public boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler) {
    if (this.position.adjacent(target.position)) {
      return true;
    } else {
      Point nextPos = nextPositionDude(world, target.position);

      if (!this.position.equals(nextPos)) {
        world.moveEntity(scheduler, this, nextPos);
      }
      return false;
    }
  }

  public Point nextPositionFairy(WorldModel world, Point destPos) {
    int horiz = Integer.signum(destPos.getX() - this.position.getX());
    Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

    if (horiz == 0 || world.isOccupied(newPos)) {
      int vert = Integer.signum(destPos.getY() - this.position.getY());
      newPos = new Point(this.position.getX(), this.position.getY() + vert);

      if (vert == 0 || world.isOccupied(newPos)) {
        newPos = this.position;
      }
    }

    return newPos;
  }

  public Point nextPositionDude(WorldModel world, Point destPos) {
    int horiz = Integer.signum(destPos.getX() - this.position.getX());
    Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

    if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
      int vert = Integer.signum(destPos.getY() - this.position.getY());
      newPos = new Point(this.position.getX(), this.position.getY() + vert);

      if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
        newPos = this.position;
      }
    }
    return newPos;
  }

  public Action createAnimationAction(int repeatCount) {
    return new Action(ActionKind.ANIMATION, this, null, null, repeatCount);
  }

  public Action createActivityAction(WorldModel world, ImageStore imageStore) {
    return new Action(ActionKind.ACTIVITY, this, world, imageStore, 0);
  }

  public double getNumFromRange(double max, double min) {
    Random rand = new Random();
    return min + rand.nextDouble() * (max - min);
  }

  public int getIntFromRange(int max, int min) {
    Random rand = new Random();
    return min + rand.nextInt(max - min);
  }

}
