package part1;

import java.util.HashMap;
import java.util.Map;

public class Sector {
  private String name;
  private Map<Integer, Double> emissions = new HashMap<>();

  public Sector(String name, Map<Integer, Double> emissions) {
    this.name = name;
    this.emissions = emissions;
  }

  public String getName() {
    return this.name;
  }

  public Map<Integer, Double> getEmissions() {
    return this.emissions;
  }

}
