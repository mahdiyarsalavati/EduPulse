import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Assignment {
    private int deadline;
    private boolean isAvailable;
    private final Course course;
    private static List<Assignment> archive = new ArrayList<>();

    public Assignment(int deadline, boolean isAvailable, Course course) {
        this.deadline = deadline;
        this.isAvailable = isAvailable;
        this.course = course;
        if(!isAvailable) archive.add(this);
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public void extendDeadlineByDays(int days) {
        this.deadline += days;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
        if(isAvailable) archive.remove(this);
    }

    public int getDeadline() {
        return deadline;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Course getCourse() {
        return course;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return deadline == that.deadline && isAvailable == that.isAvailable && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deadline, isAvailable, course);
    }
}
