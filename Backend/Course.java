import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private String name;
    private String profName;
    private Teacher teacher;
    private int creditUnit;
    private List<StudentItem> studentItems = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private List<Assignment> assignments = new ArrayList<>();
    private int studentItemsLength = 0;
    private int projectsLength = 0;
    private int assignmentsLength = 0;
    private boolean isAvailable;
    private String examDate;
    private Semester semester;

    public Course(String name, String profName, int creditUnit, List<Student> studentItems, List<Project> projects, List<Assignment> assignments, boolean isAvailable, String examDate, Semester semester, Teacher teacher) {
        this.name = name;
        this.profName = profName;
        this.creditUnit = creditUnit;
        this.isAvailable = isAvailable;
        this.examDate = examDate;
        this.semester = semester;
        this.teacher = teacher;

        for (Student student : studentItems) {
            this.addStudent(student);
            this.studentItemsLength++;
        }

        for (Assignment assignment : assignments) {
            if (assignment.getCourse().getName().equals(this.name)) {
                this.assignments.add(assignment);
                this.assignmentsLength++;
            }
        }


        for (Project project : projects) {
            if (project.getCourse().getName().equals(this.name)) {
                this.projects.add(project);
                this.projectsLength++;
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setCreditUnit(int creditUnit) {
        this.creditUnit = creditUnit;
    }

    public void setStudentItems(List<StudentItem> studentItems) {
        this.studentItems = studentItems;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public String getProfName() {
        return profName;
    }

    public int getCreditUnit() {
        return creditUnit;
    }

    public List<StudentItem> getStudentItems() {
        return studentItems;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public int getStudentItemsLength() {
        return studentItemsLength;
    }

    public int getProjectsLength() {
        return projectsLength;
    }

    public int getAssignmentsLength() {
        return assignmentsLength;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getExamDate() {
        return examDate;
    }

    public Semester getSemester() {
        return semester;
    }

    public void addStudent(Student student) {
        if (student != null) {
            StudentItem st = new StudentItem(student);
            studentItems.add(st);
            studentItemsLength++;
            student.addCourse(this);
            student.addCreditUnit(this.creditUnit);
        }
    }

    public void addStudent(Student student, Number grade) {
        if (student != null) {
            StudentItem st = new StudentItem(student, grade);
            studentItems.add(st);
            studentItemsLength++;
            student.addCourse(this);
            student.addCreditUnit(this.creditUnit);
        }
    }

    public void removeStudent(Student student) {
        boolean wasFound = false;
        StudentItem toRemove = null;
        for (StudentItem si : studentItems) {
            if (si.getStudent().equals(student)) {
                wasFound = true;
                toRemove = si;
                break;
            }
        }
        if (wasFound) {
            studentItems.remove(toRemove);
            studentItemsLength--;
            student.removeCourse(this);
            student.addCreditUnit(-1 * this.creditUnit);
        } else {
            throw new IllegalArgumentException("Student could not be found!");
        }
    }


    public void addAssignment(Assignment assignment) {
        if (assignment.getCourse().equals(this)) {
            assignments.add(assignment);
            assignmentsLength++;
        }
    }

    public void removeAssignment(Assignment assignment) {
        if (!assignments.contains(assignment)) throw new IllegalArgumentException("Assignment could not be found!");
        else {
            assignments.remove(assignment);
            assignmentsLength--;
        }
    }

    public void addProject(Project project) {
        if (project.getCourse().equals(this)) {
            projects.add(project);
            projectsLength++;
        }
    }

    public void removeProject(Project project) {
        if (!projects.contains(project)) throw new IllegalArgumentException("Project could not be found!");
        else {
            projects.remove(project);
            projectsLength--;
        }
    }

    public void printStudents() {
        if (studentItemsLength == 0) {
            System.out.println("There is no students");
            return;
        }
        int i = 1;
        for (StudentItem si : studentItems) {
            System.out.println(i + ": First Name: " + si.getStudent().getFirstName() + " - Last Name: " + si.getStudent().getLastName() + " - Grade: " + si.getGrade());
            i++;
        }
    }

    public Number getHighestGradeValue() {
        double MAX = studentItems.get(0).getGrade().doubleValue();
        for (int i = 1; i < studentItems.size(); i++) {
            if (studentItems.get(i).getGrade().doubleValue() > MAX) {
                MAX = studentItems.get(i).getGrade().doubleValue();
            }
        }
        return MAX;
    }

    public Student getHighestGradeStudent() {
        double MAX = studentItems.get(0).getGrade().doubleValue();
        Student result = studentItems.get(0).getStudent();
        for (int i = 1; i < studentItems.size(); i++) {
            if (studentItems.get(i).getGrade().doubleValue() > MAX) {
                MAX = studentItems.get(i).getGrade().doubleValue();
                result = studentItems.get(i).getStudent();
            }
        }
        return result;
    }

    public void gradeStudent(Student student, Number grade) {
        if (grade.doubleValue() < 0 || grade.doubleValue() > 20) return;
        boolean wasFound = false;
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
        return creditUnit == course.creditUnit && Objects.equals(name, course.name) && Objects.equals(teacher, course.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teacher, creditUnit);
    }
}