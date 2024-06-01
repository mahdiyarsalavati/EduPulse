import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable {
    private String name;
    private Teacher teacher;
    private int creditUnit;
    private List<StudentItem> studentItems = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private List<Assignment> assignments = new ArrayList<>();
    private boolean isAvailable;
    private String examDate;
    private Semester semester;
    private String ID;

    public Course(String name, int creditUnit, List<Student> students, List<Project> projects, List<Assignment> assignments, boolean isAvailable, String examDate, Semester semester, Teacher teacher, String ID) {
        this.name = name;
        this.creditUnit = creditUnit;
        this.isAvailable = isAvailable;
        this.examDate = examDate;
        this.semester = semester;
        this.teacher = teacher;
        this.ID = ID;

        if (students != null) {
            for (Student student : students) {
                this.addStudent(student);
            }
        }

        if (projects != null) {
            for (Project project : projects) {
                this.addProject(project);
            }
        }

        if (assignments != null) {
            for (Assignment assignment : assignments) {
                this.addAssignment(assignment);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getCreditUnit() {
        return creditUnit;
    }

    public void setCreditUnit(int creditUnit) {
        this.creditUnit = creditUnit;
    }

    public List<StudentItem> getStudentItems() {
        return studentItems;
    }

    public void setStudentItems(List<StudentItem> studentItems) {
        this.studentItems = studentItems;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public int getAssignmentsLength() {
        return assignments.size();
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public int getStudentItemsLength() {
        return studentItems.size();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public String getID() {
        return ID;
    }

    public void addStudent(Student student) {
        if (student != null) {
            StudentItem st = new StudentItem(student);
            studentItems.add(st);
            student.addCourse(this);
            student.addCreditUnit(this.creditUnit);
        }
    }

    public void addStudent(Student student, Number grade) {
        if (student != null) {
            StudentItem st = new StudentItem(student, grade);
            studentItems.add(st);
            student.addCourse(this);
            student.addCreditUnit(this.creditUnit);
        }
    }

    public void removeStudent(Student student) {
        StudentItem toRemove = null;
        for (StudentItem si : studentItems) {
            if (si.getStudent().equals(student)) {
                toRemove = si;
                break;
            }
        }
        if (toRemove != null) {
            studentItems.remove(toRemove);
            student.removeCourse(this);
            student.addCreditUnit(-this.creditUnit);
        } else {
            throw new IllegalArgumentException("Student could not be found!");
        }
    }

    public void addAssignment(Assignment assignment) {
        if (assignment.getCourse().equals(this)) {
            assignments.add(assignment);
        }
    }

    public void removeAssignment(Assignment assignment) {
        if (assignments.remove(assignment)) {
            return;
        } else {
            throw new IllegalArgumentException("Assignment could not be found!");
        }
    }

    public void addProject(Project project) {
        if (project.getCourse().equals(this)) {
            projects.add(project);
        }
    }

    public void removeProject(Project project) {
        if (projects.remove(project)) {
            return;
        } else {
            throw new IllegalArgumentException("Project could not be found!");
        }
    }

    public void printStudents() {
        if (studentItems.isEmpty()) {
            System.out.println("There are no students");
            return;
        }
        int i = 1;
        for (StudentItem si : studentItems) {
            System.out.println(i + ": First Name: " + si.getStudent().getFirstName() + " - Last Name: " + si.getStudent().getLastName() + " - Grade: " + si.getGrade());
            i++;
        }
    }

    public Number getHighestGradeValue() {
        double maxGrade = studentItems.get(0).getGrade().doubleValue();
        for (StudentItem si : studentItems) {
            double grade = si.getGrade().doubleValue();
            if (grade > maxGrade) {
                maxGrade = grade;
            }
        }
        return maxGrade;
    }

    public Student getHighestGradeStudent() {
        double maxGrade = studentItems.get(0).getGrade().doubleValue();
        Student highestGradeStudent = studentItems.get(0).getStudent();
        for (StudentItem si : studentItems) {
            double grade = si.getGrade().doubleValue();
            if (grade > maxGrade) {
                maxGrade = grade;
                highestGradeStudent = si.getStudent();
            }
        }
        return highestGradeStudent;
    }

    public void gradeStudent(Student student, Number grade) {
        if (grade.doubleValue() < 0 || grade.doubleValue() > 20) return;
        for (StudentItem si : studentItems) {
            if (si.getStudent().equals(student)) {
                si.setGrade(grade);
                return;
            }
        }
        throw new IllegalArgumentException("Student could not be found!");
    }

    public double getGrade(Student student) {
        for (StudentItem si : studentItems) {
            if (si.getStudent().equals(student)) {
                return si.getGrade().doubleValue();
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(ID, course.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public String toString() {
        return "Course{" +
                "Name='" + name + '\'' +
                ", creditUnit=" + creditUnit +
                ", ID='" + ID + '\'' +
                '}';
    }
}
