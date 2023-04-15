package part1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import part2.Applicant;

public class TestCases {
  private final static double DELTA = 0.0001;

  ////////////////////////////////////////////////////////////
  // SimpleIf Tests //
  ////////////////////////////////////////////////////////////

  @Test
  public void testAnalyzeApplicant() {
    assertTrue(SimpleIf.analyzeApplicant(89, 85));
  }

  @Test
  public void testAnalyzeApplicant2() {
    List<CourseGrade> grades1 = Arrays.asList(new CourseGrade("Intro to CS", 100),
        new CourseGrade("Data Structures", 95), new CourseGrade("Algorithms", 91),
        new CourseGrade("Computer Organization", 91), new CourseGrade("Operating Systems", 75),
        new CourseGrade("Non-CS", 83));
    Applicant testApplicant1 = new Applicant("Hank Schrader", grades1, 5, "computer science");
    List<CourseGrade> grades2 = Arrays.asList(new CourseGrade("Intro to CS", 100),
        new CourseGrade("Data Structures", 95), new CourseGrade("Algorithms", 91),
        new CourseGrade("Computer Organization", 91), new CourseGrade("Operating Systems", 69),
        new CourseGrade("Non-CS", 83));
    Applicant testApplicant2 = new Applicant("Walter White", grades2, 0, "computer science");
    assertEquals(SimpleIf.analyzeApplicant2(testApplicant1), true);
    assertEquals(SimpleIf.analyzeApplicant2(testApplicant2), false);
  }

  @Test
  public void testAnalyzeApplicant3() {
    assertFalse(SimpleIf.analyzeApplicant(92, 99));
  }

  @Test
  public void testAnalyzeApplicant4() {
    assertTrue(SimpleIf.analyzeApplicant(103, 99));
  }

  @Test
  public void testMaxAverage() {
    assertEquals(SimpleIf.maxAverage(89.5, 91.2), 91.2, DELTA);
  }

  @Test
  public void testMaxAverage2() {
    assertEquals(SimpleIf.maxAverage(60, 89), 89, DELTA);
  }

  @Test
  public void testMaxAverage3() {
    assertEquals(SimpleIf.maxAverage(89.9, 90), 90, DELTA);
    assertEquals(SimpleIf.maxAverage(75.0, 71), 75.0, DELTA);
  }

  ////////////////////////////////////////////////////////////
  // SimpleLoop Tests //
  ////////////////////////////////////////////////////////////

  @Test
  public void testSimpleLoop1() {
    assertEquals(7, SimpleLoop.sum(3, 4));
  }

  @Test
  public void testSimpleLoop2() {
    assertEquals(7, SimpleLoop.sum(-2, 4));
  }

  @Test
  public void testSimpleLoop3() {
    assertEquals(18, SimpleLoop.sum(5, 7));
    assertEquals(10, SimpleLoop.sum(1, 4));
  }

  ////////////////////////////////////////////////////////////
  // SimpleArray Tests //
  ////////////////////////////////////////////////////////////

  @Test
  public void testSimpleArray1() {
    /*
     * What is that parameter? They are newly allocated arrays with initial
     * values.
     */
    assertArrayEquals(new boolean[] { false, false, true, true, false, false },
        SimpleArray.applicantAcceptable(new int[] { 80, 85, 89, 92, 76, 81 }, 85));
  }

  @Test
  public void testSimpleArray2() {
    assertArrayEquals(new boolean[] { false, false }, SimpleArray.applicantAcceptable(new int[] { 80, 83 }, 92));
  }

  @Test
  public void testSimpleArray3() {
    assertArrayEquals(new boolean[] { true, true, true, true, false, false },
        SimpleArray.applicantAcceptable(new int[] { 100, 98, 97, 99, 90, 91 }, 95));
    assertArrayEquals(new boolean[] { false, false, false, true, true, false },
        SimpleArray.applicantAcceptable(new int[] { 82, 83, 84, 85, 90, 75 }, 84));
  }

  ////////////////////////////////////////////////////////////
  // SimpleList Tests //
  ////////////////////////////////////////////////////////////

  @Test
  public void testSimpleList1() {
    List<Integer> input = new LinkedList<>(Arrays.asList(80, 85, 89, 92, 76, 81));
    List<Boolean> expected = new ArrayList<>(Arrays.asList(false, false, true, true, false, false));

    assertEquals(expected, SimpleList.applicantAcceptable(input, 85));
  }

  @Test
  public void testSimpleList2() {
    List<Boolean> expected1 = Arrays.asList(false, false, true, true, false, false);
    List<Boolean> expected2 = Arrays.asList(false, true, true, true, false, true);
    List<Integer> input = Arrays.asList(80, 85, 89, 92, 70, 81);
    assertEquals(expected1, SimpleList.applicantAcceptable(input, 88));
    assertEquals(expected2, SimpleList.applicantAcceptable(input, 80));
  }

  ////////////////////////////////////////////////////////////
  // BetterLoop Tests //
  ////////////////////////////////////////////////////////////

  @Test
  public void testFourOver85() {
    assertFalse(BetterLoop.atLeastFourOver85(new int[] { 89, 93, 100, 39, 84, 63 }));
  }

  @Test
  public void testFourOver85_2() {
    assertTrue(BetterLoop.atLeastFourOver85(new int[] { 89, 86, 90, 92, 84, 88 }));
  }

  @Test
  public void testFourOver85_3() {
    assertTrue(BetterLoop.atLeastFourOver85(new int[] { 90, 95, 93, 99, 80, 81 }));
    assertFalse(BetterLoop.atLeastFourOver85(new int[] { 85, 80, 85, 75, 80, 92 }));
  }

  ////////////////////////////////////////////////////////////
  // ExampleMap Tests //
  ////////////////////////////////////////////////////////////

  @Test
  public void testExampleMap1() {
    Map<String, List<CourseGrade>> courseListsByStudent = new HashMap<>();
    courseListsByStudent.put("Julie", Arrays.asList(new CourseGrade("CPE 123", 89), new CourseGrade("CPE 101", 90),
        new CourseGrade("CPE 202", 99), new CourseGrade("CPE 203", 100), new CourseGrade("CPE 225", 89)));
    courseListsByStudent.put("Paul", Arrays.asList(new CourseGrade("CPE 101", 86), new CourseGrade("CPE 202", 80),
        new CourseGrade("CPE 203", 76), new CourseGrade("CPE 225", 80)));
    courseListsByStudent.put("Zoe",
        Arrays.asList(new CourseGrade("CPE 123", 99), new CourseGrade("CPE 203", 91), new CourseGrade("CPE 471", 86),
            new CourseGrade("CPE 473", 90), new CourseGrade("CPE 476", 99), new CourseGrade("CPE 572", 100)));

    List<String> expected = Arrays.asList("Julie", "Zoe");

    /*
     * Why compare HashSets here? Just so that the order of the elements in the
     * list is not important for this test.
     */
    assertEquals(new HashSet<>(expected), new HashSet<>(ExampleMap.highScoringStudents(courseListsByStudent, 85)));
  }

  @Test
  public void testExampleMap2() {
    Map<String, List<CourseGrade>> courseListsByStudent = new HashMap<>();
    courseListsByStudent.put("Julie", Arrays.asList(new CourseGrade("CPE 123", 90), new CourseGrade("CPE 101", 91),
        new CourseGrade("CPE 202", 92), new CourseGrade("CPE 203", 100), new CourseGrade("CPE 225", 89)));
    courseListsByStudent.put("Paul", Arrays.asList(new CourseGrade("CPE 101", 87), new CourseGrade("CPE 202", 90),
        new CourseGrade("CPE 203", 91), new CourseGrade("CPE 225", 100)));
    courseListsByStudent.put("Zoe",
        Arrays.asList(new CourseGrade("CPE 123", 100), new CourseGrade("CPE 203", 92), new CourseGrade("CPE 471", 85),
            new CourseGrade("CPE 473", 100), new CourseGrade("CPE 476", 90), new CourseGrade("CPE 572", 91)));

    List<String> expected = Arrays.asList("Julie", "Paul");

    /*
     * Why compare HashSets here? Just so that the order of the elements in the
     * list is not important for this test.
     */
    assertEquals(new HashSet<>(expected), new HashSet<>(ExampleMap.highScoringStudents(courseListsByStudent, 86)));
    Map<String, List<CourseGrade>> courseListsByStudent2 = new HashMap<>();
    courseListsByStudent2.put("Peter",
        Arrays.asList(new CourseGrade("EE 101", 73), new CourseGrade("CPE 101", 78), new CourseGrade("CPE 202", 75)));

    courseListsByStudent2.put("Mark", Arrays.asList(new CourseGrade("CPE 101", 78), new CourseGrade("CPE 202", 98),
        new CourseGrade("CPE 203", 86), new CourseGrade("CPE 225", 87)));
    courseListsByStudent2.put("Nick",
        Arrays.asList(new CourseGrade("CSC 123", 63), new CourseGrade("CSC 101", 64), new CourseGrade("CSC 202", 67)));

    List<String> expected2 = Arrays.asList("Peter", "Mark");
    assertEquals(new HashSet<>(expected2), new HashSet<>(ExampleMap.highScoringStudents(courseListsByStudent2, 69)));

  }
}
