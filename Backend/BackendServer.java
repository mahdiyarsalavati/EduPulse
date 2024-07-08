import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackendServer {

    private static final int PORT = 12345;
    private static final String TEACHERS_FILE = "Backend/assets/teachers.txt";
    private static final String STUDENTS_FILE = "Backend/assets/students.txt";
    private static final String COURSES_FILE = "Backend/assets/courses.txt";
    private static final String ASSIGNMENTS_FILE = "Backend/assets/assignments.txt";
    private static final String PROJECTS_FILE = "Backend/assets/projects.txt";
    private static final String COURSES_MAPS_FILE = "Backend/assets/coursesMaps.txt";

    static List<Teacher> teachers = new ArrayList<>();
    static List<Student> students = new ArrayList<>();
    static List<Course> courses = new ArrayList<>();
    static List<Assignment> assignments = new ArrayList<>();
    static List<Project> projects = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        loadAllData();
        new SocketServer().start();
    }

    private static void loadAllData() {
        CLI.loadTeachers(courses, teachers, TEACHERS_FILE);
        CLI.loadStudents(courses, students, STUDENTS_FILE);
        CLI.loadCourses(courses, teachers, students, assignments, projects, COURSES_FILE);
        CLI.loadAssignments(courses, assignments, ASSIGNMENTS_FILE);
        CLI.loadProjects(courses, projects, PROJECTS_FILE);
        CLI.loadCourseMaps(courses, students, COURSES_MAPS_FILE);
    }

    static void rewrite() {
        writeAll(teachers, TEACHERS_FILE);
        writeAll(students, STUDENTS_FILE);
        writeAll(courses, COURSES_FILE);
        writeAll(assignments, ASSIGNMENTS_FILE);
        writeAll(projects, PROJECTS_FILE);
        writeCourseMaps();
    }

    private static <T> void writeAll(List<T> objects, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            for (T object : objects) {
                fileWriter.write(object.toString() + "\n");
            }
        } catch (IOException ignored) {
        }
    }

    private static void writeCourseMaps() {
        try (FileWriter fileWriter = new FileWriter(COURSES_MAPS_FILE)) {
            for (Course course : courses) {
                fileWriter.write(course.toMapString() + "\n");
            }
        } catch (IOException ignored) {
        }
    }

    static class SocketServer {
        private ServerSocket serverSocket;
        private ExecutorService executorService;

        public SocketServer() throws IOException {
            serverSocket = new ServerSocket(PORT);
            executorService = Executors.newFixedThreadPool(10);
        }

        public void start() {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executorService.submit(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleClient(Socket clientSocket) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request;
                while ((request = in.readLine()) != null) {
                    String response = processRequest(request);
                    System.out.println("REQUEST: " + request);
                    out.println(response);
                    System.out.println("RESPONSE: " + response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private String processRequest(String request) {
            String[] parts = request.split(" ");
            String command = parts[0];
            String response = "";

            switch (command) {
                case "GET_ASSIGNMENTS":
                    response = getAssignments(parts[1]);
                    break;
                case "LOGIN":
                    response = login(parts);
                    break;
                case "SIGNUP":
                    response = signup(parts);
                    break;
                case "GET_HOMEPAGE_DATA":
                    response = getHomePageData(parts[1]);
                    break;
                case "GET_INFOPAGE_DATA":
                    Student s1 = getStudentByUsername(parts[1]);
                    response = "INFOPAGE " + s1.getAverageGrade() + " " + s1.getSemester() + " " + s1.getCreditUnits();
                    break;
                case "CHANGE_NAME":
                    changeName(parts[1], parts[2], parts[3]);
                    response = "";
                    break;
                case "CHANGE_PASSWORD":
                    changePassword(parts[1], parts[2]);
                    response = "";
                    break;
                case "REMOVE_STUDENT":
                    removeStudent(parts[1]);
                    response = "";
                    break;
                case "GET_COURSES_OF_STUDENT":
                    response = getCoursesOfStudent(parts[1]);
                    break;
                case "REQUEST_JOIN_COURSE":
                    response = requestJoinCourse(parts[1], parts[2]);
                    break;
                default:
                    response = "UNKNOWN_COMMAND";
                    break;
            }

            return response;
        }

        private String login(String[] parts) {
            if (parts.length < 3) return "INVALID_PARAMETERS";
            String username = parts[1];
            char[] password = parts[2].toCharArray();

            boolean exists = students.stream()
                    .anyMatch(x -> x.getID().equals(username) && Arrays.equals(x.getPassword(), password));

            if (exists) {
                Student loggedInStudent = getStudentByUsername(username);
                String response = "LOGIN_SUCCESS " + loggedInStudent.getFirstName() + " " + loggedInStudent.getLastName();
                response += " " + loggedInStudent.getAssignments().size();
                response += " " + loggedInStudent.getCourses().size();
                response += " " + loggedInStudent.getHighestGrade();
                response += " " + loggedInStudent.getLowestGrade();
                response += " " + listOfAssignments(username);
                response += " END";
                response += " " + listOfNotActiveAssignments(username);
                return response;
            }

            return "LOGIN_FAILED";
        }

        private String signup(String[] parts) {
            String firstName = parts[1];
            String lastName = parts[2];
            String username = parts[3];
            String password = parts[4];

            boolean exists = students.stream().anyMatch(s -> s.getID().equals(username));

            if (!exists) {
                Student student = new Student(firstName, lastName, new ArrayList<>(), password.toCharArray(), username, Semester.FIRST);
                students.add(student);
                rewrite();
                return "SIGNUP_SUCCESS";
            }
            return "USER_ALREADY_EXISTS";
        }


        private String getCoursesOfStudent(String username) {
            String result = "";
            for (Course course : getStudentByUsername(username).getCourses()) {
                result += course.getName() + " " + course.getTeacher().getFirstName() + course.getTeacher().getLastName() + " " + course.getCreditUnit() + " " + course.getAssignments().size() + " " + course.getHighestGradeStudent().getFirstName() + course.getHighestGradeStudent().getLastName() + " END" + " ";
            }
            return result.trim();
        }

        private String requestJoinCourse(String username, String courseCode) {
            Course course = courses.stream().filter(c -> c.getID().equals(courseCode)).findFirst().orElse(null);
            Student student = getStudentByUsername(username);

            if (course != null && student != null && canJoinCourse(student, course)) {
                course.addStudent(student, 0.0);
                rewrite();
                return "JOIN_SUCCESS";
            } else {
                return "JOIN_FAILED";
            }
        }

        private boolean canJoinCourse(Student student, Course course) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Can " + student.getFirstName() + " " + student.getLastName() + " join " + course.getName() + "? " + "They have " + student.getCreditUnits() + " credit unit(s) already? (y/n):");
            String response = scanner.nextLine();
            return response.equalsIgnoreCase("y");
        }

        private static void removeStudent(String username) {
            students.removeIf(student -> student.getID().equals(username));

            for (Course course : courses) {
                course.removeStudentByUsername(username);
            }

            rewrite();
        }

        private void changePassword(String username, String newPassword) {
            Student student = getStudentByUsername(username);
            student.changePassword(newPassword);
            rewrite();

        }

        private void changeName(String username, String newName, String newLastName) {
            Student student = getStudentByUsername(username);
            student.changeName(newName, newLastName);
            rewrite();
        }

        private Student getStudentByUsername(String username) {
            return students.stream()
                    .filter(x -> x.getID().equals(username))
                    .findFirst()
                    .get();
        }

        private String getHomePageData(String username) {
            Student student = getStudentByUsername(username);
            int numberOfExams = student.getCourses().size();
            String activeAssignments = listOfAssignments(username);
            String notActiveAssignments = listOfNotActiveAssignments(username);

            return "HOMEPAGE_DATA " + student.getFirstName() + " " + student.getLastName() + " " + student.getAssignments().size() + " " + numberOfExams + " " + student.getHighestGrade() + " " + student.getLowestGrade() + " " + activeAssignments + " END " + notActiveAssignments;
        }

        private String getAssignments(String username) {
            String result = "";
            Student student = getStudentByUsername(username);
            for (Course course : student.getCourses()) {
                for (Assignment assignment : course.getAssignments()) {
                    result += " ASSIGNMENT " + getAssignmentName(assignment) + " " + assignment.daysUntilDeadline() + " " + assignment.getEstimated();
                }
            }
            result = result.replaceFirst(" ASSIGNMENT ", "");
            return result.trim();
        }

        private String getAssignmentName(Assignment assignment) {
            return assignment.getCourse().getName() + (assignment.getCourse().getAssignments().indexOf(assignment) + 1);
        }

        private String listOfAssignments(String username) {
            Student student = getStudentByUsername(username);
            List<Assignment> assignmentsStudent = student.getAssignments();
            String result = "";
            for (Assignment a : assignmentsStudent) {
                if (a.isAvailable()) {
                    result += a.getCourse().getName() + (a.getCourse().getAssignments().indexOf(a) + 1) + " ";
                }
            }
            return result.trim();
        }

        private String listOfNotActiveAssignments(String username) {
            Student student = getStudentByUsername(username);
            List<Assignment> assignmentsStudent = student.getAssignments();
            String result = "";
            for (Assignment a : assignmentsStudent) {
                if (!a.isAvailable()) {
                    result += a.getCourse().getName() + (a.getCourse().getAssignments().indexOf(a) + 1) + " ";
                }
            }
            return result.trim();
        }
    }
}