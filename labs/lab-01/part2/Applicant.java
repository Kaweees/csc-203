package part2;

import java.util.List;

import part1.CourseGrade;

public class Applicant {
  private String name;
  private List<CourseGrade> grades;
  private int expYrs;

  /**
   * Constructor that creates a new Applicant with the specified name and
   * grades.
   * 
   * @param name   The name of the applicant
   * @param grades The grades of the applicant
   * @param expYrs Years of previous experience that are relevant to the job
   *               they applied for
   */
  public Applicant(String name, List<CourseGrade> grades, int expYrs) {
    this.name = name;
    this.grades = grades;
    this.expYrs = expYrs;
  }

  public String getName() {
    return name;
  }

  public List<CourseGrade> getGrades() {
    return grades;
  }

  public CourseGrade getGradeFor(String course) {
    for (CourseGrade grade : grades) {
      if (grade.getCourseName().equals(course)) {
        return grade;
      }
    }
    return null;
  }

  public int getExpYrs() {
    return expYrs;
  }

  // @Override
  // public String toString() {
  // return "Applicant [name=" + name + ", grades=" + grades + "]";
  // }
}