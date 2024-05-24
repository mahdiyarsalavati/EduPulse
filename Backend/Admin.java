import java.util.List;

public class Admin extends Teacher {

    private List<Person> teachers;
    private List<Course> courses;


    public Admin(String firstName, String lastName, List<Course> courses, char[] password, List<Person> teachers) {
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


}