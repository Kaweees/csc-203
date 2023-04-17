package part1;

import java.util.Map;

public class Util {
  public static int getYearWithHighestEmissions(Country country) {
    int year = 0;
    double max = 0;
    for (Map.Entry<Integer, Emission> entry : country.getEmissions().entrySet()) {
      if (entry.getValue().getCO2() > max) {
        max = entry.getValue().getCO2();
        year = entry.getKey();
      }
    }
    return year;
  }

  public int getYearWithHighestEmissions(Sector sector) {
    int year = 0;
    double max = 0;
    for (Map.Entry<Integer, Double> entry : sector.getEmissions().entrySet()) {
      if (entry.getValue() > max) {
        max = entry.getValue();
        year = entry.getKey();
      }
    }
    return year;
  }
}