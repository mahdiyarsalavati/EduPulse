import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Course implements Serializable {
    private String name;
    private Teacher teacher;
    private int creditUnit;
    private Map<Student, Double> studentGrades = new HashMap<>();
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

    public Map<Student, Double> getStudentGrades() {
        return studentGrades;
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

    public int getStudentsLength() {
        return studentGrades.size();
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
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
            studentGrades.put(student, 0.0);
            student.addCourse(this);
            student.addCreditUnit(this.creditUnit);
        }
    }

    public void addStudent(Student student, Number grade) {
        if (student != null) {
            studentGrades.put(student, grade.doubleValue());
            student.addCourse(this);
            student.addCreditUnit(this.creditUnit);
        }
    }

    public void removeStudent(Student student) {
        if (studentGrades.containsKey(student)) {
            studentGrades.remove(student);
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
        if (!assignments.remove(assignment)) {
            throw new IllegalArgumentException("Assignment could not be found!");
        }
    }

    public void addProject(Project project) {
        if (project.getCourse().equals(this)) {
            projects.add(project);
        }
    }

    public void removeProject(Project project) {
        if (!projects.remove(project)) {
            throw new IllegalArgumentException("Project could not be found!");
        }
    }

    public void printStudents() {
        if (studentGrades.isEmpty()) {
            System.out.println("There are no students");
            return;
        }
        int i = 1;
        for (Map.Entry<Student, Double> entry : studentGrades.entrySet()) {
            System.out.println(i + ": First Name: " + entry.getKey().getFirstName() + " - Last Name: " + entry.getKey().getLastName() + " - Grade: " + entry.getValue());
            i++;
        }
    }

    public Number getHighestGradeValue() {
        return Collections.max(studentGrades.values());
    }

    public Student getHighestGradeStudent() {
        return studentGrades.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new NoSuchElementException("No students in course"))
                .getKey();
    }

    public void gradeStudent(Student student, Number grade) {
        if (grade.doubleValue() < 0 || grade.doubleValue() > 20) return;
        if (studentGrades.containsKey(student)) {
            studentGrades.put(student, grade.doubleValue());
        } else {
            throw new IllegalArgumentException("Student could not be found!");
        }
    }

    public double getGrade(Student student) {
        return studentGrades.getOrDefault(student, 0.0);
    }

    public List<Student> getStudents() {
        return studentGrades.keySet().stream().collect(Collectors.toList());
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
        String studentsIDs = studentGrades.keySet().stream()
                .map(Student::getID)
                .collect(Collectors.joining(",", "[", "]"));

        String assignmentsIDs = "[";
        for (int i = 0; i < getAssignments().size(); i++) {
            assignmentsIDs += getAssignments().get(i).getID();
            if (i != getAssignments().size() - 1) {
                assignmentsIDs += ",";
            }
        }
        assignmentsIDs += "]";

        String projectsIDs = "[";
        for (int i = 0; i < getProjects().size(); i++) {
            projectsIDs += getProjects().get(i).getID();
            if (i != getProjects().size() - 1) {
                projectsIDs += ",";
            }
        }
        projectsIDs += "]";

        return name + "," + creditUnit + "," + studentsIDs + "," + projectsIDs + "," + assignmentsIDs + "," + isAvailable + "," + examDate + "," + semester + "," + teacher.getID() + "," + getID();
    }

    public String toMapString() {
        String result = getID().toString() + ",";
        var st = studentGrades.entrySet().stream().collect(Collectors.toList());
        for (int i = 0; i < st.size(); i++) {
            result += st.get(i).getKey().getID() + ":" + st.get(i).getValue();
            if (i != st.size() - 1) {
                result += ",";
            }
        }
        return result;
    }

}
