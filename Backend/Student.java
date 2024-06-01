import java.util.List;
import java.util.Objects;

public class Student extends Person {
    private int creditUnits;
    private double averageGrade;
    private double semesterGrade;
    private Semester semester;

    public Student(String firstName, String lastName, List<Course> courses, char[] password, String id, Semester semester) {
        super(firstName, lastName, courses, id, password);
        this.semester = semester;
        updateCreditUnits();
        updateAverageGrade();
        updateSemesterGrade();
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
        updateSemesterGrade();
    }

    public int getCreditUnits() {
        int result = getCourses().stream().map(x -> x.getCreditUnit()).reduce((a, b) -> a + b).orElse(0);
        return result;
    }

    public void addCreditUnit(int creditUnits) {
        this.creditUnits += creditUnits;
    }

    private void updateCreditUnits() {
        this.creditUnits = getCourses().stream().mapToInt(Course::getCreditUnit).sum();
    }

    public double getAverageGrade() {
        updateAverageGrade();
        return averageGrade;
    }

    public double getSemesterGrade() {
        updateSemesterGrade();
        return semesterGrade;
    }

    private void updateAverageGrade() {
        if (getCourses().isEmpty()) {
            this.averageGrade = 0;
            return;
        }
        double totalGrade = 0;
        int totalCredits = 0;
        for (Course course : getCourses()) {
            totalGrade += course.getGrade(this) * course.getCreditUnit();
            totalCredits += course.getCreditUnit();
        }
        this.averageGrade = totalGrade / totalCredits;
    }

    private void updateSemesterGrade() {
        if (getCourses().isEmpty()) {
            this.semesterGrade = 0;
            return;
        }
        double totalGrade = 0;
        int semesterCredits = 0;
        for (Course course : getCourses()) {
            if (course.getSemester() == this.semester) {
                totalGrade += course.getGrade(this) * course.getCreditUnit();
                semesterCredits += course.getCreditUnit();
            }
        }
        this.semesterGrade = totalGrade / semesterCredits;
    }

    public void printCourses() {
        if (getCourses().isEmpty()) {
            System.out.println("There are no courses.");
            return;
        }
        System.out.println(this.getFirstName() + " " + this.getLastName() + "'s courses:");
        int i = 1;
        for (Course course : getCourses()) {
            System.out.println(i + ": Course's Name: " + course.getName() + " (" + course.getSemester() + " SEMESTER) - Credit Unit: " + course.getCreditUnit() + " - Grade: " + course.getGrade(this));
            i++;
        }
        System.out.println("------------------");
    }

    public void printAverageGrade() {
        System.out.println(getFirstName() + " " + getLastName() + "'s average grade is " + getAverageGrade());
    }

    public void printCreditUnits() {
        System.out.println(getFirstName() + " " + getLastName() + "'s total credit units are " + getCreditUnits());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return getID().equals(student.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    @Override
    public String toString() {
        String coursesIDs = "[";
        for (int i = 0; i < getCourses().size(); i++) {
            coursesIDs += getCourses().get(i).getID();
            if (i != getCourses().size() - 1) {
                coursesIDs += ",";
            }
        }
        coursesIDs += "]";
        StringBuilder password = new StringBuilder();
        for (char c : getPassword()) {
            password.append(c);
        }
        return getFirstName() + "," + getLastName() + "," + coursesIDs + "," + password + "," + getID() + "," + semester;
    }
}
