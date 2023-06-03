public class animateAction extends Action {
  animateAction(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
    super(entity, world, imageStore, repeatCount);
  }

  public void executeAction(EventScheduler scheduler) {
    super.getEntity().nextImage();
    if (super.getRepeatCount() != 1) {
      scheduler.scheduleEvent(super.getEntity(),
          ((hasAnimations) super.getEntity()).createAnimationAction(Math.max(super.getRepeatCount() - 1, 0)),
          ((hasAnimations) super.getEntity()).getAnimationPeriod());
    }
  }
}
