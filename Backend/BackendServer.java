import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BackendServer {

    private static final int PORT = 12345;
    private static final String TEACHERS_FILE = "Backend/assets/teachers.txt";
    private static final String STUDENTS_FILE = "Backend/assets/students.txt";
    private static final String COURSES_FILE = "Backend/assets/courses.txt";
    private static final String ASSIGNMENTS_FILE = "Backend/assets/assignments.txt";
    private static final String PROJECTS_FILE = "Backend/assets/projects.txt";
    private static final String COURSES_MAPS_FILE = "Backend/assets/coursesMaps.txt";
    private static final String TASKS_FILE = "Backend/assets/tasks.txt";
    private static final String ASSIGNMENT_FILES_DIR = "Backend/assets/assignment_files/";

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
        loadTeachers();
        loadStudents();
        loadCourses();
        loadAssignments();
        loadProjects();
        loadCourseMaps();
    }

    private static void loadTeachers() {
        teachers = readFromFile(TEACHERS_FILE, line -> {
            try {
                String[] details = line.split(",");
                String firstName = details[0];
                String lastName = details[1];
                List<Course> teacherCourses = Arrays.stream(details[2].replace("[", "").replace("]", "").split("-"))
                        .map(id -> courses.stream().filter(course -> course != null && course.getID().equals(id)).findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                String ID = details[3];
                String password = details[4];
                return new Teacher(firstName, lastName, teacherCourses, ID, password.toCharArray());
            } catch (Exception e) {
                System.err.println("Error loading teacher: " + line);
                return null;
            }
        });
    }

    private static void loadStudents() {
        students = readFromFile(STUDENTS_FILE, line -> {
            try {
                String[] details = line.split(",");
                String firstName = details[0];
                String lastName = details[1];
                List<Course> studentCourses = Arrays.stream(details[2].replace("[", "").replace("]", "").split("-"))
                        .map(id -> courses.stream().filter(course -> course != null && course.getID().equals(id)).findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                String password = details[3];
                String ID = details[4];
                String semester = details[5];
                return new Student(firstName, lastName, studentCourses, password.toCharArray(), ID, Semester.valueOf(semester));
            } catch (Exception e) {
                System.err.println("Error loading student: " + line);
                return null;
            }
        });
    }

    private static void loadCourses() {
        courses = readFromFile(COURSES_FILE, line -> {
            try {
                String[] details = line.split(",");
                String name = details[0];
                Integer creditUnit = Integer.parseInt(details[1]);
                List<Student> courseStudents = Arrays.stream(details[2].replace("[", "").replace("]", "").split("-"))
                        .map(id -> students.stream().filter(student -> student != null && student.getID().equals(id)).findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                List<Assignment> courseAssignments = Arrays.stream(details[3].replace("[", "").replace("]", "").split("-"))
                        .map(id -> assignments.stream().filter(assignment -> assignment != null && assignment.getID().equals(id)).findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                List<Project> courseProjects = Arrays.stream(details[4].replace("[", "").replace("]", "").split("-"))
                        .map(id -> projects.stream().filter(project -> project != null && project.getID().equals(id)).findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                boolean isAvailable = Boolean.parseBoolean(details[5]);
                String examDate = details[6];
                Semester semester = Semester.valueOf(details[7]);
                Teacher teacher = teachers.stream().filter(t -> t != null && t.getID().equals(details[8])).findFirst().orElse(null);
                String ID = details[9];
                return new Course(name, creditUnit, courseStudents, courseProjects, courseAssignments, isAvailable, examDate, semester, teacher, ID);
            } catch (Exception e) {
                System.err.println("Error loading course: " + line);
                return null;
            }
        });
    }

    private static void loadAssignments() {
        assignments = readFromFile(ASSIGNMENTS_FILE, line -> {
            try {
                String[] details = line.split(",");
                LocalDate deadline = LocalDate.parse(details[0]);
                boolean isAvailable = Boolean.parseBoolean(details[1]);
                String courseID = details[2];
                String ID = details[3];
                String estimated = details[4];
                Course course = courses.stream().filter(c -> c != null && c.getID().equals(courseID)).findFirst().orElse(null);
                if (course != null) {
                    Assignment assignment = new Assignment(deadline, isAvailable, course, ID, estimated);
                    course.addAssignment(assignment);
                    return assignment;
                }
                return null;
            } catch (Exception e) {
                System.err.println("Error loading assignment: " + line);
                return null;
            }
        });
    }

    private static void loadProjects() {
        projects = readFromFile(PROJECTS_FILE, line -> {
            try {
                String[] details = line.split(",");
                LocalDate deadline = LocalDate.parse(details[0]);
                boolean isAvailable = Boolean.parseBoolean(details[1]);
                String courseID = details[2];
                String ID = details[3];
                String name = details[4];
                String estimated = details[5];
                Course course = courses.stream().filter(c -> c != null && c.getID().equals(courseID)).findFirst().orElse(null);
                if (course != null) {
                    Project project = new Project(deadline, isAvailable, course, ID, name, estimated);
                    course.addProject(project);
                    return project;
                }
                return null;
            } catch (Exception e) {
                System.err.println("Error loading project: " + line);
                return null;
            }
        });
    }

    private static void loadCourseMaps() {
        Path courseMapsPath = Paths.get(COURSES_MAPS_FILE);
        List<String> courseMapsDetails;
        try {
            courseMapsDetails = Files.readAllLines(courseMapsPath);
        } catch (IOException ignored) {
            return;
        }

        for (String s : courseMapsDetails) {
            try {
                String[] details = s.split(",");
                String courseID = details[0];
                Course course = courses.stream().filter(c -> c != null && c.getID().equals(courseID)).findFirst().orElse(null);
                if (course != null) {
                    for (int i = 1; i < details.length; i++) {
                        String[] studentGrade = details[i].split(":");
                        String studentID = studentGrade[0];
                        double grade = Double.parseDouble(studentGrade[1]);
                        Student student = students.stream().filter(st -> st != null && st.getID().equals(studentID)).findFirst().orElse(null);
                        if (student != null) {
                            course.addStudent(student, grade);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading course map: " + s);
            }
        }
    }

    private static <T> List<T> readFromFile(String filePath, Function<String, T> mapper) {
        Path path = Paths.get(filePath);
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException ignored) {
            return new ArrayList<>();
        }
        return lines.stream().map(mapper).filter(Objects::nonNull).collect(Collectors.toList());
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
            System.out.println("SocketServer started on port " + PORT);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client accepted");
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
                    out.println(response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String signup(String[] parts) {
            if (parts.length < 5) return "INVALID_PARAMETERS";
            String firstName = parts[1];
            String lastName = parts[2];
            String username = parts[3];
            String password = parts[4];

            boolean exists = students.stream().anyMatch(s -> s.getID().equals(username)) ||
                    teachers.stream().anyMatch(t -> t.getID().equals(username));
            if (!exists) {
                Student student = new Student(firstName, lastName, new ArrayList<>(), password.toCharArray(), username, Semester.FIRST);
                students.add(student);
                rewrite();
                return "SIGNUP_SUCCESS";
            }
            return "USER_ALREADY_EXISTS";
        }

        private String processRequest(String request) {
            String[] parts = request.split(" ");
            String command = parts[0];
            String response = "";

            switch (command) {
                case "GET_TEACHERS":
                    response = getTeachers();
                    break;
                case "GET_STUDENTS":
                    response = getStudents();
                    break;
                case "GET_COURSES":
                    response = getCourses();
                    break;
                case "GET_ASSIGNMENTS":
                    response = getAssignments(parts[1]);
                    break;
                case "GET_PROJECTS":
                    response = getProjects();
                    break;
                case "ADD_TEACHER":
                    response = addTeacher(parts);
                    break;
                case "ADD_STUDENT":
                    response = addStudent(parts);
                    break;
                case "ADD_COURSE":
                    response = addCourse(parts);
                    break;
                case "ADD_ASSIGNMENT":
                    response = addAssignment(parts);
                    break;
                case "ADD_PROJECT":
                    response = addProject(parts);
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
                case "UPLOAD_ASSIGNMENT":
                    response = uploadAssignment(parts);
                    break;
                case "GET_INFOPAGE_DATA":
                    response = "INFOPAGE " + getAverage(parts[1]) + " " + getSemester(parts[1]) + " " + getCreditUnits(parts[1]);
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
                    System.out.println(request);
                    response = getCoursesOfStudent(parts[1]);
                    break;
                case "REQUEST_JOIN_COURSE":
                    System.out.println(request);
                    response = requestJoinCourse(parts[1], parts[2]);
                    break;
                default:
                    response = "UNKNOWN_COMMAND";
                    break;
            }

            return response;
        }


        private String getCoursesOfStudent(String username) {
            return students.stream()
                    .filter(s -> s.getID().equals(username))
                    .flatMap(s -> s.getCourses().stream())
                    .map(course -> String.format("%s %d %d %s END", course.getName(), course.getCreditUnit(), course.getAssignments().size(), (course.getHighestGradeStudent().getFirstName() + course.getHighestGradeStudent().getLastName())))
                    .collect(Collectors.joining(" "));
        }

        private String requestJoinCourse(String username, String courseCode) {
            Course course = courses.stream().filter(c -> c.getID().equals(courseCode)).findFirst().orElse(null);
            Student student = students.stream().filter(s -> s.getID().equals(username)).findFirst().orElse(null);

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
            int currentCreditUnits = student.getCourses().stream().mapToInt(Course::getCreditUnit).sum();
            System.out.printf("Can %s %s join %s? They have %d credit unit(s) already: (yes/no) ",
                    student.getFirstName(), student.getLastName(), course.getName(), currentCreditUnits);
            String response = scanner.nextLine();
            return response.equalsIgnoreCase("yes");
        }

        private static void removeStudent(String username) {
            students.removeIf(student -> student.getID().equals(username));

            for (Course course : courses) {
                course.removeStudentByUsername(username);
            }

            rewrite();
        }

        private void changePassword(String username, String newPassword) {
            students.stream().filter(s -> s.getID().equals(username)).forEach(x -> x.changePassword(newPassword));
            rewrite();

        }

        private void changeName(String username, String newName, String newLastName) {
            students.stream().filter(s -> s.getID().equals(username)).forEach(x -> x.changeName(newName, newLastName));
            rewrite();
        }


        private String getAverage(String username) {
            return students.stream()
                    .filter(s -> s.getID().equals(username))
                    .findFirst()
                    .map(student -> String.format("%.2f", student.getAverageGrade()))
                    .orElse("0.00");
        }


        private String getSemester(String username) {
            return String.valueOf(students.stream().filter(s -> s.getID().equals(username)).findFirst().map(Student::getSemester).get());
        }

        private String getCreditUnits(String username) {
            return String.valueOf(students.stream().filter(s -> s.getID().equals(username)).findFirst().map(Student::getCreditUnits).get());
        }

        private String getHomePageData(String username) {
            int numberOfAssignments = Integer.parseInt(numberOfAssignmentsOfAStudent(username));
            int numberOfExams = Integer.parseInt(numberOfExamsOfAStudent(username));
            double highestGrade = Double.parseDouble(highestGradeOfAStudent(username));
            double lowestGrade = Double.parseDouble(lowestGradeOfAStudent(username));
            String activeAssignments = listOfAssignments(username);
            String notActiveAssignments = listOfNotActiveAssignments(username);

            return String.format("HOMEPAGE_DATA %s %s %d %d %.2f %.2f %s END_ACTIVE %s",
                    getFirstName(username), getLastName(username),
                    numberOfAssignments, numberOfExams,
                    highestGrade, lowestGrade,
                    activeAssignments, notActiveAssignments);
        }

        private String getFirstName(String username) {
            return students.stream().filter(s -> s.getID().equals(username)).findFirst().map(Student::getFirstName).orElse("");
        }

        private String getLastName(String username) {
            return students.stream().filter(s -> s.getID().equals(username)).findFirst().map(Student::getLastName).orElse("");
        }

        private String getCoursesOfAStudent(String username) {
            return students.stream()
                    .filter(s -> s.getID().equals(username))
                    .flatMap(s -> s.getCourses().stream())
                    .map(course -> {
                        String courseName = course.getName();
                        int credits = course.getCreditUnit();
                        long activeAssignments = course.getAssignments().stream().filter(Assignment::isAvailable).count();
                        String highestGradeStudent = course.getStudents().stream()
                                .max((s1, s2) -> Double.compare(s1.getGrades().stream().mapToDouble(Double::doubleValue).max().orElse(0.0),
                                        s2.getGrades().stream().mapToDouble(Double::doubleValue).max().orElse(0.0)))
                                .map(Student::getFirstName)
                                .orElse("");
                        return String.format("%s %d %d %s", courseName, credits, activeAssignments, highestGradeStudent);
                    })
                    .collect(Collectors.joining("\n"));
        }

        private String uploadAssignment(String[] parts) {
            if (parts.length < 3) return "INVALID_PARAMETERS";
            String assignmentId = parts[1];
            String filePath = parts[2];
            File file = new File(ASSIGNMENT_FILES_DIR + assignmentId);
            try (FileInputStream fis = new FileInputStream(filePath);
                 FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                return "UPLOAD_SUCCESS";
            } catch (IOException e) {
                return "UPLOAD_FAILED";
            }
        }

        private String getTeachers() {
            return teachers.stream().map(Teacher::toString).collect(Collectors.joining("\n"));
        }

        private String getStudents() {
            return students.stream().map(Student::toString).collect(Collectors.joining("\n"));
        }

        private String getCourses() {
            return courses.stream().map(Course::toString).collect(Collectors.joining("\n"));
        }

        private String getAssignments(String username) {
            StringBuilder assignmentsBuilder = new StringBuilder();

            Student student = students.stream()
                    .filter(s -> s.getID().equals(username))
                    .findFirst()
                    .orElse(null);

            if (student == null) {
                return "USER_NOT_FOUND";
            }

            for (Course course : student.getCourses()) {
                for (Assignment assignment : course.getAssignments()) {
                    assignmentsBuilder.append(" ASSIGNMENT ")
                            .append(getAssignmentName(assignment)).append(" ")
                            .append(assignment.daysUntilDeadline()).append(" ")
                            .append(assignment.getEstimated()).append(" ");
                }
            }

            return assignmentsBuilder.toString().trim();
        }

        private String getAssignmentName(Assignment assignment) {
            return assignment.getCourse().getName() + (assignment.getCourse().getAssignments().indexOf(assignment) + 1);
        }

        private String getProjects() {
            return projects.stream().map(Project::toString).collect(Collectors.joining("\n"));
        }

        private String addTeacher(String[] parts) {
            if (parts.length < 6) return "INVALID_PARAMETERS";
            String firstName = parts[1];
            String lastName = parts[2];
            List<Course> teacherCourses = Arrays.stream(parts[3].split("-"))
                    .map(id -> courses.stream().filter(course -> course != null && course.getID().equals(id)).findFirst().orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            String ID = parts[4];
            String password = parts[5];

            boolean exists = teachers.stream().anyMatch(t -> t.getID().equals(ID));
            if (!exists) {
                Teacher teacher = new Teacher(firstName, lastName, teacherCourses, ID, password.toCharArray());
                teachers.add(teacher);
                rewrite();
                return "TEACHER_ADDED";
            }
            return "TEACHER_ALREADY_EXISTS";
        }

        private String addStudent(String[] parts) {
            if (parts.length < 7) return "INVALID_PARAMETERS";
            String firstName = parts[1];
            String lastName = parts[2];
            List<Course> studentCourses = Arrays.stream(parts[3].split("-"))
                    .map(id -> courses.stream().filter(course -> course != null && course.getID().equals(id)).findFirst().orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            String password = parts[4];
            String ID = parts[5];
            String semester = parts[6];

            boolean exists = students.stream().anyMatch(s -> s.getID().equals(ID));
            if (!exists) {
                Student student = new Student(firstName, lastName, studentCourses, password.toCharArray(), ID, Semester.valueOf(semester));
                students.add(student);
                rewrite();
                return "STUDENT_ADDED";
            }
            return "STUDENT_ALREADY_EXISTS";
        }

        private String addCourse(String[] parts) {
            if (parts.length < 10) return "INVALID_PARAMETERS";
            String name = parts[1];
            int creditUnit = Integer.parseInt(parts[2]);
            List<Student> courseStudents = Arrays.stream(parts[3].split("-"))
                    .map(id -> students.stream().filter(student -> student != null && student.getID().equals(id)).findFirst().orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            List<Assignment> courseAssignments = Arrays.stream(parts[4].split("-"))
                    .map(id -> assignments.stream().filter(assignment -> assignment != null && assignment.getID().equals(id)).findFirst().orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            List<Project> courseProjects = Arrays.stream(parts[5].split("-"))
                    .map(id -> projects.stream().filter(project -> project != null && project.getID().equals(id)).findFirst().orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            boolean isAvailable = Boolean.parseBoolean(parts[6]);
            String examDate = parts[7];
            Semester semester = Semester.valueOf(parts[8]);
            Teacher teacher = teachers.stream().filter(t -> t != null && t.getID().equals(parts[9])).findFirst().orElse(null);
            String ID = parts[10];

            boolean exists = courses.stream().anyMatch(c -> c.getID().equals(ID));
            if (!exists) {
                Course course = new Course(name, creditUnit, courseStudents, courseProjects, courseAssignments, isAvailable, examDate, semester, teacher, ID);
                courses.add(course);
                rewrite();
                return "COURSE_ADDED";
            }
            return "COURSE_ALREADY_EXISTS";
        }

        private String addAssignment(String[] parts) {
            if (parts.length < 5) return "INVALID_PARAMETERS";
            LocalDate deadline = LocalDate.parse(parts[1]);
            boolean isAvailable = Boolean.parseBoolean(parts[2]);
            String courseID = parts[3];
            String ID = parts[4];
            String estimated = parts[5];

            Course course = courses.stream().filter(c -> c != null && c.getID().equals(courseID)).findFirst().orElse(null);
            if (course != null) {
                Assignment assignment = new Assignment(deadline, isAvailable, course, ID, estimated);
                assignments.add(assignment);
                course.addAssignment(assignment);
                rewrite();
                return "ASSIGNMENT_ADDED";
            }
            return "COURSE_NOT_FOUND";
        }

        private String addProject(String[] parts) {
            if (parts.length < 6) return "INVALID_PARAMETERS";
            LocalDate deadline = LocalDate.parse(parts[1]);
            boolean isAvailable = Boolean.parseBoolean(parts[2]);
            String courseID = parts[3];
            String ID = parts[4];
            String name = parts[5];
            String estimated = parts[6];

            Course course = courses.stream().filter(c -> c != null && c.getID().equals(courseID)).findFirst().orElse(null);
            if (course != null) {
                Project project = new Project(deadline, isAvailable, course, ID, name, estimated);
                projects.add(project);
                course.addProject(project);
                rewrite();
                return "PROJECT_ADDED";
            }
            return "COURSE_NOT_FOUND";
        }

        private String login(String[] parts) {
            if (parts.length < 3) return "INVALID_PARAMETERS";
            String username = parts[1];
            String password = parts[2];

            AtomicReference<Student> student = new AtomicReference<>();

            boolean isValidStudent = students.stream()
                    .anyMatch(s -> {
                        boolean match = s.getID().equals(username) && Arrays.equals(s.getPassword(), password.toCharArray());
                        if (match) {
                            student.set(s);
                        }
                        return match;
                    });

            if (isValidStudent) {
                Student loggedInStudent = student.get();
                String response = "LOGIN_SUCCESS " + loggedInStudent.getFirstName() + " " + loggedInStudent.getLastName();
                response += " " + numberOfAssignmentsOfAStudent(username);
                response += " " + numberOfExamsOfAStudent(username);
                response += " " + highestGradeOfAStudent(username);
                response += " " + lowestGradeOfAStudent(username);
                response += " " + listOfAssignments(username);
                response += " END_ACTIVE";
                response += " " + listOfNotActiveAssignments(username);
                return response;
            }

            return "LOGIN_FAILED";
        }

        private String numberOfAssignmentsOfAStudent(String username) {
            long count = assignments.stream().filter(a -> a.getCourse().getStudents().stream().anyMatch(s -> s.getID().equals(username))).count();
            return String.valueOf(count);
        }

        private String numberOfExamsOfAStudent(String username) {
            long count = students.stream()
                    .filter(s -> s.getID().equals(username))
                    .flatMap(s -> s.getCourses().stream())
                    .count();
            return String.valueOf(count);
        }

        private String highestGradeOfAStudent(String username) {
            double highestGrade = courses.stream()
                    .flatMap(c -> c.getStudents().stream())
                    .filter(s -> s.getID().equals(username))
                    .mapToDouble(s -> s.getGrades().stream().mapToDouble(Double::doubleValue).max().orElse(0.0))
                    .max().orElse(0.0);
            return String.valueOf(highestGrade);
        }

        private String lowestGradeOfAStudent(String username) {
            double lowestGrade = courses.stream()
                    .flatMap(c -> c.getStudents().stream())
                    .filter(s -> s.getID().equals(username))
                    .mapToDouble(s -> s.getGrades().stream().mapToDouble(Double::doubleValue).min().orElse(0.0))
                    .min().orElse(0.0);
            return String.valueOf(lowestGrade);
        }

        private String listOfAssignments(String username) {
            List<String> result = assignments.stream()
                    .filter(a -> a.isAvailable() && a.getCourse().getStudents().stream().anyMatch(s -> s.getID().equals(username)))
                    .map(a -> a.getCourse().getName() + "" + (a.getCourse().getAssignments().indexOf(a) + 1))
                    .collect(Collectors.toList());
            return String.join(" ", result);
        }

        private String listOfNotActiveAssignments(String username) {
            return assignments.stream()
                    .filter(a -> !a.isAvailable() && a.getCourse().getStudents().stream().anyMatch(s -> s.getID().equals(username)))
                    .map(Assignment::toString)
                    .collect(Collectors.joining(" "));
        }
    }
}