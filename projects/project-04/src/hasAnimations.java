import java.util.List;

import processing.core.PImage;

public abstract class hasAnimations extends Entity {
  private double animationPeriod;

  hasAnimations(String id, Point position, List<PImage> images, double animationPeriod) {
    super(id, position, images);
    this.animationPeriod = animationPeriod;
  }

  public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

  public Action createAnimationAction(int repeatCount) {
    return new animateAction(this, null, null, repeatCount);
  }

  public double getAnimationPeriod() {
    return animationPeriod;
  }
}
