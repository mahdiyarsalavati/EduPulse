import java.util.List;

public class Teacher extends Person {
    public Teacher(String firstName, String lastName, List<Course> courses) {
        super(firstName, lastName, courses);
    }

    public void addAssignment(Course course, Assignment assignment) {
        if (!getCourses().contains(course))
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        else {
            for (Course c : getCourses()) {
                if (c.equals(course)) {
                    c.addAssignment(assignment);
                    return;
                }
            }
        }
    }

    public void removeAssignment(Course course, Assignment assignment) {
        if (!getCourses().contains(course))
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        else {
            for (Course c : getCourses()) {
                if (c.equals(course)) {
                    if (c.getAssignments().contains(assignment)) {
                        c.removeAssignment(assignment);
                    }
                    return;
                }
            }
        }
    }

    public void addProject(Course course, Project project) {
        if (!getCourses().contains(course))
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        else {
            for (Course c : getCourses()) {
                if (c.equals(course)) {
                    c.addProject(project);
                }
            }
        }
    }

    public void removeProject(Course course, Project project) {
        if (!getCourses().contains(course))
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        else {
            for (Course c : getCourses()) {
                if (c.equals(course)) {
                    if (c.getProjects().contains(project)) {
                        c.removeProject(project);
                    }
                    return;
                }
            }
        }
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
        course.gradeStudent(student, course.getGrade(student) + reward.doubleValue());
    }

    public void gradeStudent(Course course, Student student, Number grade) {
        if (!getCourses().contains(course))
            throw new IllegalArgumentException("Course could not be found (for this teacher)!");
        else {
            for (Course c : getCourses()) {
                if (c.equals(course)) {
                    c.gradeStudent(student, grade);
                    return;
                }
            }
        }
    }

    public void extendDeadline(Assignment assignment, int day) {
        assignment.setDeadline(assignment.getDeadline() + day);
    }

    public void extendDeadline(Project project, int day) {
        project.setDeadline(project.getDeadline() + day);
    }

    public void activeAssignment(Assignment assignment, boolean isAvailable) {
        assignment.setAvailable(isAvailable);
    }

    public void activeProject(Project project, boolean isAvailable) {
        project.setAvailable(isAvailable);
    }



}
