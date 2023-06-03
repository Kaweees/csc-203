import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Dude extends hasActions implements canMove, canTransform {
  private int resourceLimit;
  private int resourceCount;
  private boolean dudeFull;

  Dude(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double animationPeriod,
      double actionPeriod, boolean dudeFull) {
    super(id, position, images, animationPeriod, actionPeriod);
    this.dudeFull = dudeFull;
    this.resourceLimit = resourceLimit;
    this.resourceCount = resourceCount;
  }

  public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
    if (dudeFull) {
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
    int horiz = Integer.signum(destPos.x - getPosition().x);
    Point newPos = new Point(getPosition().x + horiz, getPosition().y);

    if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
      int vert = Integer.signum(destPos.y - getPosition().y);
      newPos = new Point(getPosition().x, getPosition().y + vert);

      if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
        newPos = getPosition();
      }
    }

    return newPos;
  }

  public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
    scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
  }

  public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    if (dudeFull) {
      Entity dude = new WorldModel().createDudeNotFull(getId(), getPosition(), getActionPeriod(), getAnimationPeriod(),
          resourceLimit, getImages());

      world.removeEntity(scheduler, this);

      world.addEntity(dude);
      ((hasAnimations) dude).scheduleActions(scheduler, world, imageStore);
      return true;
    } else {
      if (resourceCount >= resourceLimit) {
        Entity dude = new WorldModel().createDudeFull(getId(), getPosition(), getActionPeriod(), getAnimationPeriod(),
            resourceLimit, getImages());

        world.removeEntity(scheduler, this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(dude);
        ((hasAnimations) dude).scheduleActions(scheduler, world, imageStore);

        return true;
      }

      return false;
    }
  }
}
