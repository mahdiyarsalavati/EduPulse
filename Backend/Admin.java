import java.util.ArrayList;
import java.util.List;

public class Admin extends Teacher {

    private List<Teacher> teachers = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    private List<Assignment> assignments = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();

    public Admin(String firstName, String lastName, List<Course> courses, char[] password, List<Teacher> teachers) {
        super(firstName, lastName, courses, "admin", password);
        this.teachers = teachers;
    }

    public void addTeacher(Teacher teacher) {
        if (teacher != null && !teachers.contains(teacher)) teachers.add(teacher);
    }

    public void addAssignmentAdmin(Assignment assignment) {
        assignments.add(assignment);
    }

    public void addProjectAdmin(Project project) {
        projects.add(project);
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Student> getStudents() {
        return students;
    }

    public String removeTeacherByID(String ID) {
        boolean wasFound = false;
        for (Teacher teacher : teachers) {
            if (teacher.getID().equals(ID)) {
                wasFound = true;
                teachers.remove(teacher);
                break;
            }
        }
        if (wasFound) {
            return null;
        }
        return "Teacher was not found";
    }

    public Teacher findTeacherByID(String ID) {
        Teacher result = null;
        boolean wasFound = false;
        for (Teacher teacher : teachers) {
            if (teacher.getID().equals(ID)) {
                wasFound = true;
                result = teacher;
                break;
            }
        }


        return result;
    }

    public String removeCourseByID(String ID) {
        boolean wasFound = false;
        for (Course course : getCourses()) {
            if (course.getID().equals(ID)) {
                wasFound = true;
                getCourses().remove(course);
                removeCourse(course);
                break;
            }
        }
        if (wasFound) {
            return null;
        }
        return "Course was not found";
    }

    public String removeStudentByID(String ID) {
        boolean wasFound = false;
        for (Student student : students) {
            if (student.getID().equals(ID)) {
                wasFound = true;
                students.remove(student);
                for (Course course : student.getCourses()) {
                    course.removeStudent(student);
                }
                break;
            }
        }
        if (wasFound) {
            return null;
        }
        return "Student was not found";
    }

    public Course findCourseByID(String ID) {
        Course result = null;
        boolean wasFound = false;
        for (Course course : getCourses()) {
            if (course.getID().equals(ID)) {
                wasFound = true;
                result = course;
                break;
            }
        }
        return result;
    }

    public String removeAssignmentByID(String ID) {
        boolean wasFound = false;
        for (Assignment assignment : assignments) {
            if (assignment.getID().equals(ID)) {
                wasFound = true;
                assignments.remove(assignment);
                removeAssignment(assignment.getCourse(), assignment);
                break;
            }
        }
        if (wasFound) {
            return null;
        }
        return "Assignment was not found";
    }

    public String removeProjectByID(String ID) {
        boolean wasFound = false;
        for (Project project : projects) {
            if (project.getID().equals(ID)) {
                wasFound = true;
                projects.remove(project);
                removeProject(project.getCourse(), project);
                break;
            }
        }
        if (wasFound) {
            return null;
        }
        return "Assignment was not found";
    }

    public Student findStudentByID(String ID) {
        Student result = null;
        for (Student student : students) {
            if (student.getID().equals(ID)) {
                result = student;
                break;
            }
        }
        return result;
    }

    public Assignment findAssignmentByID(String ID) {
        Assignment result = null;
        for (Assignment assignment : assignments) {
            if (assignment.getID().equals(ID)) {
                result = assignment;
                break;
            }
        }
        return result;
    }

    public Project findProjectByID(String ID) {
        Project result = null;
        for (Project project : projects) {
            if (project.getID().equals(ID)) {
                result = project;
                break;
            }
        }
        return result;
    }

}