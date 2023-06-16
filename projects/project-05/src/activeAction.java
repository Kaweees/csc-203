public class activeAction extends Action {

  activeAction(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
    super(entity, world, imageStore, repeatCount);
  }

  public void executeAction(EventScheduler scheduler) {
    if (super.getEntity() instanceof hasActions) {
      ((hasActions) super.getEntity()).executeActivity(super.getWorld(), super.getImageStore(), scheduler);
    }
  }
}
