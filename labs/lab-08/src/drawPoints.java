
/*
 * Helper For Lab 8
 */
import processing.core.*;

public class drawPoints extends PApplet {

  public void settings() {
    size(500, 500);
  }

  public void setup() {
    background(180);
    noLoop();
  }

  public void draw() {

    double x, y;

    String[] lines = loadStrings("drawMe.txt");
    println("there are " + lines.length);
    for (String line : lines) {
      if (line.length() > 0) {
        String[] words = line.split(",");
        x = Double.parseDouble(words[0]);
        y = Double.parseDouble(words[1]);
        println("xy: " + x + " " + y);
        ellipse((int) x, (int) y, 1, 1);
      }
    }
  }

  public static void main(String[] args) {
    PApplet.main("drawPoints");
  }
}
