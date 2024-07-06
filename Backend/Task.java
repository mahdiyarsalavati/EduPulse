public class Task {
    private String title;
    private String time;
    private boolean isDone;
    private String username;

    public Task(String title, String time, boolean isDone, String username) {
        this.title = title;
        this.time = time;
        this.isDone = isDone;
        this.username = username;
    }

    @Override
    public String toString() {
        return title + "," + time + "," + isDone + "," + username;
    }

    public String getUsername() {
        return username;
    }
}
