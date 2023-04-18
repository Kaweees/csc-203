package part1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.w3c.dom.css.Counter;

/**
 * THIS CLASS WON'T COMPILE UNTIL YOU CREATE YOUR COUNTRY AND SECTOR CLASSES
 */
public class PartOneTestCases {

  /**
   * Tests the implementation of the Emission class. TO-DO: Examine this test
   * case to know what you should name your public methods.
   *
   * @throws NoSuchMethodException
   */
  @Test
  public void testEmissionImplSpecifics() throws NoSuchMethodException {
    final List<String> expectedMethodNames = Arrays.asList("getCO2", "getN2O", "getCH4");

    final List<Class> expectedMethodReturns = Arrays.asList(double.class, double.class, double.class);

    final List<Class[]> expectedMethodParameters = Arrays.asList(new Class[0], new Class[0], new Class[0]);

    verifyImplSpecifics(Emission.class, expectedMethodNames, expectedMethodReturns, expectedMethodParameters);
  }

  /**
   * Tests the implementation of the Country class. TO-DO: Examine this test
   * case to know what you should name your public methods.
   *
   * @throws NoSuchMethodException
   */
  @Test
  public void testCountryImplSpecifics() throws NoSuchMethodException {
    final List<String> expectedMethodNames = Arrays.asList("getName", "getEmissions");

    final List<Class> expectedMethodReturns = Arrays.asList(String.class, Map.class);

    final List<Class[]> expectedMethodParameters = Arrays.asList(new Class[0], new Class[0]);

    verifyImplSpecifics(Country.class, expectedMethodNames, expectedMethodReturns, expectedMethodParameters);
  }

  /**
   * Tests the part1 implementation of the Sector class. TO-DO: Examine this
   * test to know what you should name your public methods.
   *
   * @throws NoSuchMethodException
   */
  @Test
  public void testSectorImplSpecifics() throws NoSuchMethodException {
    final List<String> expectedMethodNames = Arrays.asList("getName", "getEmissions");

    final List<Class> expectedMethodReturns = Arrays.asList(String.class, Map.class);

    final List<Class[]> expectedMethodParameters = Arrays.asList(new Class[0], new Class[0]);

    verifyImplSpecifics(Sector.class, expectedMethodNames, expectedMethodReturns, expectedMethodParameters);
  }

  private static void verifyImplSpecifics(final Class<?> clazz, final List<String> expectedMethodNames,
      final List<Class> expectedMethodReturns, final List<Class[]> expectedMethodParameters)
      throws NoSuchMethodException {
    assertEquals(0, clazz.getFields().length, "Unexpected number of public fields");

    final List<Method> publicMethods = Arrays.stream(clazz.getDeclaredMethods())
        .filter(m -> Modifier.isPublic(m.getModifiers())).collect(Collectors.toList());

    assertEquals(expectedMethodNames.size(), publicMethods.size(), "Unexpected number of public methods");

    assertTrue(expectedMethodNames.size() == expectedMethodReturns.size(), "Invalid test configuration");
    assertTrue(expectedMethodNames.size() == expectedMethodParameters.size(), "Invalid test configuration");

    for (int i = 0; i < expectedMethodNames.size(); i++) {
      Method method = clazz.getDeclaredMethod(expectedMethodNames.get(i), expectedMethodParameters.get(i));
      assertEquals(expectedMethodReturns.get(i), method.getReturnType());
    }
  }

  @Test
  public void testEmission() {
    Emission emission = new Emission(1.0, 2.0, 3.0);
    assertEquals(1.0, emission.getCO2());
    assertEquals(2.0, emission.getN2O());
    assertEquals(3.0, emission.getCH4());
  }

  @Test
  public void testCountry() {
    Map<Integer, Emission> emissions = new HashMap<>();
    emissions.put(2010, new Emission(1.0, 2.0, 3.0));
    emissions.put(2011, new Emission(4.0, 5.0, 6.0));
    Country country = new Country("Canada", emissions);
    assertEquals("Canada", country.getName());
    assertEquals(2, country.getEmissions().size());
    assertEquals(emissions, country.getEmissions());
  }

  @Test
  public void testSector() {
    Map<Integer, Double> emissions = new HashMap<>();
    emissions.put(2010, 1.0);
    emissions.put(2011, 2.0);
    Sector sector = new Sector("Agriculture", emissions);
    assertEquals("Agriculture", sector.getName());
    assertEquals(2, sector.getEmissions().size());
    assertEquals(emissions, sector.getEmissions());
  }

  @Test
  public void testGetYearWithHighestEmissions() {
    Map<Integer, Emission> countryEmissions = new HashMap<>();
    countryEmissions.put(2010, new Emission(1.0, 2.0, 3.0));
    countryEmissions.put(2011, new Emission(4.0, 5.0, 6.0));
    Country country = new Country("Canada", countryEmissions);
    assertEquals(2011, Util.getYearWithHighestEmissions(country));

    Map<Integer, Double> sectorEmissions = new HashMap<>();
    sectorEmissions.put(2010, 1.0);
    sectorEmissions.put(2011, 2.0);
    Sector sector = new Sector("Agriculture", sectorEmissions);
    assertEquals(2011, Util.getYearWithHighestEmissions(sector));
  }
}