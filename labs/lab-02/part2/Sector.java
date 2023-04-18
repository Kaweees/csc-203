package part2;

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

  public int getYearWithHighestEmissions() {
    Map<Integer, Double> map = getEmissions();
    int year = 0;
    double emissionTotal = 0;
    for (Map.Entry<Integer, Double> emission : map.entrySet()) {
      double total = emission.getValue();
      if (emissionTotal == 0) {
        emissionTotal = total;
        year = emission.getKey();
      } else if (total > emissionTotal) {
        emissionTotal = total;
        year = emission.getKey();
      }
    }
    return year;
  }
}
