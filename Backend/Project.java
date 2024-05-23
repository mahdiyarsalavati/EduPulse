public class Project extends Assignment {
    private String name;

    public Project(int deadline, boolean isAvailable, Course course, String name) {
        super(deadline, isAvailable, course);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
