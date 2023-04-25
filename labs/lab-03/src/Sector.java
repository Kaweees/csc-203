import java.util.List;
import java.util.Map;

public class Sector implements GreenhouseGasEmitter {
  private final String name;
  private final Map<Integer, Double> emissions;

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
      for (int i = startYear; i < endYear + 1; i++) {
        sum += sect.getEmissions().get(i);
      }
      double avg = (sum) / (endYear - startYear + 1);
      if (avg > finalAvg) {
        finalAvg = avg;
        avgSect = sect;
      }
    }
    assert avgSect != null;
    System.out.println(avgSect.getName());
    System.out.println(finalAvg);
    return avgSect;
  }

  public double getEmissionsInYear(int year) {
    return this.emissions.get(year);
  }
}
