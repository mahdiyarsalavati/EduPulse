import java.util.List;

public class Admin extends Teacher {

    private List<Person> teachers;


    public Admin(String firstName, String lastName, List<Course> courses, List<Person> teachers) {
        super(firstName, lastName, courses);
        this.teachers = teachers;
    }

    public void addTeacher(Teacher teacher) {
        if (teacher != null && !teachers.contains(teacher))
            teachers.add(teacher);
    }


}