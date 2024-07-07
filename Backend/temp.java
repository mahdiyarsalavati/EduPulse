//import java.io.BufferedReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.stream.Collectors;
//
//public class temp {
//
//    private static final int PORT = 8280;
//    private static final String TEACHERS_FILE = "Backend/assets/teachers.txt";
//    private static final String STUDENTS_FILE = "Backend/assets/students.txt";
//    private static final String COURSES_FILE = "Backend/assets/courses.txt";
//    private static final String ASSIGNMENTS_FILE = "Backend/assets/assignments.txt";
//    private static final String PROJECTS_FILE = "Backend/assets/projects.txt";
//    private static final String COURSES_MAPS_FILE = "Backend/assets/coursesMaps.txt";
//
//    static List<Teacher> teachers = new ArrayList<>();
//    static List<Student> students = new ArrayList<>();
//    static List<Course> courses = new ArrayList<>();
//    static List<Assignment> assignments = new ArrayList<>();
//    static List<Project> projects = new ArrayList<>();
//
//    public static void main(String[] args) throws IOException {
//        loadAllData();
//        new SocketServer().start();
//    }
//
//    private static void loadAllData() {
//        CLI.loadTeachers(courses, teachers, TEACHERS_FILE);
//        CLI.loadStudents(courses, students, STUDENTS_FILE);
//        CLI.loadCourses(courses, teachers, students, assignments, projects, COURSES_FILE);
//        CLI.loadAssignments(courses, assignments, ASSIGNMENTS_FILE);
//        CLI.loadProjects(courses, projects, PROJECTS_FILE);
//        CLI.loadCourseMaps(courses, students, COURSES_MAPS_FILE);
//    }
//
//    static void rewrite() {
//        writeAll(teachers, TEACHERS_FILE);
//        writeAll(students, STUDENTS_FILE);
//        writeAll(courses, COURSES_FILE);
//        writeAll(assignments, ASSIGNMENTS_FILE);
//        writeAll(projects, PROJECTS_FILE);
//        writeCourseMaps();
//    }
//
//    private static <T> void writeAll(List<T> objects, String filePath) {
//        try (FileWriter fileWriter = new FileWriter(filePath)) {
//            for (T object : objects) {
//                fileWriter.write(object.toString() + "\n");
//            }
//        } catch (IOException ignored) {
//        }
//    }
//
//    private static void writeCourseMaps() {
//        try (FileWriter fileWriter = new FileWriter(COURSES_MAPS_FILE)) {
//            for (Course course : courses) {
//                fileWriter.write(course.toMapString() + "\n");
//            }
//        } catch (IOException ignored) {
//        }
//    }
//
//    static class SocketServer {
//        private ServerSocket serverSocket;
//        private ExecutorService executorService;
//
//        public SocketServer() throws IOException {
//            serverSocket = new ServerSocket(PORT);
//            executorService = Executors.newCachedThreadPool();
//        }
//
//        public void start() {
//            while (true) {
//                try {
//                    Socket clientSocket = serverSocket.accept();
//                    executorService.submit(() -> handleClient(clientSocket));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        private void handleClient(Socket clientSocket) {
//            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
//
//                String request;
//                while ((request = in.readLine()) != null) {
//                    String response = processRequest(request);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private String signup(String[] parts) {
//            String firstName = parts[1];
//            String lastName = parts[2];
//            String username = parts[3];
//            String password = parts[4];
//
//            boolean exists = students.stream().anyMatch(s -> s.getID().equals(username)) ||
//                    teachers.stream().anyMatch(t -> t.getID().equals(username));
//
//            if (!exists) {
//                Student student = new Student(firstName, lastName, new ArrayList<>(), password.toCharArray(), username, Semester.FIRST);
//                students.add(student);
//                rewrite();
//                return "SIGNUP_SUCCESS";
//            }
//            return "USER_ALREADY_EXISTS";
//        }
//
//        private String processRequest(String request) {
//            String[] parts = request.split(" ");
//            String command = parts[0];
//            String response = "";
//            System.out.println(request);
//
//            switch (command) {
//                case "GET_ASSIGNMENTS":
//                    response = getAssignments(parts[1]);
//                    break;
//                case "LOGIN":
//                    response = login(parts);
//                    break;
//                case "SIGNUP":
//                    response = signup(parts);
//                    break;
//                case "GET_HOMEPAGE_DATA":
//                    response = getHomePageData(parts[1]);
//                    break;
//                case "GET_INFOPAGE_DATA":
//                    response = "INFOPAGE " + getAverage(parts[1]) + " " + getSemester(parts[1]) + " " + getCreditUnits(parts[1]);
//                    break;
//                case "CHANGE_NAME":
//                    changeName(parts[1], parts[2], parts[3]);
//                    response = "";
//                    break;
//                case "CHANGE_PASSWORD":
//                    changePassword(parts[1], parts[2]);
//                    response = "";
//                    break;
//                case "REMOVE_STUDENT":
//                    removeStudent(parts[1]);
//                    response = "";
//                    break;
//                case "GET_COURSES_OF_STUDENT":
//                    response = getCoursesOfStudent(parts[1]);
//                    break;
//                case "REQUEST_JOIN_COURSE":
//                    response = requestJoinCourse(parts[1], parts[2]);
//                    break;
//                default:
//                    response = "UNKNOWN_COMMAND";
//                    break;
//            }
//
//            return response;
//        }
//
//
//        private String getCoursesOfStudent(String username) {
//            return students.stream()
//                    .filter(s -> s.getID().equals(username))
//                    .flatMap(s -> s.getCourses().stream())
//                    .map(course -> String.format("%s %s %d %d %s END", course.getName(), course.getTeacher().getFirstName() + course.getTeacher().getLastName(), course.getCreditUnit(), course.getAssignments().size(), (course.getHighestGradeStudent().getFirstName() + course.getHighestGradeStudent().getLastName())))
//                    .collect(Collectors.joining(" "));
//        }
//
//        private String requestJoinCourse(String username, String courseCode) {
//            Course course = courses.stream().filter(c -> c.getID().equals(courseCode)).findFirst().orElse(null);
//            Student student = students.stream().filter(s -> s.getID().equals(username)).findFirst().orElse(null);
//
//            if (course != null && student != null && canJoinCourse(student, course)) {
//                course.addStudent(student, 0.0);
//                rewrite();
//                return "JOIN_SUCCESS";
//            } else {
//                return "JOIN_FAILED";
//            }
//        }
//
//        private boolean canJoinCourse(Student student, Course course) {
//            Scanner scanner = new Scanner(System.in);
//            int currentCreditUnits = student.getCourses().stream().mapToInt(Course::getCreditUnit).sum();
//            System.out.printf("Can %s %s join %s? They have %d credit unit(s) already: (yes/no) ",
//                    student.getFirstName(), student.getLastName(), course.getName(), currentCreditUnits);
//            String response = scanner.nextLine();
//            return response.equalsIgnoreCase("yes");
//        }
//
//        private static void removeStudent(String username) {
//            students.removeIf(student -> student.getID().equals(username));
//
//            for (Course course : courses) {
//                course.removeStudentByUsername(username);
//            }
//
//            rewrite();
//        }
//
//        private void changePassword(String username, String newPassword) {
//            students.stream().filter(s -> s.getID().equals(username)).forEach(x -> x.changePassword(newPassword));
//            rewrite();
//
//        }
//
//        private void changeName(String username, String newName, String newLastName) {
//            students.stream().filter(s -> s.getID().equals(username)).forEach(x -> x.changeName(newName, newLastName));
//            rewrite();
//        }
//
//
//        private String getAverage(String username) {
//            return students.stream()
//                    .filter(s -> s.getID().equals(username))
//                    .findFirst()
//                    .map(student -> String.format("%.2f", student.getAverageGrade()))
//                    .orElse("0.00");
//        }
//
//
//        private String getSemester(String username) {
//            return String.valueOf(students.stream().filter(s -> s.getID().equals(username)).findFirst().map(Student::getSemester).get());
//        }
//
//        private String getCreditUnits(String username) {
//            return String.valueOf(students.stream().filter(s -> s.getID().equals(username)).findFirst().map(Student::getCreditUnits).get());
//        }
//
//        private String getHomePageData(String username) {
//            int numberOfAssignments = Integer.parseInt(numberOfAssignmentsOfAStudent(username));
//            int numberOfExams = Integer.parseInt(numberOfExamsOfAStudent(username));
//            double highestGrade = Double.parseDouble(highestGradeOfAStudent(username));
//            double lowestGrade = Double.parseDouble(lowestGradeOfAStudent(username));
//            String activeAssignments = listOfAssignments(username);
//            String notActiveAssignments = listOfNotActiveAssignments(username);
//
//            return String.format("HOMEPAGE_DATA %s %s %d %d %.2f %.2f %s END_ACTIVE %s",
//                    getFirstName(username), getLastName(username),
//                    numberOfAssignments, numberOfExams,
//                    highestGrade, lowestGrade,
//                    activeAssignments, notActiveAssignments);
//        }
//
//        private String getFirstName(String username) {
//            return students.stream().filter(s -> s.getID().equals(username)).findFirst().map(Student::getFirstName).orElse("");
//        }
//
//        private String getLastName(String username) {
//            return students.stream().filter(s -> s.getID().equals(username)).findFirst().map(Student::getLastName).orElse("");
//        }
//
//
//        private String getAssignments(String username) {
//            String assignments = "";
//
//            Student student = students.stream()
//                    .filter(s -> s.getID().equals(username))
//                    .findFirst()
//                    .orElse(null);
//
//            if (student == null) {
//                return "USER_NOT_FOUND";
//            }
//
//            for (Course course : student.getCourses()) {
//                for (Assignment assignment : course.getAssignments()) {
//                    assignments += " ASSIGNMENT " + getAssignmentName(assignment) + " " + assignment.daysUntilDeadline() + " " + assignment.getEstimated();
//                }
//            }
//
//            return assignments;
//        }
//
//        private String getAssignmentName(Assignment assignment) {
//            return assignment.getCourse().getName() + (assignment.getCourse().getAssignments().indexOf(assignment) + 1);
//        }
//
//
//        private String login(String[] parts) {
//            if (parts.length < 3) return "INVALID_PARAMETERS";
//            String username = parts[1];
//            String password = parts[2];
//
//            AtomicReference<Student> student = new AtomicReference<>();
//
//            boolean isValidStudent = students.stream()
//                    .anyMatch(s -> {
//                        boolean match = s.getID().equals(username) && Arrays.equals(s.getPassword(), password.toCharArray());
//                        if (match) {
//                            student.set(s);
//                        }
//                        return match;
//                    });
//
//            if (isValidStudent) {
//                Student loggedInStudent = student.get();
//                String response = "LOGIN_SUCCESS " + loggedInStudent.getFirstName() + " " + loggedInStudent.getLastName();
//                response += " " + numberOfAssignmentsOfAStudent(username);
//                response += " " + numberOfExamsOfAStudent(username);
//                response += " " + highestGradeOfAStudent(username);
//                response += " " + lowestGradeOfAStudent(username);
//                response += " " + listOfAssignments(username);
//                response += " END_ACTIVE";
//                response += " " + listOfNotActiveAssignments(username);
//                return response;
//            }
//
//            return "LOGIN_FAILED";
//        }
//
//
//        private String numberOfAssignmentsOfAStudent(String username) {
//            Long count = assignments.stream()
//                    .filter(a -> a.getCourse().getStudents().stream().anyMatch(s -> s.getID().equals(username)))
//                    .count();
//            return count.toString();
//        }
//
//        private String numberOfExamsOfAStudent(String username) {
//            Long count = students.stream()
//                    .filter(s -> s.getID().equals(username))
//                    .flatMap(s -> s.getCourses().stream())
//                    .count();
//            return count.toString();
//        }
//
//        private List<Double> getGradesOfStudent(String username) {
//            List<Course> studentCourses = new ArrayList<>();
//            List<Double> grades = new ArrayList<>();
//            Student student = students.stream()
//                    .filter(x -> x.getID().equals(username))
//                    .findFirst().orElse(null);
//            if (student == null) return null;
//            studentCourses = student.getCourses();
//            studentCourses.stream()
//                    .map(x -> x.getGrade(student))
//                    .forEach(x -> grades.add(x));
//            return grades;
//        }
//
//        private String highestGradeOfAStudent(String username) {
//            List<Double> studentGrades = getGradesOfStudent(username);
//            if (studentGrades == null) return "0.00";
//            return Collections.max(studentGrades).toString();
//        }
//
//        private String lowestGradeOfAStudent(String username) {
//            List<Double> studentGrades = getGradesOfStudent(username);
//            if (studentGrades == null) return "0.00";
//            return Collections.min(studentGrades).toString();
//        }
//
//        private String listOfAssignments(String username) {
//            List<Course> studentCourses = new ArrayList<>();
//            List<Assignment> studentAssignments = new ArrayList<>();
//            Student student = students.stream()
//                    .filter(x -> x.getID().equals(username))
//                    .findFirst().orElse(null);
//            if (student == null) return null;
//            studentCourses = student.getCourses();
//
//            studentCourses.stream()
//                    .map(x -> x.getAssignments())
//                    .forEach(x -> studentAssignments.addAll(x));
//
//            String result = "";
//            for (Assignment assignment : studentAssignments) {
//                result += assignment.getCourse().getName() + (assignment.getCourse().getAssignments().indexOf(assignment) + 1) + " ";
//            }
//
//            return result.trim();
//        }
//
//
//        private String listOfNotActiveAssignments(String username) {
//            List<Course> studentCourses = new ArrayList<>();
//            List<Assignment> studentAssignments = new ArrayList<>();
//            Student student = students.stream()
//                    .filter(x -> x.getID().equals(username))
//                    .findFirst().orElse(null);
//            if (student == null) return null;
//            studentCourses = student.getCourses();
//
//            studentCourses.stream()
//                    .map(x -> x.getAssignments())
//                    .forEach(x -> studentAssignments.addAll(x));
//
//            List<Assignment> SA = new ArrayList<>();
//            studentAssignments.stream().filter(x -> !x.isAvailable()).forEach(x -> SA.add(x));
//
//            String result = "";
//            for (Assignment assignment : SA) {
//                result += assignment.getCourse().getName() + (assignment.getCourse().getAssignments().indexOf(assignment) + 1) + " ";
//            }
//
//            return result.trim();
//        }
//    }
//}