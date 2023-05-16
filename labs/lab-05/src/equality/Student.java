package equality;

import java.util.List;
import java.util.Objects;

public class Student {
  private final String surname;
  private final String givenName;
  private final int age;
  private final List<CourseSection> currentCourses;

  public Student(final String surname, final String givenName, final int age,
      final List<CourseSection> currentCourses) {
    this.surname = surname;
    this.givenName = givenName;
    this.age = age;
    this.currentCourses = currentCourses;
  }

  public String getSurname() {
    return surname;
  }

  public String getGivenName() {
    return givenName;
  }

  public int getAge() {
    return age;
  }

  public List<CourseSection> getCurrentCourses() {
    return currentCourses;
  }

  public int hashCode() {
    return Objects.hash(this.surname, this.givenName, age, this.currentCourses);
  }
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!this.getClass().equals(obj.getClass()))
      return false;

    Student student = (Student) obj;
    return Objects.equals(this.surname, student.getSurname()) && Objects.equals(this.givenName, student.getGivenName())
        && this.age == student.getAge() && this.currentCourses.equals(student.currentCourses);
  }
}
