import java.util.List;

public class Person {
    private String firstName;
    private String lastName;
    private List<Course> courses;
    private int coursesLength;

    private char[] password;

    public Person(String firstName, String lastName, List<Course> courses, char[] password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = courses;
        this.coursesLength = courses.size();
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.coursesLength = courses.size();
        this.courses = courses;
    }

    public void addCourse(Course course) {
        if (course != null) courses.add(course);
        this.coursesLength++;
    }

    public void removeCourse(Course course) {
        if (!courses.contains(course)) return;
        courses.remove(course);
        this.coursesLength--;
    }

    public int getCoursesLength() {
        return coursesLength;
    }
}
