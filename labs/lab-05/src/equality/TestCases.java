package equality;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestCases {
  @Test
  public void testCourseSectionEquals() {
    CourseSection course1 = new CourseSection("CSC", "202", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    CourseSection course2 = new CourseSection("CSC", "202", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    assertEquals(course1, course2);
  }

  @Test
  public void testCourseSectionNotEquals() {
    CourseSection course1 = new CourseSection("CSC", "202", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    CourseSection course2 = new CourseSection("CSC", "225", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    assertNotEquals(course1, course2);
  }

  @Test
  public void testCourseSectionHashEquals() {
    CourseSection course1 = new CourseSection("CSC", "202", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    CourseSection course2 = new CourseSection("CSC", "202", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    assertEquals(course1.hashCode(), course2.hashCode());
  }

  @Test
  public void testCourseSectionHashNotEquals() {
    CourseSection course1 = new CourseSection("CSC", "202", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    CourseSection course2 = new CourseSection("CSC", "225", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    assertNotEquals(course1.hashCode(), course2.hashCode());
  }

  @Test
  public void testStudent() {
    CourseSection course1 = new CourseSection("CSC", "202", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    CourseSection course2 = new CourseSection("CSC", "225", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));
    CourseSection course3 = new CourseSection("CSC", "203", 29, LocalTime.of(4, 25), LocalTime.of(10, 10));

    List<CourseSection> courseList1 = new ArrayList<>();
    courseList1.add(course3);
    courseList1.add(course1);
    courseList1.add(course2);

    List<CourseSection> courseList2 = new ArrayList<>();
    courseList2.add(course2);
    courseList2.add(course3);

    Student student1 = new Student("Parkinson", "David", 19, courseList1);
    Student student2 = new Student("Mealy", "James", 21, courseList1);
    Student student3 = new Student("Planck", "John", 22, courseList2);

    assertNotEquals(student1, student3);
    assertNotEquals(student2.hashCode(), student3.hashCode());
    assertEquals(student1, student1);
    assertNotEquals(null, student1);
    assertNotEquals(student1, student2);
  }
}
