package equality;

import java.time.LocalTime;
import java.util.Objects;

public class CourseSection {
  private final String prefix;
  private final String number;
  private final int enrollment;
  private final LocalTime startTime;
  private final LocalTime endTime;

  public CourseSection(final String prefix, final String number, final int enrollment, final LocalTime startTime,
      final LocalTime endTime) {
    this.prefix = prefix;
    this.number = number;
    this.enrollment = enrollment;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  // additional likely methods not defined since they are not needed for testing
  public String getPrefix() {
    return this.prefix;
  }

  public String getNumber() {
    return this.number;
  }

  public int getEnrollment() {
    return this.enrollment;
  }

  public LocalTime getStartTime() {
    return this.startTime;
  }

  public LocalTime getEndTime() {
    return this.endTime;
  }

  public int hashCode() {
    int hash = 0;
    hash = 31 * hash + prefix.hashCode();
    hash = 31 * hash + number.hashCode();
    hash = 31 * hash + enrollment;
    hash = 31 * hash + startTime.hashCode();
    hash = 31 * hash + endTime.hashCode();
    return hash;
  }

  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!this.getClass().equals(obj.getClass()))
      return false;

    CourseSection course = (CourseSection) obj;
    return Objects.equals(this.prefix, course.getPrefix()) && Objects.equals(this.number, course.getNumber())
        && this.enrollment == course.getEnrollment() && this.startTime.equals(course.getStartTime())
        && this.endTime.equals(course.getEndTime());
  }
}
