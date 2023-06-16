import java.util.List;

import processing.core.PImage;

public class Obstacle extends hasAnimations {
  Obstacle(String id, Point position, List<PImage> images, double animationPeriod) {
    super(id, position, images, animationPeriod);
  }

  public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
    scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
  }
}
