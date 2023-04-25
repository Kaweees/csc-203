import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running. Keeps track of
 * the size of the world, the background image for each location in the world,
 * and the entities that populate the world.
 */
public final class WorldModel {
  private int numRows;
  private int numCols;
  private Background[][] background;
  private Entity[][] occupancy;
  private Set<Entity> entities;
  private static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have
                                                                       // to
                                                                       // be in
                                                                       // sync
                                                                       // since
                                                                       // grows
                                                                       // and
                                                                       // gains
                                                                       // health
                                                                       // at
                                                                       // same
                                                                       // time
  private static final int SAPLING_HEALTH_LIMIT = 5;
  private static final int PROPERTY_KEY = 0;
  private static final int PROPERTY_ID = 1;
  private static final int PROPERTY_COL = 2;
  private static final int PROPERTY_ROW = 3;
  private static final int ENTITY_NUM_PROPERTIES = 4;
  private static final int STUMP_NUM_PROPERTIES = 0;
  private static final int SAPLING_HEALTH = 0;
  private static final int SAPLING_NUM_PROPERTIES = 1;
  private static final int OBSTACLE_ANIMATION_PERIOD = 0;
  private static final int OBSTACLE_NUM_PROPERTIES = 1;
  private static final int DUDE_ACTION_PERIOD = 0;
  private static final int DUDE_ANIMATION_PERIOD = 1;
  private static final int DUDE_LIMIT = 2;
  private static final int DUDE_NUM_PROPERTIES = 3;
  private static final int HOUSE_NUM_PROPERTIES = 0;
  private static final int FAIRY_ANIMATION_PERIOD = 0;
  private static final int FAIRY_ACTION_PERIOD = 1;
  private static final int FAIRY_NUM_PROPERTIES = 2;
  private static final int TREE_ANIMATION_PERIOD = 0;
  private static final int TREE_ACTION_PERIOD = 1;
  private static final int TREE_HEALTH = 2;
  private static final int TREE_NUM_PROPERTIES = 3;

  public WorldModel() {

  }

  /**
   * Helper method for testing. Don't move or modify this method.
   */
  public List<String> log() {
    List<String> list = new ArrayList<>();
    for (Entity entity : entities) {
      String log = entity.log();
      if (log != null)
        list.add(log);
    }
    return list;
  }

  public int getNumRows() {
    return this.numRows;
  }

  public int getNumCols() {
    return this.numCols;
  }

  public Background getBackgroundCell(Point pos) {
    return this.background[pos.getY()][pos.getX()];
  }

  public Set<Entity> getEntities() {
    return this.entities;
  }

  public void parseSapling(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == SAPLING_NUM_PROPERTIES) {
      int health = Integer.parseInt(properties[SAPLING_HEALTH]);
      Entity entity = createSapling(id, pt, imageStore.getImageList(Functions.SAPLING_KEY), health);
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", Functions.SAPLING_KEY, SAPLING_NUM_PROPERTIES));
    }
  }

  public void parseDude(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == DUDE_NUM_PROPERTIES) {
      Entity entity = createDudeNotFull(id, pt, Double.parseDouble(properties[DUDE_ACTION_PERIOD]),
          Double.parseDouble(properties[DUDE_ANIMATION_PERIOD]), Integer.parseInt(properties[DUDE_LIMIT]),
          imageStore.getImageList(Functions.DUDE_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", Functions.DUDE_KEY, DUDE_NUM_PROPERTIES));
    }
  }

  public void parseFairy(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == FAIRY_NUM_PROPERTIES) {
      Entity entity = createFairy(id, pt, Double.parseDouble(properties[FAIRY_ACTION_PERIOD]),
          Double.parseDouble(properties[FAIRY_ANIMATION_PERIOD]), imageStore.getImageList(Functions.FAIRY_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", Functions.FAIRY_KEY, FAIRY_NUM_PROPERTIES));
    }
  }

  public void parseTree(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == TREE_NUM_PROPERTIES) {
      Entity entity = createTree(id, pt, Double.parseDouble(properties[TREE_ACTION_PERIOD]),
          Double.parseDouble(properties[TREE_ANIMATION_PERIOD]), Integer.parseInt(properties[TREE_HEALTH]),
          imageStore.getImageList(Functions.TREE_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", Functions.TREE_KEY, TREE_NUM_PROPERTIES));
    }
  }

  public void parseObstacle(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == OBSTACLE_NUM_PROPERTIES) {
      Entity entity = createObstacle(id, pt, Double.parseDouble(properties[OBSTACLE_ANIMATION_PERIOD]),
          imageStore.getImageList(Functions.OBSTACLE_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", Functions.OBSTACLE_KEY, OBSTACLE_NUM_PROPERTIES));
    }
  }

  public void parseHouse(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == HOUSE_NUM_PROPERTIES) {
      Entity entity = createHouse(id, pt, imageStore.getImageList(Functions.HOUSE_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", Functions.HOUSE_KEY, HOUSE_NUM_PROPERTIES));
    }
  }

  public void parseStump(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == STUMP_NUM_PROPERTIES) {
      Entity entity = createStump(id, pt, imageStore.getImageList(Functions.STUMP_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", Functions.STUMP_KEY, STUMP_NUM_PROPERTIES));
    }
  }

  public void tryAddEntity(Entity entity) {
    if (isOccupied(entity.getPosition())) {
      // arguably the wrong type of exception, but we are not
      // defining our own exceptions yet
      throw new IllegalArgumentException("position occupied");
    }

    addEntity(entity);
  }

  public boolean withinBounds(Point pos) {
    return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0 && pos.getX() < this.numCols;
  }

  public boolean isOccupied(Point pos) {
    return this.withinBounds(pos) && getOccupancyCell(pos) != null;
  }

  public Optional<Entity> findNearest(Point pos, List<EntityKind> kinds) {
    List<Entity> ofType = new LinkedList<>();
    for (EntityKind kind : kinds) {
      for (Entity entity : this.getEntities()) {
        if (entity.getKind() == kind) {
          ofType.add(entity);
        }
      }
    }

    return nearestEntity(ofType, pos);
  }

  public Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
    if (entities.isEmpty()) {
      return Optional.empty();
    } else {
      Entity nearest = entities.get(0);
      int nearestDistance = nearest.getPosition().distanceSquared(pos);

      for (Entity other : entities) {
        int otherDistance = other.getPosition().distanceSquared(pos);

        if (otherDistance < nearestDistance) {
          nearest = other;
          nearestDistance = otherDistance;
        }
      }

      return Optional.of(nearest);
    }
  }

  /*
   * Assumes that there is no entity currently occupying the intended
   * destination cell.
   */
  public void addEntity(Entity entity) {
    if (withinBounds(entity.getPosition())) {
      setOccupancyCell(entity.getPosition(), entity);
      this.entities.add(entity);
    }
  }

  public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
    Point oldPos = entity.getPosition();
    if (withinBounds(pos) && !pos.equals(oldPos)) {
      setOccupancyCell(oldPos, null);
      Optional<Entity> occupant = getOccupant(pos);
      occupant.ifPresent(target -> removeEntity(scheduler, target));
      setOccupancyCell(pos, entity);
      entity.setPosition(pos);
    }
  }

  public void removeEntity(EventScheduler scheduler, Entity entity) {
    scheduler.unscheduleAllEvents(entity);
    removeEntityAt(entity.getPosition());
  }

  public void removeEntityAt(Point pos) {
    if (withinBounds(pos) && getOccupancyCell(pos) != null) {
      Entity entity = getOccupancyCell(pos);

      /*
       * This moves the entity just outside the grid for debugging purposes.
       */
      entity.setPosition(new Point(-1, -1));
      this.entities.remove(entity);
      setOccupancyCell(pos, null);
    }
  }

  public Optional<Entity> getOccupant(Point pos) {
    if (isOccupied(pos)) {
      return Optional.of(getOccupancyCell(pos));
    } else {
      return Optional.empty();
    }
  }

  public Entity getOccupancyCell(Point pos) {
    return this.occupancy[pos.getY()][pos.getX()];
  }

  public void setOccupancyCell(Point pos, Entity entity) {
    this.occupancy[pos.getY()][pos.getX()] = entity;
  }

  public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground) {
    parseSaveFile(saveFile, imageStore, defaultBackground);
    if (this.background == null) {
      this.background = new Background[this.numRows][this.numCols];
      for (Background[] row : this.background)
        Arrays.fill(row, defaultBackground);
    }
    if (this.occupancy == null) {
      this.occupancy = new Entity[this.numRows][this.numCols];
      this.entities = new HashSet<>();
    }
  }

  public void parseSaveFile(Scanner saveFile, ImageStore imageStore, Background defaultBackground) {
    String lastHeader = "";
    int headerLine = 0;
    int lineCounter = 0;
    while (saveFile.hasNextLine()) {
      lineCounter++;
      String line = saveFile.nextLine().strip();
      if (line.endsWith(":")) {
        headerLine = lineCounter;
        lastHeader = line;
        switch (line) {
        case "Backgrounds:" -> this.background = new Background[this.numRows][this.numCols];
        case "Entities:" -> {
          this.occupancy = new Entity[this.numRows][this.numCols];
          this.entities = new HashSet<>();
        }
        }
      } else {
        switch (lastHeader) {
        case "Rows:" -> this.numRows = Integer.parseInt(line);
        case "Cols:" -> this.numCols = Integer.parseInt(line);
        case "Backgrounds:" -> parseBackgroundRow(line, lineCounter - headerLine - 1, imageStore);
        case "Entities:" -> parseEntity(line, imageStore);
        }
      }
    }
  }

  public void parseBackgroundRow(String line, int row, ImageStore imageStore) {
    String[] cells = line.split(" ");
    if (row < this.numRows) {
      int rows = Math.min(cells.length, this.numCols);
      for (int col = 0; col < rows; col++) {
        this.background[row][col] = new Background(cells[col], imageStore.getImageList(cells[col]));
      }
    }
  }

  public void parseEntity(String line, ImageStore imageStore) {
    String[] properties = line.split(" ", ENTITY_NUM_PROPERTIES + 1);
    if (properties.length >= ENTITY_NUM_PROPERTIES) {
      String key = properties[PROPERTY_KEY];
      String id = properties[PROPERTY_ID];
      Point pt = new Point(Integer.parseInt(properties[PROPERTY_COL]), Integer.parseInt(properties[PROPERTY_ROW]));

      properties = properties.length == ENTITY_NUM_PROPERTIES ? new String[0]
          : properties[ENTITY_NUM_PROPERTIES].split(" ");

      switch (key) {
      case Functions.OBSTACLE_KEY -> parseObstacle(properties, pt, id, imageStore);
      case Functions.DUDE_KEY -> parseDude(properties, pt, id, imageStore);
      case Functions.FAIRY_KEY -> parseFairy(properties, pt, id, imageStore);
      case Functions.HOUSE_KEY -> parseHouse(properties, pt, id, imageStore);
      case Functions.TREE_KEY -> parseTree(properties, pt, id, imageStore);
      case Functions.SAPLING_KEY -> parseSapling(properties, pt, id, imageStore);
      case Functions.STUMP_KEY -> parseStump(properties, pt, id, imageStore);
      default -> throw new IllegalArgumentException("Entity key is unknown");
      }
    } else {
      throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
    }
  }

  public Entity createHouse(String id, Point position, List<PImage> images) {
    return new Entity(EntityKind.HOUSE, id, position, images, 0, 0, 0, 0, 0, 0);
  }

  public Entity createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
    return new Entity(EntityKind.OBSTACLE, id, position, images, 0, 0, 0, animationPeriod, 0, 0);
  }

  public Entity createTree(String id, Point position, double actionPeriod, double animationPeriod, int health,
      List<PImage> images) {
    return new Entity(EntityKind.TREE, id, position, images, 0, 0, actionPeriod, animationPeriod, health, 0);
  }

  public Entity createStump(String id, Point position, List<PImage> images) {
    return new Entity(EntityKind.STUMP, id, position, images, 0, 0, 0, 0, 0, 0);
  }

  // health starts at 0 and builds up until ready to convert to Tree
  public Entity createSapling(String id, Point position, List<PImage> images, int health) {
    return new Entity(EntityKind.SAPLING, id, position, images, 0, 0, SAPLING_ACTION_ANIMATION_PERIOD,
        SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
  }

  public Entity createFairy(String id, Point position, double actionPeriod, double animationPeriod,
      List<PImage> images) {
    return new Entity(EntityKind.FAIRY, id, position, images, 0, 0, actionPeriod, animationPeriod, 0, 0);
  }

  // need resource count, though it always starts at 0
  public Entity createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod,
      int resourceLimit, List<PImage> images) {
    return new Entity(EntityKind.DUDE_NOT_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod,
        0, 0);
  }

  // don't technically need resource count ... full
  public Entity createDudeFull(String id, Point position, double actionPeriod, double animationPeriod,
      int resourceLimit, List<PImage> images) {
    return new Entity(EntityKind.DUDE_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0,
        0);
  }

  public void scheduleActions(EventScheduler scheduler, ImageStore imageStore) {
    for (Entity entity : this.entities) {
      entity.scheduleActions(scheduler, this, imageStore);
    }
  }

  public Optional<PImage> getBackgroundImage(Point pos) {
    if (withinBounds(pos)) {
      return Optional.of(getBackgroundCell(pos).getCurrentImage());
    } else {
      return Optional.empty();
    }
  }
}
