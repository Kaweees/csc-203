package Lab7;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import processing.core.PApplet;
import processing.core.PImage;

public class PathingMain extends PApplet {
  private List<PImage> imgs;
  private int current_image;
  private long next_time;
  private PImage background;
  private PImage obstacle;
  private PImage goal;
  private List<Point> path;

  private static final int TILE_SIZE = 32;

  private static final int ANIMATION_TIME = 100;

  private GridValues[][] grid;
  private static final int ROWS = 15;
  private static final int COLS = 20;

  private static enum GridValues {
    BACKGROUND, OBSTACLE, GOAL, SEARCHED
  };

  private Point wPos;

  private boolean drawPath = false;

  public void settings() {
    size(640, 480);
  }

  /* runs once to set up world */
  public void setup() {

    path = new LinkedList<>();
    wPos = new Point(2, 2);
    imgs = new ArrayList<>();
    imgs.add(loadImage("images/wyvern1.bmp"));
    imgs.add(loadImage("images/wyvern2.bmp"));
    imgs.add(loadImage("images/wyvern3.bmp"));

    background = loadImage("images/grass.bmp");
    obstacle = loadImage("images/vein.bmp");
    goal = loadImage("images/water.bmp");

    grid = new GridValues[ROWS][COLS];
    initialize_grid(grid);

    current_image = 0;
    next_time = System.currentTimeMillis() + ANIMATION_TIME;
    noLoop();
    draw();
  }

  /* set up a 2D grid to represent the world */
  private static void initialize_grid(GridValues[][] grid) {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        grid[row][col] = GridValues.BACKGROUND;
      }
    }

    // set up some obstacles
    for (int row = 2; row < 8; row++) {
      grid[row][row + 5] = GridValues.OBSTACLE;
    }

    for (int row = 8; row < 12; row++) {
      grid[row][19 - row] = GridValues.OBSTACLE;
    }

    for (int col = 1; col < 8; col++) {
      grid[11][col] = GridValues.OBSTACLE;
    }

    grid[13][14] = GridValues.GOAL;
  }

  private void next_image() {
    current_image = (current_image + 1) % imgs.size();
  }

  /* runs over and over */
  public void draw() {
    // A simplified action scheduling handler
    long time = System.currentTimeMillis();
    if (time >= next_time) {
      next_image();
      next_time = time + ANIMATION_TIME;
    }

    draw_grid();
    draw_path();

    image(imgs.get(current_image), wPos.x * TILE_SIZE, wPos.y * TILE_SIZE);
  }

  private void draw_grid() {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        draw_tile(row, col);
      }
    }
  }

  private void draw_path() {
    if (drawPath) {
      for (Point p : path) {
        fill(128, 0, 0);
        rect(p.x * TILE_SIZE + TILE_SIZE * 3 / 8, p.y * TILE_SIZE + TILE_SIZE * 3 / 8, TILE_SIZE / 4, TILE_SIZE / 4);
      }
    }
  }

  private void draw_tile(int row, int col) {
    switch (grid[row][col]) {
    case BACKGROUND:
      image(background, col * TILE_SIZE, row * TILE_SIZE);
      break;
    case OBSTACLE:
      image(obstacle, col * TILE_SIZE, row * TILE_SIZE);
      break;
    case SEARCHED:
      fill(0, 128);
      rect(col * TILE_SIZE + TILE_SIZE / 4, row * TILE_SIZE + TILE_SIZE / 4, TILE_SIZE / 2, TILE_SIZE / 2);
      break;
    case GOAL:
      image(goal, col * TILE_SIZE, row * TILE_SIZE);
      break;
    }
  }

  public static void main(String args[]) {
    PApplet.main("Lab7.PathingMain");
  }

  public void keyPressed() {
    if (key == ' ') {
      // clear out prior path
      path.clear();
      // example - replace with dfs
      moveOnce(wPos, grid, path);
    } else if (key == 'p') {
      drawPath ^= true;
      redraw();
    }
  }

  /*
   * replace the below with a depth first search this code provided only as an
   * example of moving in in one direction for one tile - it mostly is for
   * illustrating how you might test the occupancy grid and add nodes to path!
   */
  private boolean moveOnce(Lab7.Point pos, GridValues[][] grid, List<Point> path) {
    try {
      Thread.sleep(200);
    } catch (Exception e) {
    }
    redraw();

    Point rightN = new Point(pos.x + 1, pos.y);
    Point leftN = new Point(pos.x - 1, pos.y);
    Point upN = new Point(pos.x, pos.y - 1);
    Point downN = new Point(pos.x, pos.y + 1);
    ArrayList<Point> neighbors = new ArrayList<Point>();
    neighbors.add(rightN);
    neighbors.add(downN);
    neighbors.add(leftN);
    neighbors.add(upN);
//     check if current node is the goal
    if (grid[pos.y][pos.x] == GridValues.GOAL) {
      path.add(0, pos);
      return true;
    }

    // set this value as searched
    grid[pos.y][pos.x] = GridValues.SEARCHED;

    System.out.printf("(%d, %d) : %s\n", pos.x, pos.y, grid[pos.y][pos.x]);

    for (Point node : neighbors) {
      // check if node is a valid grid cell and not an obstacle
      if (withinBounds(node, grid) && grid[node.y][node.x] != GridValues.OBSTACLE
          && grid[node.y][node.x] != GridValues.SEARCHED) {
        // check if current node is the goal
        if (moveOnce(node, grid, path)) {
          path.add(0, node);

        }
      }
    }
    return false;
  }

  // if (!found && withinBounds(leftN, grid) && grid[leftN.y][leftN.x] !=
  // GridValues.OBSTACLE
  // && grid[leftN.y][leftN.x] != GridValues.SEARCHED) {
  // found = findDFS(leftN, grid, path);
  // }
  // if (!found && withinBounds(upN, grid) && grid[upN.y][upN.x] !=
  // GridValues.OBSTACLE
  // && grid[upN.y][upN.x] != GridValues.SEARCHED) {
  // found = findDFS(upN, grid, path);
  // }
  // if (!found && withinBounds(downN, grid) && grid[downN.y][downN.x] !=
  // GridValues.OBSTACLE
  // && grid[downN.y][downN.x] != GridValues.SEARCHED) {
  // found = findDFS(downN, grid, path);
  // }
  // if (found) {
  // path.add(0, pos);
  // }
  // }
  // return found;
  // }
  // Point[] neighbors = new Point[4];
  // neighbors[0] = new Point(pos.x + 1, pos.y); // right neighbor
  // neighbors[1] = new Point(pos.x - 1, pos.y); // left neighbor
  // neighbors[2] = new Point(pos.x, pos.y - 1); // up neighbor
  // neighbors[3] = new Point(pos.x, pos.y + 1); // down neighbor
  // // test if this is a valid grid cell
  // for (Point node : neighbors) {
  // if (withinBounds(node, grid) && grid[node.y][node.x] != GridValues.OBSTACLE
  // && grid[node.y][node.x] != GridValues.SEARCHED) {
  // // check if neighbor is the goal
  // if (grid[node.y][node.x] == GridValues.GOAL) {
  // path.add(0, node);
  // return true;
  // } else {
  // // set neighbor as searched
  // grid[node.y][node.x] = GridValues.SEARCHED;
  //
  // }
  // }
  // }

  // private int findDFS(GridValues[][] grid, int row, int col, List<Point>
  // path) {
  // // check if this is a valid grid cell
  // if (withinBounds(new Point(col, row), grid) && grid[row][col] !=
  // GridValues.OBSTACLE
  // && grid[row][col] != GridValues.SEARCHED) {
  // // check if my right neighbor is the goal
  // if (grid[row][col] == GridValues.GOAL) {
  // path.add(0, new Point(col, row));
  // return true;
  // }
  // // set this value as searched
  // grid[row][col] = GridValues.SEARCHED;
  // // check if I can move right
  // if (findDFS(row, col + 1, grid, path)) {
  // path.add(0, new Point(col, row));
  // return true;
  // }
  // // check if I can move down
  // if (findDFS(row + 1, col, grid, path)) {
  // path.add(0, new Point(col, row));
  // return true;
  // }
  // // check if I can move left
  // if (findDFS(row, col - 1, grid, path)) {
  // path.add(0, new Point(col, row));
  // return true;
  // }
  // // check if I can move up
  // if (findDFS(row - 1, col, grid, path)) {
  // path.add(0, new Point(col, row));
  // return true;
  // }
  // }
  // return false;
  // }

  private static boolean withinBounds(Point p, GridValues[][] grid) {
    return p.y >= 0 && p.y < grid.length && p.x >= 0 && p.x < grid[0].length;
  }
}
