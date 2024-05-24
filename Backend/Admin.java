import java.util.List;

public class Admin extends Teacher {

    private List<Teacher> teachers;
    private List<Course> courses;


    public Admin(String firstName, String lastName, List<Course> courses, char[] password, List<Teacher> teachers) {
        super(firstName, lastName, courses, "admin", password);
        this.teachers = teachers;
    }

    public void addTeacher(Teacher teacher) {
        if (teacher != null && !teachers.contains(teacher))
            teachers.add(teacher);
    }

    public void addCourse(Course course) {
        if (course != null && !courses.contains(course))
            courses.add(course);
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    @Override
    public List<Course> getCourses() {
        return courses;
    }

    public String removeTeacherByID(String ID) {
        boolean wasFound = false;
        for(Teacher teacher : teachers) {
            if(teacher.getID().equals(ID)) {
                wasFound = true;
                teachers.remove(teacher);
                break;
            }
        }
        if(wasFound) {
            return null;
        }
        return "Teacher was not found";
    }
}