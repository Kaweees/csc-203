import java.util.List;
import java.util.Map;

public class Country implements GreenhouseGasEmitter {
  private final String name;
  private final Map<Integer, Emission> emissions;

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

  public static Country countryWithHighestCH4InYear(List<Country> countries, int year) {
    double highEmi = 0;
    Country highCount = null;
    for (Country cont : countries) {
      double emi = cont.getEmissions().get(year).getCH4();
      if (emi > highEmi) {
        highEmi = emi;
        highCount = cont;
      }
    }
    assert highCount != null;
    System.out.println(highCount.getName());
    System.out.println(highEmi);
    return highCount;
  }

  public static Country countryWithHighestChangeInEmissions(List<Country> countries, int startYear, int endYear) {
    double change = 0;
    Country changeCont = null;
    for (Country cont : countries) {
      Map<Integer, Emission> emi = cont.getEmissions();
      double emiStart = emi.get(startYear).getCO2() + emi.get(startYear).getN2O() + emi.get(startYear).getCH4();
      double emiEnd = emi.get(endYear).getCO2() + emi.get(endYear).getN2O() + emi.get(endYear).getCH4();
      double delta = (emiEnd - emiStart);
      if (delta > change) {
        change = delta;
        changeCont = cont;
      }
    }
    assert changeCont != null;
    System.out.println(changeCont.getName());
    System.out.println(change);
    return changeCont;
  }

  public double getEmissionsInYear(int year) {
    Emission emi = this.emissions.get(year);
    return emi.getCO2() + emi.getN2O() + emi.getCH4();
  }
}