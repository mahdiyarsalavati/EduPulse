import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Assignment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LocalDate creationDate;
    private LocalDate deadline;
    private boolean isAvailable;
    private final Course course;
    private static final List<Assignment> archive = new ArrayList<>();
    private final String ID;

    public Assignment(LocalDate deadline, boolean isAvailable, Course course, String ID) {
        this.creationDate = LocalDate.now();
        this.deadline = deadline;
        this.isAvailable = isAvailable;
        this.course = course;
        this.ID = ID;
        if (!isAvailable) {
            archive.add(this);
        }
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
        if (available) {
            archive.remove(this);
        } else {
            archive.add(this);
        }
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

    public String getID() {
        return ID;
    }

    public void extendDeadlineByDays(int days) {
        this.deadline = this.deadline.plusDays(days);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(ID, that.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public String toString() {
        return deadline + "," + isAvailable + "," + course.getID() + "," + getID();
    }
}
