/**
 * An action that can be taken by an entity
 */
public final class Action {
  private ActionKind kind;
  private Entity entity;
  private WorldModel world;
  private ImageStore imageStore;
  private int repeatCount;

  public Action(ActionKind kind, Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
    this.kind = kind;
    this.entity = entity;
    this.world = world;
    this.imageStore = imageStore;
    this.repeatCount = repeatCount;
  }
  ActionKind getKind() {
    return kind;
  }

  Entity getEntity() {
    return entity;
  }
  WorldModel getWorld() {
    return world;
  }
  ImageStore getImageStore() {
    return imageStore;
  }
  int getRepeatCount() {
    return repeatCount;
  }

}