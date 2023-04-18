package part1;

import java.util.Map;

public class Util {
  public static int getYearWithHighestEmissions(Country country) {
    Map<Integer, Emission> map = country.getEmissions();
    int year = 0;
    double emissionTotal = 0;
    for (Map.Entry<Integer, Emission> entry : country.getEmissions().entrySet()) {
      Emission emission = entry.getValue();
      double total = emission.getCH4() + emission.getCO2() + emission.getN2O();
      if (emissionTotal == 0) {
        emissionTotal = total;
        year = entry.getKey();
      } else if (total > emissionTotal) {
        emissionTotal = total;
        year = entry.getKey();
      }
    }
    return year;
  }

  public static int getYearWithHighestEmissions(Sector sector) {
    Map<Integer, Double> map = sector.getEmissions();
    int year = 0;
    double emissionHigh = 0;
    for (Map.Entry<Integer, Double> emission : map.entrySet()) {
      double total = emission.getValue();
      if (emissionHigh == 0) {
        emissionHigh = total;
        year = emission.getKey();
      } else if (total > emissionHigh) {
        emissionHigh = total;
        year = emission.getKey();
      }
    }
    return year;
  }
}