import java.util.List;

public class Admin extends Teacher {

    private List<Teacher> teachers;

    public Admin(String firstName, String lastName, List<Course> courses, char[] password, List<Teacher> teachers) {
        super(firstName, lastName, courses, "admin", password);
        this.teachers = teachers;
    }

    public void addTeacher(Teacher teacher) {
        if (teacher != null && !teachers.contains(teacher))
            teachers.add(teacher);
    }

    public void addCourse(Course course) {
        if (course != null && !getCourses().contains(course))
            getCourses().add(course);
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    @Override
    public List<Course> getCourses() {
        return getCourses();
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
                break;
            }
        }
        if (wasFound) {
            return null;
        }
        return "Course was not found";
    }
}