package part2;

import java.util.HashMap;
import java.util.Map;

public class Country {
  private String name;
  private Map<Integer, Emission> emissions = new HashMap<>();

  public Country(String name, Map<Integer, Emission> emissions) {
    this.name = name;
    this.emissions = emissions;
  }

  public String getName() {
    return this.name;
  }

  public Map<Integer, Emission> getEmissions() {
    return this.emissions;
  }

  public int getYearWithHighestEmissions() {
    Map<Integer, Emission> map = getEmissions();
    double emissionHigh = 0;
    int year = 0;
    for (Map.Entry<Integer, Emission> emission : map.entrySet()) {
      Emission emi = emission.getValue();
      double total = emi.getCO2() + emi.getN2O() + emi.getCH4();
      if (emissionHigh == 0) {
        emissionHigh = total;
        year = emission.getKey();
      } else {
        if (total > emissionHigh) {
          emissionHigh = total;
          year = emission.getKey();
        }
      }
    }
    return year;
  }

  
}
