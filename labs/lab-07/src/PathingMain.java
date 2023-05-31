import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import processing.core.*;

public class PathingMain extends PApplet {
  private List<PImage> images;
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

    path = new LinkedList<Point>();
    wPos = new Point(2, 2);
    images = new ArrayList<>();
    images.add(loadImage("images/wyvern1.bmp"));
    images.add(loadImage("images/wyvern2.bmp"));
    images.add(loadImage("images/wyvern3.bmp"));

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
    for (GridValues[] gridValues : grid) {
      Arrays.fill(gridValues, GridValues.BACKGROUND);
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
    current_image = (current_image + 1) % images.size();
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

    image(images.get(current_image), wPos.x * TILE_SIZE, wPos.y * TILE_SIZE);
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
        rect(p.x * TILE_SIZE + (float) (TILE_SIZE * 3) / 8, p.y * TILE_SIZE + (float) (TILE_SIZE * 3) / 8, (float) TILE_SIZE
                / 4, (float) TILE_SIZE / 4);
      }
    }
  }

  private void draw_tile(int row, int col) {
    switch (grid[row][col]) {
    case BACKGROUND -> image(background, col * TILE_SIZE, row * TILE_SIZE);
    case OBSTACLE -> image(obstacle, col * TILE_SIZE, row * TILE_SIZE);
    case SEARCHED -> {
      fill(0, 128);
      rect(col * TILE_SIZE + (float) TILE_SIZE / 4, row * TILE_SIZE + (float) TILE_SIZE / 4, (float) TILE_SIZE / 2, (float) TILE_SIZE
              / 2);
    }
    case GOAL -> image(goal, col * TILE_SIZE, row * TILE_SIZE);
    }
  }

  public static void main(String[] args) {
    PApplet.main("PathingMain");
  }

  public void keyPressed() {
    if (key == ' ') {
      // clear out prior path
      path.clear();
      // example - replace with dfs
      moveDFS(wPos, grid, path);
    } else if (key == 'p') {
      drawPath ^= true;
      redraw();
    }
  }

  private boolean moveDFS(Point pos, GridValues[][] grid, List<Point> path) {

    // check if my right neighbor is the goal
    if (grid[pos.y][pos.x] == GridValues.GOAL) {
      path.add(0, pos);
      return true;
    }
    grid[pos.y][pos.x] = GridValues.SEARCHED;

    Point upN = new Point(pos.x, pos.y - 1);
    Point downN = new Point(pos.x, pos.y + 1);
    Point leftN = new Point(pos.x - 1, pos.y);
    Point rightN = new Point(pos.x + 1, pos.y);

    ArrayList<Point> neighbors = new ArrayList<Point>();
    neighbors.add(rightN);
    neighbors.add(downN);
    neighbors.add(leftN);
    neighbors.add(upN);

    for (Point node : neighbors) {
      // check node is a valid grid cell, hasn't been searched, and isn't an
      // obstacle
      if (withinBounds(node, grid) && grid[node.y][node.x] != GridValues.OBSTACLE
          && grid[node.y][node.x] != GridValues.SEARCHED) {
        if (moveDFS(node, grid, path)) {
          path.add(0, node);
          return true;
        }
      }
    }
    return false;
  }

  private static boolean withinBounds(Point p, GridValues[][] grid) {
    return p.y >= 0 && p.y < grid.length && p.x >= 0 && p.x < grid[0].length;
  }
}
