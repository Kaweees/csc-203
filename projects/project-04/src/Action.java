/**
 * An action that can be taken by an entity
 */
public abstract class Action {
  private final Entity entity;
  private final WorldModel world;
  private final ImageStore imageStore;
  private final int repeatCount;

  public Action(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
    this.entity = entity;
    this.world = world;
    this.imageStore = imageStore;
    this.repeatCount = repeatCount;
  }

  public Entity getEntity() {
    return this.entity;
  }

  public ImageStore getImageStore() {
    return imageStore;
  }

  public int getRepeatCount() {
    return repeatCount;
  }

  public WorldModel getWorld() {
    return world;
  }

  abstract void executeAction(EventScheduler scheduler);

}
