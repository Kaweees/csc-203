import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Fairy extends hasActions implements canMove {

  Fairy(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod) {
    super(id, position, images, animationPeriod, actionPeriod);
  }

  public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
    Optional<Entity> fairyTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(Stump.class)));

    if (fairyTarget.isPresent()) {
      Point tgtPos = fairyTarget.get().getPosition();

      if (moveTo(world, fairyTarget.get(), scheduler)) {

        Entity sapling = new WorldModel().createSapling(WorldModel.SAPLING_KEY + "_" + fairyTarget.get().getId(),
            tgtPos, imageStore.getImageList(WorldModel.SAPLING_KEY), 0);

        world.addEntity(sapling);
        ((hasAnimations) sapling).scheduleActions(scheduler, world, imageStore);
      }
    }

    scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
  }

  public Point nextPosition(WorldModel world, Point destPos) {
    int horiz = Integer.signum(destPos.x - getPosition().x);
    Point newPos = new Point(getPosition().x + horiz, getPosition().y);

    if (horiz == 0 || world.isOccupied(newPos)) {
      int vert = Integer.signum(destPos.y - getPosition().y);
      newPos = new Point(getPosition().x, getPosition().y + vert);

      if (vert == 0 || world.isOccupied(newPos)) {
        newPos = getPosition();
      }
    }

    return newPos;
  }

  public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
    scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
  }

  public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
    if (adjacent(getPosition(), target.getPosition())) {
      world.removeEntity(scheduler, target);
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
