package part2;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import part1.CourseGrade;

public class TestCases {
  /*
   * This test is just to get you started.
   */
  @Test
  public void testGetName() {
    List<CourseGrade> grades = Arrays.asList(new CourseGrade("Intro to CS", 100),
        new CourseGrade("Data Structures", 95), new CourseGrade("Algorithms", 91),
        new CourseGrade("Computer Organization", 91), new CourseGrade("Operating Systems", 75),
        new CourseGrade("Non-CS", 83));
    Applicant testApplicant = new Applicant("Aakash", grades, 5, "computer science");
    List<CourseGrade> grades2 = Arrays.asList(new CourseGrade("CPE 123", 90), new CourseGrade("CPE 101", 91),
        new CourseGrade("CPE 202", 92), new CourseGrade("CPE 203", 100));
    Applicant testApplicant2 = new Applicant("Peter", grades2, 5, "computer engineering");
    assertEquals("Peter", testApplicant2.getName());
    assert (!testApplicant.getName().equals(testApplicant2.getName()));
  }

  @Test
  public void testGetGrades() {
    List<CourseGrade> grades = Arrays.asList(new CourseGrade("Intro to CS", 100),
        new CourseGrade("Data Structures", 95), new CourseGrade("Algorithms", 91),
        new CourseGrade("Computer Organization", 91), new CourseGrade("Operating Systems", 75),
        new CourseGrade("Non-CS", 83));
    Applicant testApplicant = new Applicant("Aakash", grades, 5, "computer science");
    List<CourseGrade> grades2 = Arrays.asList(new CourseGrade("CPE 123", 90), new CourseGrade("CPE 101", 91),
        new CourseGrade("CPE 202", 92), new CourseGrade("CPE 203", 100));
    Applicant testApplicant2 = new Applicant("Peter", grades2, 5, "computer engineering");
    assertEquals(grades, testApplicant.getGrades());
    assertEquals(grades2, testApplicant2.getGrades());
    assertNotSame(testApplicant.getGrades(), testApplicant2.getGrades());
  }

  @Test
  public void testGetGradeFor() {
    List<CourseGrade> grades = Arrays.asList(new CourseGrade("Intro to CS", 100),
        new CourseGrade("Data Structures", 95), new CourseGrade("Algorithms", 91),
        new CourseGrade("Computer Organization", 91), new CourseGrade("Operating Systems", 75),
        new CourseGrade("Non-CS", 83));
    Applicant testApplicant = new Applicant("Aakash", grades, 5, "computer science");
    List<CourseGrade> grades2 = Arrays.asList(new CourseGrade("CPE 123", 90), new CourseGrade("CPE 101", 91),
        new CourseGrade("CPE 202", 92), new CourseGrade("CPE 203", 100));
    Applicant testApplicant2 = new Applicant("Peter", grades2, 5, "computer engineering");
    assertEquals(95, testApplicant.getGradeFor("Data Structures").getScore());
    assertEquals(90, testApplicant2.getGradeFor("CPE 123").getScore());
    assertEquals(91, testApplicant2.getGradeFor("CPE 101").getScore());
  }

  @Test
  public void testGetExpYrs() {
    List<CourseGrade> grades = Arrays.asList(new CourseGrade("Intro to CS", 100),
        new CourseGrade("Data Structures", 95), new CourseGrade("Algorithms", 91),
        new CourseGrade("Computer Organization", 91), new CourseGrade("Operating Systems", 75),
        new CourseGrade("Non-CS", 83));
    Applicant testApplicant = new Applicant("Aakash", grades, 5, "computer science");
    List<CourseGrade> grades2 = Arrays.asList(new CourseGrade("CPE 123", 90), new CourseGrade("CPE 101", 91),
        new CourseGrade("CPE 202", 92), new CourseGrade("CPE 203", 100));
    Applicant testApplicant2 = new Applicant("Peter", grades2, 10, "computer engineering");
    assertEquals(5, testApplicant.getExpYrs());
    assertEquals(10, testApplicant2.getExpYrs());
    assertNotSame(testApplicant.getExpYrs(), testApplicant2.getExpYrs());
  }

  /*
   * The tests below here are to verify the basic requirements regarding the
   * "design" of your class. These are to remain unchanged.
   */
  @Test
  public void testImplSpecifics() throws NoSuchMethodException {
    final List<String> expectedMethodNames = Arrays.asList("getName", "getGrades", "getGradeFor");

    final List<Class> expectedMethodReturns = Arrays.asList(String.class, List.class, CourseGrade.class);

    final List<Class[]> expectedMethodParameters = Arrays.asList(new Class[0], new Class[0],
        new Class[] { String.class });

    verifyImplSpecifics(Applicant.class, expectedMethodNames, expectedMethodReturns, expectedMethodParameters);
  }

  private static void verifyImplSpecifics(final Class<?> clazz, final List<String> expectedMethodNames,
      final List<Class> expectedMethodReturns, final List<Class[]> expectedMethodParameters)
      throws NoSuchMethodException {
    assertEquals(0, Applicant.class.getFields().length, "Unexpected number of public fields");

    final List<Method> publicMethods = Arrays.stream(clazz.getDeclaredMethods())
        .filter(m -> Modifier.isPublic(m.getModifiers())).collect(Collectors.toList());

    assertTrue(expectedMethodNames.size() + 1 >= publicMethods.size(), "Unexpected number of public methods");

    assertTrue(expectedMethodNames.size() == expectedMethodReturns.size(), "Invalid test configuration");
    assertTrue(expectedMethodNames.size() == expectedMethodParameters.size(), "Invalid test configuration");

    for (int i = 0; i < expectedMethodNames.size(); i++) {
      Method method = clazz.getDeclaredMethod(expectedMethodNames.get(i), expectedMethodParameters.get(i));
      assertEquals(expectedMethodReturns.get(i), method.getReturnType());
    }
  }
}
