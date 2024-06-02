import java.util.List;

public class Person {
    private String firstName;
    private String lastName;
    private List<Course> courses;
    private int coursesLength;
    private String ID;
    private char[] password;

    public Person(String firstName, String lastName, List<Course> courses, String ID, char[] password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = courses;
        this.coursesLength = (courses == null) ? 0 : courses.size();
        this.password = password;
        this.ID = ID;
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
        this.courses = courses;
        this.coursesLength = (courses == null) ? 0 : courses.size();
    }

    public void addCourse(Course course) {
        if (!this.getCourses().contains(course))
            courses.add(course);
        this.coursesLength++;
    }

    public void removeCourse(Course course) {
        if (courses.remove(course)) {
            this.coursesLength--;
        }
    }

    public int getCoursesLength() {
        return coursesLength;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", coursesLength=" + coursesLength +
                ", ID='" + ID + '\'' +
                '}';
    }
}
