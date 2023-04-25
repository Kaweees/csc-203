import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The database of country and sector emissions. Do not modify this file, but
 * feel free to read through the code. It mostly uses constructs that we have
 * talked about in class. TODO: There will be changes required in this class.
 * See the specification document for details.
 */
public class EmissionsDatabase {

  private List<GreenhouseGasEmitter> sectors;
  private double sectorMinEmission = -1;
  private double sectorMaxEmission = -1;

  private List<GreenhouseGasEmitter> countries;
  private double countryMinEmission = -1;
  private double countryMaxEmission = -1;

  public EmissionsDatabase() {
    this.loadSectors();
    this.loadCountries();
  }

  /**
   * @return The minimum Country emission value (across all years)
   */
  public double getCountryMinEmission() {
    return this.countryMinEmission;
  }

  /**
   * @return The maximum Country emission value (across all years)
   */
  public double getCountryMaxEmission() {
    return this.countryMaxEmission;
  }

  /**
   * @return The minimum Sector emission value (across all years)
   */
  public double getSectorMinEmission() {
    return this.sectorMinEmission;
  }

  /**
   * @return The maximum Sector emission value (across all years)
   */
  public double getSectorMaxEmission() {
    return this.sectorMaxEmission;
  }

  /**
   * Obtains the list of Countries, reading it from the file first if it hasn't
   * been read yet.
   *
   * @return A list of Country objects
   */
  public List<GreenhouseGasEmitter> getCountries() {
    if (this.countries == null) {
      this.loadCountries();
    }
    return countries;
  }

  /**
   * Obtains the list of Sectors, reading it from the file first if it hasn't
   * been read yet.
   * 
   * @return A list of Sector objects
   */
  public List<GreenhouseGasEmitter> getSectors() {
    if (this.sectors == null) {
      this.loadSectors();
    }
    return this.sectors;
  }

  // Loads the countries from the countries.csv file, and computes the country
  // min and max while doing so.
  private void loadCountries() {
    File dataFile = new File("src/countries.csv");
    Map<String, Map<Integer, Emission>> emissions = new HashMap<>();

    this.countryMaxEmission = Double.MIN_VALUE;
    this.countryMinEmission = Double.MAX_VALUE;

    try {
      Scanner scan = new Scanner(dataFile);
      scan.nextLine(); // Skip the header line
      while (scan.hasNextLine()) {
        String[] data = scan.nextLine().split(",");

        // Each line contains Country, Year, CO2, N20, CH4 --- in that order
        String name = data[0];
        int year = Integer.parseInt(data[1]);
        double co2emissions = Double.parseDouble(data[2]);
        double n2oemissions = Double.parseDouble(data[3]);
        double ch4emissions = Double.parseDouble(data[4]);

        double total = co2emissions + n2oemissions + ch4emissions;
        if (total > this.countryMaxEmission) {
          this.countryMaxEmission = total;
        }

        if (total < this.countryMinEmission) {
          this.countryMinEmission = total;
        }

        Emission emission = new Emission(co2emissions, n2oemissions, ch4emissions);

        if (!emissions.containsKey(name)) {
          emissions.put(name, new HashMap<>());
        }
        emissions.get(name).put(year, emission);
      }
      scan.close();

      // Process emissions into a List of Countries
      this.countries = new ArrayList<>();
      for (Map.Entry<String, Map<Integer, Emission>> entry : emissions.entrySet()) {
        Country country = new Country(entry.getKey(), entry.getValue());
        this.countries.add(country);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  // Loads the sectors from the sectors.csv file, and computes the sector min
  // and max while doing so.
  private void loadSectors() {
    this.sectorMaxEmission = Double.MIN_VALUE;
    this.sectorMinEmission = Double.MAX_VALUE;

    File dataFile = new File("src/sectors.csv");
    Map<String, Map<Integer, Double>> tempMap = new HashMap<>();
    try {
      Scanner scan = new Scanner(dataFile);
      scan.nextLine(); // Skip the header line
      while (scan.hasNextLine()) {
        String[] data = scan.nextLine().split(",");

        // Each line contains Sector, Year, Emissions --- in that order
        String name = data[0].split("\\.")[2]; // Sector names are
                                               // "Emissions.Sector.X" — we only
                                               // want "X"
        int year = Integer.parseInt(data[1]);
        double greenhouseGasEmissions = Double.parseDouble(data[2]);

        // Update max and min emissions
        if (greenhouseGasEmissions > this.sectorMaxEmission) {
          this.sectorMaxEmission = greenhouseGasEmissions;
        }

        if (greenhouseGasEmissions < this.sectorMinEmission) {
          this.sectorMinEmission = greenhouseGasEmissions;
        }

        if (!tempMap.containsKey(name)) {
          tempMap.put(name, new HashMap<>());
        }
        tempMap.get(name).put(year, greenhouseGasEmissions);
      }
      scan.close();

      // Process tempMap into a List of Countries
      this.sectors = new LinkedList<>();
      for (Map.Entry<String, Map<Integer, Double>> entry : tempMap.entrySet()) {
        Sector sector = new Sector(entry.getKey(), entry.getValue());
        this.sectors.add(sector);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
