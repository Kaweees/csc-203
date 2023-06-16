import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Zombie extends hasActions implements canMove {
  private final PathingStrategy strategy;

  public Zombie(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod,
      PathingStrategy pStrategy) {
    super(id, position, images, animationPeriod, actionPeriod);
    this.strategy = pStrategy;
  }

  public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
    Optional<Entity> zombieTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(Fairy.class)));
    System.out.println(zombieTarget);

    if (zombieTarget.isPresent() && moveTo(world, zombieTarget.get(), scheduler)) {
      Point position = zombieTarget.get().getPosition();
      world.removeEntity(scheduler, zombieTarget.get());
      Entity stump = new Stump("stump", position, imageStore.getImageList("stump"));
      System.out.println("stump created");
    } else {
      scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    }
  }

  public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
    if (adjacent(getPosition(), target.getPosition())) {
      return true;
    } else {
      Point nextPos = nextPosition(world, target.getPosition());

      if (!getPosition().equals(nextPos)) {
        world.moveEntity(scheduler, this, nextPos);
      }
      return false;
    }
  }

  public Point nextPosition(WorldModel world, Point destPos) {
    List<Point> path = strategy.computePath(getPosition(), destPos, p -> !world.isOccupied(p) && world.withinBounds(p),
        this::adjacent, PathingStrategy.CARDINAL_NEIGHBORS);
    if (path.isEmpty()) {
      return getPosition();
    } else {
      return path.get(0);
    }
  }

  public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
    scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
  }

  private PathingStrategy getStrategy() {
    return this.strategy;
  }
}