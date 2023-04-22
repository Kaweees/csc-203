package part3;

import java.util.HashMap;
import java.util.List;
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

  public static Sector sectorWithBiggestChangeInEmissions(List<Sector> sectors, int startYear, int endYear) {
    double finalAvg = -1;
    Sector avgSect = null;
    for (Sector sect : sectors) {
      double sum = 0;
      for (int i = startYear; i < endYear+1; i++) {
        sum += sect.getEmissions().get(i);
      }
      double avg = (sum) / (endYear - startYear+1);
      if (avg > finalAvg) {
        finalAvg = avg;
        avgSect = sect;
      }
    }
    System.out.println(avgSect.getName());
    System.out.println(finalAvg);
    return avgSect;
  }
}
