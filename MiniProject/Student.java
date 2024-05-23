import java.util.List;
import java.util.Objects;

public class Student extends Person {
    private int creditUnits = 0;
    private double averageGrade = 0.0;
    private double semesterGrade = 0.0;
    private String id;
    private Semester semester;


    public Student(String firstName, String lastName, List<Course> courses, String id, Semester semester) {
        super(firstName, lastName, courses);
        for (Course course : courses) {
            this.creditUnits += course.getCreditUnit();
        }
        this.id = id;
        this.semester = semester;
        updateAverageGrade();
        updateSemesterGrade();
    }

    public void setid(String id) {
        this.id = id;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public int getCreditUnits() {
        this.creditUnits = 0;
        for (Course course : getCourses()) {
            this.creditUnits += course.getCreditUnit();
        }
        return creditUnits;
    }

    public void addCreditUnit(int cu) {
        this.creditUnits += cu;
    }

    public double getAverageGrade() {
        updateAverageGrade();
        return averageGrade;
    }

    public double getSemesterGrade() {
        updateSemesterGrade();
        return semesterGrade;
    }

    public String getid() {
        return id;
    }

    public Semester getSemester() {
        return semester;
    }

    private void updateAverageGrade() {
        if (getCourses().isEmpty()) {
            averageGrade = 0;
            return;
        }
        double result = 0.0;
        int numberOfCredits = 0;
        for (Course course : getCourses()) {
            result += course.getGrade(this) * course.getCreditUnit();
            numberOfCredits += course.getCreditUnit();
        }
        result /= numberOfCredits;
        averageGrade = result;
    }

    private void updateSemesterGrade() {
        if (getCourses().isEmpty()) {
            semesterGrade = 0;
            return;
        }
        double result = 0.0;
        int semesterCredit = 0;
        for (Course course : getCourses()) {
            if (course.getSemester() == this.semester) {
                result += course.getGrade(this) * course.getCreditUnit();
                semesterCredit += course.getCreditUnit();
            }
        }
        result /= semesterCredit;
        semesterGrade = result;
    }

    @Override
    public String toString() {
        updateAverageGrade();
        updateSemesterGrade();
        return "Name: " + getFirstName() + "\n" + "Last Name: " + getLastName() + "\n" + "SEMESTER: " + getSemester() + "\n" + "Average grade: " + averageGrade + "\n" + "Semester grade: " + semesterGrade + "\n" + "---------------------";
    }

    public void printCourses() {
        if (getCourses().isEmpty()) {
            System.out.println("There is no courses");
            return;
        }
        System.out.println(this.getFirstName() + " " + this.getLastName() + "'s courses:");
        int i = 1;
        for (Course course : getCourses()) {
            System.out.println("" + i + ": Course's Name: " + course.getName() + " (" + course.getSemester() + " SEMESTER) " + " - Credit Unit: " + course.getCreditUnit() + " - Grade: " + course.getGrade(this));
            i++;
        }
        System.out.println("------------------");
    }

    public void printAverageGrade() {
        updateAverageGrade();
        System.out.println(getFirstName() + " " + getLastName() + "'s average grade is " + averageGrade);
    }

    public void printCreditUnits() {
        int cu = getCreditUnits();
        System.out.println(getFirstName() + " " + getLastName() + "'s total credit units is " + cu);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
