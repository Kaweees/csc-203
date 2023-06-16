import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Dude extends hasActions implements canMove, canTransform {
  private final int resourceLimit;
  private int resourceCount;
  private final boolean dudeFull;
  private final PathingStrategy strategy;

  public Dude(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
      double animationPeriod, double actionPeriod, boolean dudeFull, PathingStrategy pStrategy) {
    super(id, position, images, animationPeriod, actionPeriod);
    this.dudeFull = dudeFull;
    this.resourceLimit = resourceLimit;
    this.resourceCount = resourceCount;
    this.strategy = pStrategy;
  }

  public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
    boolean foundGrave = false;
    Point upN = new Point(this.getPosition().getX(), this.getPosition().getY() - 1);
    Point downN = new Point(this.getPosition().getX(), this.getPosition().getY() + 1);
    Point leftN = new Point(this.getPosition().getX() - 1, this.getPosition().getY());
    Point rightN = new Point(this.getPosition().getX() + 1, this.getPosition().getY());

    ArrayList<Point> neighbors = new ArrayList<>();
    neighbors.add(rightN);
    neighbors.add(downN);
    neighbors.add(leftN);
    neighbors.add(upN);

    for (Point node : neighbors) {
      // check node is a valid grid cell, hasn't been searched, and isn't an
      // obstacle

      if (world.withinBounds(node)) {
        Optional<Entity> entityOptional = world.getOccupant(node);
        if (entityOptional.isPresent() && entityOptional.get() instanceof Grave) {
          foundGrave = true;
          break;
        }
      }
    }

    if (foundGrave) {
      Point position = this.getPosition();
      world.removeEntity(scheduler, this);
      Zombie zombie = new Zombie("zombie", position, imageStore.getImageList("zombie"), 1, 0,
          strategy);
      System.out.println("zombie created");
      world.addEntity(zombie);
      zombie.scheduleActions(scheduler, world, imageStore);
    }

    else if (dudeFull) {
      Optional<Entity> fullTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(House.class)));

      if (fullTarget.isPresent() && moveTo(world, fullTarget.get(), scheduler)) {
        transform(world, scheduler, imageStore);
      } else {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
      }
    } else {

      Optional<Entity> target = world.findNearest(getPosition(),
          new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

      if (target.isEmpty() || !moveTo(world, target.get(), scheduler) || !transform(world, scheduler, imageStore)) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
      }
    }
  }

  public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
    if (dudeFull) {
      if (adjacent(getPosition(), target.getPosition())) {
        return true;
      } else {
        Point nextPos = nextPosition(world, target.getPosition());

        if (!getPosition().equals(nextPos)) {
          world.moveEntity(scheduler, this, nextPos);
        }
        return false;
      }
    } else {
      if (adjacent(getPosition(), target.getPosition())) {
        resourceCount += 1;
        if (target.getClass() == Tree.class)
          ((Tree) target).setHealth(((Tree) target).getHealth() - 1);
        else if (target.getClass() == Sapling.class) {
          ((Sapling) target).setHealth(((Sapling) target).getHealth() - 1);
        }
        return true;
      } else {
        Point nextPos = nextPosition(world, target.getPosition());

        if (!getPosition().equals(nextPos)) {
          world.moveEntity(scheduler, this, nextPos);
        }
        return false;
      }
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

  public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    if (dudeFull) {

      Entity dude = new WorldModel().createDudeNotFull(getId(), getPosition(), getActionPeriod(), getAnimationPeriod(),
          resourceLimit, getImages(), this.getStrategy());

      world.removeEntity(scheduler, this);

      world.addEntity(dude);
      ((hasAnimations) dude).scheduleActions(scheduler, world, imageStore);
      return true;
    } else {
      if (resourceCount >= resourceLimit) {
        Entity dude = new WorldModel().createDudeFull(getId(), getPosition(), getActionPeriod(), getAnimationPeriod(),
            resourceLimit, getImages(), this.getStrategy());

        world.removeEntity(scheduler, this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(dude);
        ((hasAnimations) dude).scheduleActions(scheduler, world, imageStore);

        return true;
      }

      return false;
    }
  }

  private PathingStrategy getStrategy() {
    return this.strategy;
  }
}
