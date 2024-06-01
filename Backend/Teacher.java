import java.util.List;
import java.util.Arrays;

public class Teacher extends Person {

    public Teacher(String firstName, String lastName, List<Course> courses, String ID, char[] password) {
        super(firstName, lastName, courses, ID, password);
    }

    public void addAssignment(Course course, Assignment assignment) {
        if (course == null || !getCourses().contains(course)) {
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        }
        course.addAssignment(assignment);
    }

    public void removeAssignment(Course course, Assignment assignment) {
        if (course == null || !getCourses().contains(course)) {
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        }
        course.removeAssignment(assignment);
    }

    public void addProject(Course course, Project project) {
        if (course == null || !getCourses().contains(course)) {
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        }
        course.addProject(project);
    }

    public void removeProject(Course course, Project project) {
        if (course == null || !getCourses().contains(course)) {
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        }
        course.removeProject(project);
    }

    public void addStudent(Course course, Student student) {
        if (course != null && student != null) {
            course.addStudent(student);
        }
    }

    public void removeStudent(Course course, Student student) {
        if (course != null && student != null) {
            course.removeStudent(student);
        }
    }

    public void rewardStudent(Course course, Student student, Number reward) {
        if (course != null) {
            course.gradeStudent(student, course.getGrade(student) + reward.doubleValue());
        }
    }

    public void gradeStudent(Course course, Student student, Number grade) {
        if (course == null || !getCourses().contains(course)) {
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        }
        course.gradeStudent(student, grade.doubleValue());
    }

    public void extendDeadline(Assignment assignment, int days) {
        if (assignment != null) {
            assignment.extendDeadlineByDays(days);
        }
    }

    public void extendDeadline(Project project, int days) {
        if (project != null) {
            project.extendDeadlineByDays(days);
        }
    }

    public void activateAssignment(Assignment assignment, boolean isAvailable) {
        if (assignment != null) {
            assignment.setAvailable(isAvailable);
        }
    }

    public void activateProject(Project project, boolean isAvailable) {
        if (project != null) {
            project.setAvailable(isAvailable);
        }
    }

    @Override
    public String toString() {
        return "Teacher{ FirstName=" + getFirstName().replaceAll(" ", "") +
                " LastName=" + getLastName().replaceAll(" ", "") +
                " ID=" + getID().replaceAll(" ", "") +
                " Password=" + Arrays.toString(getPassword()) + "}";
    }
}
