import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Assignment {
    private LocalDate creationDate;
    private LocalDate deadline;
    private boolean isAvailable;
    private final Course course;
    private static List<Assignment> archive = new ArrayList<>();

    public Assignment(LocalDate deadline, boolean isAvailable, Course course) {
        this.creationDate = LocalDate.now();
        this.deadline = deadline;
        this.isAvailable = isAvailable;
        this.course = course;
        if(!isAvailable) archive.add(this);
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void extendDeadlineByDays(int days) {
        this.deadline = this.deadline.plusDays(days);
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
        if(isAvailable) archive.remove(this);
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Course getCourse() {
        return course;
    }

    public long daysUntilDeadline() {
        return ChronoUnit.DAYS.between(LocalDate.now(), this.deadline);
    }

    public long daysSinceCreation() {
        return ChronoUnit.DAYS.between(this.creationDate, LocalDate.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return isAvailable == that.isAvailable &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(deadline, that.deadline) &&
                Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creationDate, deadline, isAvailable, course);
    }
}
