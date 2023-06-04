import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import processing.core.PImage;

/**
 * Represents the 2D World in which this simulation is running. Keeps track of
 * the size of the world, the background image for each location in the world,
 * and the entities that populate the world.
 */
public final class WorldModel {
  public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz",
      "dirt_vert_left", "dirt_vert_right", "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));
  public static final String STUMP_KEY = "stump";
  public static final String SAPLING_KEY = "sapling";
  public static final String OBSTACLE_KEY = "obstacle";
  public static final String DUDE_KEY = "dude";
  public static final String HOUSE_KEY = "house";
  public static final String FAIRY_KEY = "fairy";
  public static final String TREE_KEY = "tree";
  private static final int PROPERTY_KEY = 0;
  private static final int PROPERTY_ID = 1;
  private static final int PROPERTY_COL = 2;
  private static final int PROPERTY_ROW = 3;
  private static final int ENTITY_NUM_PROPERTIES = 4;
  private static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have
                                                                       // to be
                                                                       // in
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
  private int numRows;
  private int numCols;
  private Background[][] background;
  private Entity[][] occupancy;
  private Set<Entity> entities;

  public WorldModel() {

  }

  public int getNumRows() {
    return numRows;
  }

  public int getNumCols() {
    return numCols;
  }

  public Set<Entity> getEntities() {
    return entities;
  }

  public void parseDude(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == DUDE_NUM_PROPERTIES) {
      AStarPathingStrategy strategy = new AStarPathingStrategy();
      Entity entity = new WorldModel().createDudeNotFull(id, pt, Double.parseDouble(properties[DUDE_ACTION_PERIOD]),
          Double.parseDouble(properties[DUDE_ANIMATION_PERIOD]), Integer.parseInt(properties[DUDE_LIMIT]),
          imageStore.getImageList(DUDE_KEY), strategy);
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", DUDE_KEY, DUDE_NUM_PROPERTIES));
    }
  }

  public void parseFairy(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == FAIRY_NUM_PROPERTIES) {
      AStarPathingStrategy strategy = new AStarPathingStrategy();
      Entity entity = new WorldModel().createFairy(id, pt, Double.parseDouble(properties[FAIRY_ACTION_PERIOD]),
          Double.parseDouble(properties[FAIRY_ANIMATION_PERIOD]), imageStore.getImageList(FAIRY_KEY), strategy);
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", FAIRY_KEY, FAIRY_NUM_PROPERTIES));
    }
  }

  public void parseTree(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == TREE_NUM_PROPERTIES) {
      Entity entity = new WorldModel().createTree(id, pt, Double.parseDouble(properties[TREE_ACTION_PERIOD]),
          Double.parseDouble(properties[TREE_ANIMATION_PERIOD]), Integer.parseInt(properties[TREE_HEALTH]),
          imageStore.getImageList(TREE_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", TREE_KEY, TREE_NUM_PROPERTIES));
    }
  }

  public void parseObstacle(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == OBSTACLE_NUM_PROPERTIES) {
      Entity entity = new WorldModel().createObstacle(id, pt, Double.parseDouble(properties[OBSTACLE_ANIMATION_PERIOD]),
          imageStore.getImageList(OBSTACLE_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", OBSTACLE_KEY, OBSTACLE_NUM_PROPERTIES));
    }
  }

  public void parseHouse(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == HOUSE_NUM_PROPERTIES) {
      Entity entity = new WorldModel().createHouse(id, pt, imageStore.getImageList(HOUSE_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", HOUSE_KEY, HOUSE_NUM_PROPERTIES));
    }
  }

  public void parseStump(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == STUMP_NUM_PROPERTIES) {
      Entity entity = new WorldModel().createStump(id, pt, imageStore.getImageList(STUMP_KEY));
      tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", STUMP_KEY, STUMP_NUM_PROPERTIES));
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
    return pos.y >= 0 && pos.y < numRows && pos.x >= 0 && pos.x < numCols;
  }

  public boolean isOccupied(Point pos) {
    return this.withinBounds(pos) && getOccupancyCell(pos) != null;
  }

  public Optional<Entity> findNearest(Point pos, ArrayList<Class> classes) {
    List<Entity> ofType = new LinkedList<>();
    for (Class myClass : classes) {
      for (Entity entity : entities) {
        if (entity.getClass() == myClass) {
          ofType.add(entity);
        }
      }
    }

    return nearestEntity(ofType, pos);
  }

  /*
   * Assumes that there is no entity currently occupying the intended
   * destination cell.
   */
  public void addEntity(Entity entity) {
    if (this.withinBounds(entity.getPosition())) {
      setOccupancyCell(entity.getPosition(), entity);
      entities.add(entity);
    }
  }

  public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
    Point oldPos = entity.getPosition();
    if (this.withinBounds(pos) && !pos.equals(oldPos)) {
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
    if (this.withinBounds(pos) && getOccupancyCell(pos) != null) {
      Entity entity = getOccupancyCell(pos);

      /*
       * This moves the entity just outside of the grid for debugging purposes.
       */
      entity.setPosition(new Point(-1, -1));
      entities.remove(entity);
      setOccupancyCell(pos, null);
    }
  }

  public Optional<Entity> getOccupant(Point pos) {
    if (this.isOccupied(pos)) {
      return Optional.of(getOccupancyCell(pos));
    } else {
      return Optional.empty();
    }
  }

  public Entity getOccupancyCell(Point pos) {
    return occupancy[pos.y][pos.x];
  }

  public void setOccupancyCell(Point pos, Entity entity) {
    occupancy[pos.y][pos.x] = entity;
  }

  public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground) {
    parseSaveFile(saveFile, imageStore, defaultBackground);
    if (background == null) {
      background = new Background[numRows][numCols];
      for (Background[] row : background)
        Arrays.fill(row, defaultBackground);
    }
    if (occupancy == null) {
      occupancy = new Entity[numRows][numCols];
      entities = new HashSet<>();
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
        case "Backgrounds:" -> background = new Background[numRows][numCols];
        case "Entities:" -> {
          occupancy = new Entity[numRows][numCols];
          entities = new HashSet<>();
        }
        }
      } else {
        switch (lastHeader) {
        case "Rows:" -> numRows = Integer.parseInt(line);
        case "Cols:" -> numCols = Integer.parseInt(line);
        case "Backgrounds:" -> parseBackgroundRow(line, lineCounter - headerLine - 1, imageStore);
        case "Entities:" -> parseEntity(line, imageStore);
        }
      }
    }
  }

  public void parseBackgroundRow(String line, int row, ImageStore imageStore) {
    String[] cells = line.split(" ");
    if (row < numRows) {
      int rows = Math.min(cells.length, numCols);
      for (int col = 0; col < rows; col++) {
        background[row][col] = new Background(cells[col], imageStore.getImageList(cells[col]));
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
      case OBSTACLE_KEY -> this.parseObstacle(properties, pt, id, imageStore);
      case DUDE_KEY -> this.parseDude(properties, pt, id, imageStore);
      case FAIRY_KEY -> this.parseFairy(properties, pt, id, imageStore);
      case HOUSE_KEY -> this.parseHouse(properties, pt, id, imageStore);
      case TREE_KEY -> this.parseTree(properties, pt, id, imageStore);
      case SAPLING_KEY -> parseSapling(properties, pt, id, imageStore);
      case STUMP_KEY -> this.parseStump(properties, pt, id, imageStore);
      default -> throw new IllegalArgumentException("Entity key is unknown");
      }
    } else {
      throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
    }
  }

  public Background getBackgroundCell(Point pos) {
    return background[pos.y][pos.x];
  }

  public void setBackgroundCell(Point pos, Background background) {
    this.background[pos.y][pos.x] = background;
  }

  public Optional<PImage> getBackgroundImage(Point pos) {
    if (this.withinBounds(pos)) {
      return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
    } else {
      return Optional.empty();
    }
  }

  public Entity createHouse(String id, Point position, List<PImage> images) {
    return new House(id, position, images);
  }

  public Entity createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
    return new Obstacle(id, position, images, animationPeriod);
  }

  public Entity createTree(String id, Point position, double actionPeriod, double animationPeriod, int health,
      List<PImage> images) {
    return new Tree(id, position, images, animationPeriod, actionPeriod, health, 0);
  }

  public Entity createStump(String id, Point position, List<PImage> images) {
    return new Stump(id, position, images);
  }

  // health starts at 0 and builds up until ready to convert to Tree
  public Entity createSapling(String id, Point position, List<PImage> images, int health) {
    return new Sapling(id, position, images, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0,
        SAPLING_HEALTH_LIMIT);
  }

  public Entity createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images,
      PathingStrategy strategy) {
    return new Fairy(id, position, images, animationPeriod, actionPeriod, strategy);
  }

  // need resource count, though it always starts at 0
  public Entity createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod,
      int resourceLimit, List<PImage> images, PathingStrategy strategy) {
    return new Dude(id, position, images, resourceLimit, 0, animationPeriod, actionPeriod, false, strategy);
  }

  // don't technically need resource count ... full
  public Entity createDudeFull(String id, Point position, double actionPeriod, double animationPeriod,
      int resourceLimit, List<PImage> images, PathingStrategy strategy) {
    return new Dude(id, position, images, resourceLimit, 0, animationPeriod, actionPeriod, true, strategy);
  }

  public Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
    if (entities.isEmpty()) {
      return Optional.empty();
    } else {
      Entity nearest = entities.get(0);
      int nearestDistance = new WorldModel().distanceSquared(nearest.getPosition(), pos);

      for (Entity other : entities) {
        int otherDistance = new WorldModel().distanceSquared(other.getPosition(), pos);

        if (otherDistance < nearestDistance) {
          nearest = other;
          nearestDistance = otherDistance;
        }
      }

      return Optional.of(nearest);
    }
  }

  public int distanceSquared(Point p1, Point p2) {
    int deltaX = p1.x - p2.x;
    int deltaY = p1.y - p2.y;

    return deltaX * deltaX + deltaY * deltaY;
  }

  public void parseSapling(String[] properties, Point pt, String id, ImageStore imageStore) {
    if (properties.length == SAPLING_NUM_PROPERTIES) {
      int health = Integer.parseInt(properties[SAPLING_HEALTH]);
      Entity entity = createSapling(id, pt, imageStore.getImageList(SAPLING_KEY), health);
      this.tryAddEntity(entity);
    } else {
      throw new IllegalArgumentException(
          String.format("%s requires %d properties when parsing", SAPLING_KEY, SAPLING_NUM_PROPERTIES));
    }
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
}
