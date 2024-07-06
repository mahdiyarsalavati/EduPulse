import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Assignment {
    private final LocalDate creationDate;
    private LocalDate deadline;
    private boolean isAvailable;
    private final Course course;
    private static final List<Assignment> archive = new ArrayList<>();
    private final String ID;
    private String estimated;

    public Assignment(LocalDate deadline, boolean isAvailable, Course course, String ID, String estimated) {
        this.creationDate = LocalDate.now();
        this.deadline = deadline;
        this.isAvailable = isAvailable;
        this.course = course;
        this.ID = ID;
        this.estimated = estimated;
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

    public String getEstimated() {
        return estimated;
    }

    public void setEstimated(String estimated) {
        this.estimated = estimated;
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
        long daysUntilDeadline = daysUntilDeadline();
        return "Assignment: " + getID() + ", Deadline: " + deadline + ", Days until deadline: " + daysUntilDeadline + ", Estimated: " + estimated;
    }
}
