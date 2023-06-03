import java.util.List;

import processing.core.PImage;

public abstract class hasActions extends hasAnimations {
  private final double actionPeriod;

  hasActions(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod) {
    super(id, position, images, animationPeriod);
    this.actionPeriod = actionPeriod;
  }

  public double getActionPeriod() {
    return actionPeriod;
  }

  public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

  public Action createActivityAction(WorldModel world, ImageStore imageStore) {
    return new activeAction(this, world, imageStore, 0);
  }
}
