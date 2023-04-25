import java.util.List;

import processing.core.PApplet;

/**
 * Plots greenhouse gas emissions data. You will be making changes to this file.
 *
 * @author CSC/CPE 203 staff
 */
public class Plotter extends PApplet {
  private final static int MARGIN_LR = 100;
  private final static int MARGIN_TB = 100;
  private final static int MARK_SIZE = 5;

  private final EmissionsDatabase db;
  private int[][] colourPalette;

  /**
   * Create a new Plotter with a fresh EmissionsDatabase.
   */
  public Plotter() {
    this.db = new EmissionsDatabase();
  }

  /**
   * Gets called automatically once before setup.
   */
  @Override
  public void settings() {
    size(900, 600);
  }

  /**
   * Gets called automatically before drawing.
   */
  @Override
  public void setup() {
    background(255);
    this.colourPalette = new int[195][3];
    for (int i = 0; i < this.colourPalette.length; i++) {
      this.colourPalette[i][0] = (int) random(256);
      this.colourPalette[i][1] = (int) random(256);
      this.colourPalette[i][2] = (int) random(256);
    }
  }

  /**
   * Gets called automatically and repeatedly.
   */
  @Override
  public void draw() {
    textSize(14);
    background(255);
    this.plotEmissionsData(this.db.getSectors(), this.db.getSectorMinEmission(), this.db.getSectorMaxEmission());
  }

  // Plots sector emission data.
  // TODO: Make changes here (and appropriate changes elsewhere) to support
  // plotting
  // either Country or Sector emissions using Object-oriented design principles.
  private void plotEmissionsData(List<GreenhouseGasEmitter> emitters, double min, double max) {
    writePlotTitle("Greenhouse gas emissions");
    writeYearsAxis();
    writeEmissionsAxis(min, max);

    int toolTipX = -1;
    int toolTipY = -1;
    String toolTipName = null;

    int colourIndex = 0;
    for (GreenhouseGasEmitter emitter : emitters) {
      // First, choose a colour for the points being plotted, and update the
      // colourIndex
      int[] rgb = this.colourPalette[colourIndex % this.colourPalette.length];
      colourIndex = colourIndex + 1;

      // Plot the greenhouse gas emissions from each year from 1970 to 2012
      for (int year = 1970; year <= 2012; year++) {
        double emission = emitter.getEmissionsInYear(year);
        stroke(rgb[0], rgb[1], rgb[2]);
        fill(rgb[0], rgb[1], rgb[2]);

        // Draw the point at the specified x-axis and y-axis
        int x = mapYear(year);
        int y = mapEmissions(emission, min, max);
        circle(x, y, MARK_SIZE);

        // Determine if the mouse is hovering within a drawn point
        if (this.mouseX > x - MARK_SIZE && this.mouseX < x + MARK_SIZE && this.mouseY > y - MARK_SIZE
            && this.mouseY < y + MARK_SIZE) {
          toolTipX = x;
          toolTipY = y;
          toolTipName = emitter.getName();
        }
      }
    }

    // If the mouse is hovering over a point, display a tooltip with information
    if (toolTipName != null) {
      writeToolTipLabel(toolTipName, toolTipX, toolTipY);
    }
  }

  // Maps an emission value to a pixel value in the graph's y-axis range
  private int mapEmissions(double emissions, double min, double max) {
    return (int) map((float) emissions, (float) min, (float) max, this.height - MARGIN_TB - 5, MARGIN_TB);
  }

  // Draws the vertical axis representing emissions in kilo-tons, with values
  // going from `min` to `max`.
  private void writeEmissionsAxis(double min, double max) {
    textSize(12);
    fill(0);
    stroke(0);
    textAlign(RIGHT, CENTER);

    int stepSize = (int) getTickSize(max, min);

    for (double em = min; em <= max; em = em + stepSize) {
      text((int) em, MARGIN_LR - 5, mapEmissions(em, min, max));
    }
    line(MARGIN_LR, this.height - MARGIN_TB, MARGIN_LR, MARGIN_TB);

    pushMatrix();
    translate(MARGIN_LR / 6, (this.height / 2));
    rotate(radians(270));
    textAlign(CENTER, CENTER);
    textSize(14);
    text("Greenhouse gas emissions in equivalent kilo-tons of CO2", 0, 0);
    popMatrix();
  }

  // Maps a year value to a pixel value in the graph's x-axis range
  private int mapYear(int year) {
    return (int) map(year, 1970, 2012, MARGIN_LR + 5, this.width - 10);
  }

  // Draws the horizontal axis representing years, going from 1970 to 2012.
  private void writeYearsAxis() {
    textSize(12);
    fill(0);
    stroke(0);
    textAlign(RIGHT, CENTER);
    for (int year = 1970; year <= 2012; year++) {
      pushMatrix();
      float angle = radians(270);
      translate(mapYear(year), this.height - MARGIN_TB + 5);
      rotate(angle);
      text(year, 0, 0);
      popMatrix();
    }
    line(MARGIN_LR, this.height - MARGIN_TB, this.width - 10, this.height - MARGIN_TB);
    textAlign(CENTER, CENTER);
    textSize(14);
    text("Year", this.width / 2, this.height - (MARGIN_TB / 2));
  }

  // Writes the plot's title
  private void writePlotTitle(String title) {
    fill(21, 71, 52); // Font colour Poly Green
    textAlign(CENTER, CENTER);
    textSize(16);
    text(title, this.width / 2, MARGIN_TB / 2);
  }

  private void writeToolTipLabel(String text, int x, int y) {
    textSize(12);
    int textWidth = (int) textWidth(text);
    int textHeight = 20; // approximate
    fill(0);
    if (x + textWidth + 4 > width) {
      int diff = (x + textWidth + 4) - width;
      x = x - diff;
    }

    if (y + textHeight > height) {
      int diff = (y + textHeight) - height;
      y = y - diff;
    }
    stroke(0);
    fill(255);
    rect(x - 2, y - 2, textWidth + 4, textHeight);
    fill(0);
    textAlign(LEFT, TOP);
    text(text, x, y);
  }

  // Finds an aesthetic tick step size between max and min
  // Source: https://stackoverflow.com/a/326746
  private double getTickSize(double max, double min) {
    double range = max - min;
    int tickCount = 20;
    double unroundedTickSize = range / (tickCount - 1);
    double x = Math.ceil(Math.log10(unroundedTickSize) - 1);
    double pow10x = Math.pow(10, x);
    double roundedTickRange = Math.ceil(unroundedTickSize / pow10x) * pow10x;
    return roundedTickRange;
  }
}
