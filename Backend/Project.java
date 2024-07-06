import java.time.LocalDate;

public class Project extends Assignment {
    private String name;
    private String estimated;

    public Project(LocalDate deadline, boolean isAvailable, Course course, String ID, String name, String estimated) {
        super(deadline, isAvailable, course, ID, estimated);
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getDeadline() + "," + isAvailable() + "," + getCourse().getID() + "," + getID() + "," + name;
    }
}
