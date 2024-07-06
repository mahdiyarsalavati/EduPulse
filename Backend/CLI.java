import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CLI {

    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final String TEACHERS_FILE = "Backend/assets/teachers.txt";
    private static final String STUDENTS_FILE = "Backend/assets/students.txt";
    private static final String COURSES_FILE = "Backend/assets/courses.txt";
    private static final String ASSIGNMENTS_FILE = "Backend/assets/assignments.txt";
    private static final String PROJECTS_FILE = "Backend/assets/projects.txt";
    private static final String COURSES_MAPS_FILE = "Backend/assets/coursesMaps.txt";

    private static List<Teacher> teachers = new ArrayList<>();
    private static List<Student> students = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();
    private static List<Assignment> assignments = new ArrayList<>();
    private static List<Project> projects = new ArrayList<>();

    private static final FileUtils<Teacher> fileUtilsTeacher = new FileUtils<>();
    private static final FileUtils<Student> fileUtilsStudent = new FileUtils<>();
    private static final FileUtils<Course> fileUtilsCourse = new FileUtils<>();
    private static final FileUtils<Assignment> fileUtilsAssignment = new FileUtils<>();
    private static final FileUtils<Project> fileUtilsProject = new FileUtils<>();

    static {
        loadTeachers();
        loadStudents();
        loadCourses();
        loadAssignments();
        loadProjects();
        loadCourseMaps();
    }

    private static void reload() {
        loadTeachers();
        loadStudents();
        loadCourses();
        loadAssignments();
        loadProjects();
        loadCourseMaps();
    }


    private static void loadTeachers() {
        teachers.clear();
        Path teachersPath = Paths.get(TEACHERS_FILE);
        List<String> teachersDetails = new ArrayList<>();
        try {
            teachersDetails = Files.readAllLines(teachersPath);
        } catch (IOException ignored) {
        }
        for (String s : teachersDetails) {
            String[] details = s.split(",");
            String firstName = details[0];
            String lastName = details[1];
            String coursesIDs1 = details[2].replace("[", "");
            coursesIDs1 = coursesIDs1.replace("]", "");
            String[] coursesIDs = coursesIDs1.split("-");
            List<Course> teacherCourses = new ArrayList<>();
            for (String cid : coursesIDs) {
                for (Course course : courses) {
                    if (course.getID().equals(cid)) {
                        teacherCourses.add(course);
                    }
                }
            }
            String ID = details[3];
            String password = details[4];
            Teacher teacher = new Teacher(firstName, lastName, teacherCourses, ID, password.toCharArray());
            teachers.add(teacher);
        }
    }

    private static void loadStudents() {
        students.clear();
        Path studentsPath = Paths.get(STUDENTS_FILE);
        List<String> studentsDetails = new ArrayList<>();
        try {
            studentsDetails = Files.readAllLines(studentsPath);
        } catch (IOException ignored) {
        }
        for (String s : studentsDetails) {
            String[] details = s.split(",");
            String firstName = details[0];
            String lastName = details[1];
            String coursesIDs1 = details[2].replace("[", "");
            coursesIDs1 = coursesIDs1.replace("]", "");
            String[] coursesIDs = coursesIDs1.split("-");
            List<Course> studentCourses = new ArrayList<>();
            for (String cid : coursesIDs) {
                for (Course course : courses) {
                    if (course.getID().equals(cid)) {
                        studentCourses.add(course);
                    }
                }
            }
            String password = details[3];
            String ID = details[4];
            String semester = details[5];
            Student student = new Student(firstName, lastName, studentCourses, password.toCharArray(), ID, Semester.valueOf(semester));
            students.add(student);
        }
    }

    private static void loadCourses() {
        courses.clear();
        Path coursesPath = Paths.get(COURSES_FILE);
        List<String> coursesDetails = new ArrayList<>();
        try {
            coursesDetails = Files.readAllLines(coursesPath);
        } catch (IOException ignored) {
            return;
        }

        for (String s : coursesDetails) {
            String[] details = s.split(",");
            String name = details[0];
            Integer creditUnit = Integer.parseInt(details[1]);

            String[] studentsIDs = details[2].replace("[", "").replace("]", "").split("-");
            List<Student> courseStudents = new ArrayList<>();
            for (String studentID : studentsIDs) {
                Student student = students.stream().filter(st -> st.getID().equals(studentID)).findFirst().orElse(null);
                if (student != null) {
                    courseStudents.add(student);
                }
            }

            String[] assignmentsIDs = details[3].replace("[", "").replace("]", "").split("-");
            List<Assignment> courseAssignments = new ArrayList<>();
            for (String assignmentID : assignmentsIDs) {
                Assignment assignment = assignments.stream().filter(a -> a.getID().equals(assignmentID)).findFirst().orElse(null);
                if (assignment != null) {
                    courseAssignments.add(assignment);
                }
            }

            String[] projectsIDs = details[4].replace("[", "").replace("]", "").split("-");
            List<Project> courseProjects = new ArrayList<>();
            for (String projectID : projectsIDs) {
                Project project = projects.stream().filter(p -> p.getID().equals(projectID)).findFirst().orElse(null);
                if (project != null) {
                    courseProjects.add(project);
                }
            }

            boolean isAvailable = Boolean.parseBoolean(details[5]);
            String examDate = details[6];
            Semester semester = Semester.valueOf(details[7]);
            Teacher teacher = teachers.stream().filter(t -> t.getID().equals(details[8])).findFirst().orElse(null);
            String ID = details[9];

            Course course = new Course(name, creditUnit, courseStudents, courseProjects, courseAssignments, isAvailable, examDate, semester, teacher, ID);
            courses.add(course);
        }
    }


    private static void loadAssignments() {
        assignments.clear();
        Path assignmentsPath = Paths.get(ASSIGNMENTS_FILE);
        List<String> assignmentsDetails = new ArrayList<>();
        try {
            assignmentsDetails = Files.readAllLines(assignmentsPath);
        } catch (IOException ignored) {
            return;
        }

        for (String s : assignmentsDetails) {
            String[] details = s.split(",");
            LocalDate deadline = LocalDate.parse(details[0]);
            boolean isAvailable = Boolean.parseBoolean(details[1]);
            String courseID = details[2];
            String ID = details[3];
            String estimated = details[4];
            Course course = courses.stream().filter(c -> c.getID().equals(courseID)).findFirst().orElse(null);
            if (course != null) {
                Assignment assignment = new Assignment(deadline, isAvailable, course, ID, estimated);
                assignments.add(assignment);
                course.addAssignment(assignment);
            }
        }
    }

    private static void loadProjects() {
        projects.clear();
        Path projectsPath = Paths.get(PROJECTS_FILE);
        List<String> projectsDetails = new ArrayList<>();
        try {
            projectsDetails = Files.readAllLines(projectsPath);
        } catch (IOException ignored) {
            return;
        }

        for (String s : projectsDetails) {
            String[] details = s.split(",");
            LocalDate deadline = LocalDate.parse(details[0]);
            boolean isAvailable = Boolean.parseBoolean(details[1]);
            String courseID = details[2];
            String ID = details[3];
            String name = details[4];
            String estimated = details[5];
            Course course = courses.stream().filter(c -> c.getID().equals(courseID)).findFirst().orElse(null);
            if (course != null) {
                Project project = new Project(deadline, isAvailable, course, ID, name, estimated);
                projects.add(project);
                course.addProject(project);
            }
        }
    }

    private static void loadCourseMaps() {
        Path courseMapsPath = Paths.get(COURSES_MAPS_FILE);
        List<String> courseMapsDetails = new ArrayList<>();
        try {
            courseMapsDetails = Files.readAllLines(courseMapsPath);
        } catch (IOException ignored) {
            return;
        }

        for (String s : courseMapsDetails) {
            String[] details = s.split(",");
            String courseID = details[0];
            Course course = courses.stream().filter(c -> c.getID().equals(courseID)).findFirst().orElse(null);
            if (course != null) {
                for (int i = 1; i < details.length; i++) {
                    String[] studentGrade = details[i].split(":");
                    String studentID = studentGrade[0];
                    double grade = Double.parseDouble(studentGrade[1]);
                    Student student = students.stream().filter(st -> st.getID().equals(studentID)).findFirst().orElse(null);
                    if (student != null) {
                        course.addStudent(student, grade);
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        if (teachers == null) {
            teachers = new ArrayList<>();
        }

        Admin admin = new Admin("admin", "admin", new ArrayList<>(), "admin".toCharArray(), teachers);

        clearScreen();
        displayWelcomeMessage();

        int role = getValidatedInput(scanner, RED, 1, 2);

        clearScreen();

        if (role == 1) {
            System.out.print("Enter your Teacher ID: ");
            String teacherID = scanner.next();
            Teacher teacher = admin.findTeacherByID(teacherID);
            if (teacher != null) {
                handleTeacher(scanner, admin, teacherID);
            } else {
                System.out.println(RED + "Teacher not found.");
            }
        } else {
            handleAdmin(scanner, console, admin, teachers);
        }

        scanner.close();
    }

    private static void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    private static void displayWelcomeMessage() {
        System.out.println(BLUE + "Welcome to the CLI interface!");
        System.out.println(GREEN + "Choose your role:\n1) Teacher\n2) Admin");
    }

    private static void handleTeacher(Scanner scanner, Admin admin, String teacherID) throws FileNotFoundException {
        Teacher teacher = admin.findTeacherByID(teacherID);
        if (teacher != null) {
            boolean exit = false;
            while (!exit) {
                int input = showTeacherMenu(scanner, GREEN);
                clearScreen();
                switch (input) {
                    case 1 -> addCourse(scanner, admin);
                    case 2 -> removeCourse(scanner, admin);
                    case 3 -> addStudent(scanner, admin);
                    case 4 -> removeStudent(scanner, admin);
                    case 5 -> addStudentToCourse(scanner, admin);
                    case 6 -> removeStudentFromCourse(scanner, admin);
                    case 7 -> addAssignment(scanner, admin);
                    case 8 -> removeAssignment(scanner, admin);
                    case 9 -> addProject(scanner, admin);
                    case 10 -> removeProject(scanner, admin);
                    case 11 -> rewardStudent(scanner, admin);
                    case 12 -> gradeStudent(scanner, admin);
                    case 13 -> extendAssignmentDeadline(scanner, admin);
                    case 14 -> extendProjectDeadline(scanner, admin);
                    case 15 -> activateAssignment(scanner, admin);
                    case 16 -> activateProject(scanner, admin);
                    case 17 -> exit = true;

                }
                if (!exit) clearScreen();
            }
        } else {
            System.out.println(RED + "Teacher not found.");
        }
    }

    private static void handleAdmin(Scanner scanner, Console console, Admin admin, List<Teacher> teachers) throws FileNotFoundException {
        boolean exit = false;
        while (!exit) {
            int input = showAdminMenu(scanner, YELLOW);
            clearScreen();
            switch (input) {
                case 1 -> addTeacher(scanner, console, admin);
                case 2 -> removeTeacher(scanner, admin);
                case 3 -> addCourse(scanner, admin);
                case 4 -> removeCourse(scanner, admin);
                case 5 -> addStudent(scanner, admin);
                case 6 -> removeStudent(scanner, admin);
                case 7 -> addStudentToCourse(scanner, admin);
                case 8 -> removeStudentFromCourse(scanner, admin);
                case 9 -> addAssignment(scanner, admin);
                case 10 -> removeAssignment(scanner, admin);
                case 11 -> addProject(scanner, admin);
                case 12 -> removeProject(scanner, admin);
                case 13 -> rewardStudent(scanner, admin);
                case 14 -> gradeStudent(scanner, admin);
                case 15 -> extendAssignmentDeadline(scanner, admin);
                case 16 -> extendProjectDeadline(scanner, admin);
                case 17 -> activateAssignment(scanner, admin);
                case 18 -> activateProject(scanner, admin);
                case 19 -> exit = true;
            }
            if (!exit) clearScreen();
        }
    }

    private static int getValidatedInput(Scanner scanner, String color, int min, int max) {
        int result = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                result = scanner.nextInt();
                if (result >= min && result <= max) {
                    validInput = true;
                } else {
                    System.out.println(color + "Please enter a valid number:");
                }
            } catch (InputMismatchException e) {
                System.out.println(color + "Invalid input. Please enter a number:");
                scanner.nextLine();
            }
        }
        return result;
    }

    private static int showAdminMenu(Scanner scanner, String color) {
        System.out.println(color + "Admin Dashboard:");
        System.out.println("1) add teacher");
        System.out.println("2) remove teacher");
        System.out.println("3) add course");
        System.out.println("4) remove course");
        System.out.println("5) add student");
        System.out.println("6) remove student");
        System.out.println("7) add student to a course");
        System.out.println("8) remove student from a course");
        System.out.println("9) add assignment");
        System.out.println("10) remove assignment");
        System.out.println("11) add project");
        System.out.println("12) remove project");
        System.out.println("13) reward student");
        System.out.println("14) grade student");
        System.out.println("15) extend deadline of assignment");
        System.out.println("16) extend deadline of project");
        System.out.println("17) activate assignment");
        System.out.println("18) activate project");
        System.out.println("19) exit");

        return getValidatedInput(scanner, color, 1, 19);
    }

    private static int showTeacherMenu(Scanner scanner, String color) {
        System.out.println(color + "Teacher Dashboard:");
        System.out.println("1) add course");
        System.out.println("2) remove course");
        System.out.println("3) add student");
        System.out.println("4) remove student");
        System.out.println("5) add student to a course");
        System.out.println("6) remove student from a course");
        System.out.println("7) add assignment");
        System.out.println("8) remove assignment");
        System.out.println("9) add project");
        System.out.println("10) remove project");
        System.out.println("11) reward student");
        System.out.println("12) grade student");
        System.out.println("13) extend deadline of assignment");
        System.out.println("14) extend deadline of project");
        System.out.println("15) activate assignment");
        System.out.println("16) activate project");
        System.out.println("17) exit");

        return getValidatedInput(scanner, color, 1, 17);
    }

    private static void addTeacher(Scanner scanner, Console console, Admin admin) {
        reload();
        System.out.println(YELLOW + "Adding a teacher: ");
        System.out.print("First Name: ");
        String firstName = scanner.next();
        System.out.print("Last Name: ");
        String lastName = scanner.next();
        System.out.print("Username (ID): ");
        String ID = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();

        boolean exists = teachers.stream().anyMatch(t -> t.getID().equals(ID));
        if (!exists) {
            Teacher teacher = new Teacher(firstName, lastName, new ArrayList<>(), ID, password.toCharArray());
            teachers.add(teacher);
            System.out.println(GREEN + "Teacher added successfully!");
            rewrite();
        } else {
            System.out.println(RED + "A teacher with this ID already exists.");
        }
    }


    private static void removeTeacher(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Removing a teacher:");
        if (teachers.isEmpty()) {
            System.out.println(RED + "No teachers available to remove.");
            return;
        }

        System.out.println("Select a teacher to remove from the list:");
        displayList(teachers);
        int teacherIndex = getValidatedInput(scanner, RED, 1, teachers.size()) - 1;
        Teacher teacherToRemove = teachers.remove(teacherIndex);

        courses.forEach(course -> {
            if (course.getTeacher().equals(teacherToRemove)) {
                course.setTeacher(null);
            }
        });

        System.out.println(GREEN + "Teacher removed successfully.");
        rewrite();
    }


    private static void addCourse(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Adding a course: ");
        System.out.print("Name: ");
        String name = scanner.next();
        System.out.print("Choose the teacher: ");
        if (teachers.isEmpty()) return;

        displayList(teachers);
        scanner.nextLine();
        int chosenTeacherNum = scanner.nextInt();
        Teacher chosenTeacher = teachers.get(chosenTeacherNum - 1);

        System.out.print("Credit Unit: ");
        int creditUnit = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Is Available? 1) Yes 2) No: ");
        boolean isAvailable = scanner.nextInt() == 1;
        scanner.nextLine();

        System.out.print("Exam Date: ");
        String examDate = scanner.nextLine();
        System.out.print("Semester: ");
        Semester semester = Semester.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("ID: ");
        String ID = scanner.nextLine();

        boolean exists = courses.stream().anyMatch(c -> c.getID().equals(ID));
        if (!exists) {
            Course course = new Course(name, creditUnit, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), isAvailable, examDate, semester, chosenTeacher, ID);
            courses.add(course);
            System.out.println(GREEN + "Course added successfully!");
            rewrite();
        } else {
            System.out.println(RED + "A course with this ID already exists.");
        }
    }


    private static void removeCourse(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Removing a course: ");
        if (courses.isEmpty()) {
            System.out.println(RED + "No courses available to remove.");
            return;
        }

        displayList(courses);
        System.out.print("Choose the course by entering the number: ");
        int choice;
        choice = getValidatedInput(scanner, RED, 1, courses.size());

        int index = choice - 1;

        Course courseToRemove = courses.get(index);
        removeCourseFromAllObjects(courseToRemove);
        courses.remove(index);
        System.out.println(GREEN + "Course removed successfully!");
        rewrite();
    }


    private static void addStudent(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Adding a student: ");
        System.out.print("First Name: ");
        String firstName = scanner.next();
        System.out.print("Last Name: ");
        String lastName = scanner.next();
        System.out.print("ID: ");
        String ID = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        scanner.nextLine();
        System.out.print("Semester: ");
        Semester semester = Semester.valueOf(scanner.nextLine().toUpperCase());

        boolean exists = students.stream().anyMatch(s -> s.getID().equals(ID));
        if (!exists) {
            Student student = new Student(firstName, lastName, new ArrayList<>(), password.toCharArray(), ID, semester);
            admin.getStudents().add(student);
            students.add(student);
            System.out.println(GREEN + "Student added successfully!");
            rewrite();
        } else {
            System.out.println(RED + "A student with this ID already exists.");
        }
    }

    private static void removeStudent(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Removing a student:");
        if (students.isEmpty()) {
            System.out.println(RED + "No students available.");
            return;
        }

        System.out.println("Select a student from the list:");
        displayList(students);
        int studentIndex = getValidatedInput(scanner, RED, 1, students.size()) - 1;
        Student studentToRemove = students.remove(studentIndex);
        System.out.println(GREEN + "Student removed successfully.");
        rewrite();
    }


    private static void addAssignment(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Adding an assignment: ");
        System.out.print("Deadline: (from now by days): ");
        int n = scanner.nextInt();
        LocalDate deadline = LocalDate.now().plusDays(n);
        System.out.print("Is Available? 1) Yes 2) No: ");
        boolean isAvailable = scanner.nextInt() == 1;
        scanner.nextLine();
        System.out.print("ID: ");
        String ID = scanner.nextLine();
        System.out.print("Estimated time to get this done (in hrs): ");
        String estimated = scanner.nextLine();

        boolean exists = assignments.stream().anyMatch(a -> a.getID().equals(ID));
        if (!exists) {
            System.out.print("Choose the course by entering the number: ");
            displayList(courses);
            int chosenCourseNum = getValidatedInput(scanner, RED, 1, courses.size());
            Course chosenCourse = courses.get(chosenCourseNum - 1);

            Assignment assignment = new Assignment(deadline, isAvailable, chosenCourse, ID, estimated);
            assignments.add(assignment);
            chosenCourse.addAssignment(assignment);
            System.out.println(GREEN + "Assignment added successfully!");
            rewrite();
        } else {
            System.out.println(RED + "An assignment with this ID already exists.");
        }
    }


    private static void removeAssignment(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Removing an assignment:");
        if (assignments.isEmpty()) {
            System.out.println(RED + "No assignments available.");
            return;
        }

        System.out.println("Select an assignment from the list:");
        displayList(assignments);
        int assignmentIndex = getValidatedInput(scanner, RED, 1, assignments.size()) - 1;
        Assignment assignmentToRemove = assignments.remove(assignmentIndex);
        System.out.println(GREEN + "Assignment removed successfully.");
        rewrite();
    }


    private static void addProject(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Adding a project: ");
        System.out.print("Name: ");
        String name = scanner.next();
        System.out.print("Deadline: (from now by days): ");
        int n = scanner.nextInt();
        LocalDate deadline = LocalDate.now().plusDays(n);
        System.out.print("Is Available? 1) Yes 2) No: ");
        boolean isAvailable = scanner.nextInt() == 1;
        scanner.nextLine();
        System.out.print("ID: ");
        String ID = scanner.nextLine();
        System.out.print("Estimated time to done this (in hrs): ");
        String estimated = scanner.nextLine();

        boolean exists = projects.stream().anyMatch(p -> p.getID().equals(ID));
        if (!exists) {
            System.out.print("Choose the course by entering the number: ");
            displayList(courses);
            int chosenCourseNum = getValidatedInput(scanner, RED, 1, courses.size());
            Course chosenCourse = courses.get(chosenCourseNum - 1);

            Project project = new Project(deadline, isAvailable, chosenCourse, ID, name, estimated);
            projects.add(project);
            chosenCourse.addProject(project);
            System.out.println(GREEN + "Project added successfully!");
            rewrite();
        } else {
            System.out.println(RED + "A project with this ID already exists.");
        }
    }


    private static void removeProject(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Removing a project:");
        if (projects.isEmpty()) {
            System.out.println(RED + "No projects available.");
            return;
        }

        System.out.println("Select a project from the list:");
        displayList(projects);
        int projectIndex = getValidatedInput(scanner, RED, 1, projects.size()) - 1;
        Project projectToRemove = projects.remove(projectIndex);
        System.out.println(GREEN + "Project removed successfully.");
        rewrite();
    }


    private static void rewardStudent(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Rewarding a student:");
        if (courses.isEmpty()) {
            System.out.println(RED + "No courses available.");
            return;
        }

        System.out.println("Select a course from the list:");
        displayList(courses);
        int courseIndex = getValidatedInput(scanner, RED, 1, courses.size()) - 1;
        Course selectedCourse = courses.get(courseIndex);

        System.out.println("Select a student from the course:");
        displayList(selectedCourse.getStudents());
        int studentIndex = getValidatedInput(scanner, RED, 1, selectedCourse.getStudents().size()) - 1;
        Student selectedStudent = selectedCourse.getStudents().get(studentIndex);

        selectedCourse.gradeStudent(selectedStudent, selectedCourse.getGrade(selectedStudent) + 1.0);

        System.out.println(GREEN + "Student rewarded successfully with 5 points.");
        rewrite();
    }


    private static void gradeStudent(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Grading a student:");
        if (courses.isEmpty()) {
            System.out.println(RED + "No courses available.");
            return;
        }

        System.out.println("Select a course from the list:");
        displayList(courses);
        int courseIndex = getValidatedInput(scanner, RED, 1, courses.size()) - 1;
        Course selectedCourse = courses.get(courseIndex);

        System.out.println("Select a student from the course:");
        displayList(selectedCourse.getStudents());
        int studentIndex = getValidatedInput(scanner, RED, 1, selectedCourse.getStudents().size()) - 1;
        Student selectedStudent = selectedCourse.getStudents().get(studentIndex);

        System.out.println("Enter the grade for the student:");
        double grade = scanner.nextDouble();

        selectedCourse.gradeStudent(selectedStudent, grade);

        System.out.println(GREEN + "Student graded successfully with a grade of " + grade + ".");
        rewrite();
    }


    private static void extendAssignmentDeadline(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Extending an assignment's deadline:");
        if (assignments.isEmpty()) {
            System.out.println(RED + "No assignments available.");
            return;
        }

        System.out.println("Select an assignment from the list:");
        displayList(assignments);
        int assignmentIndex = getValidatedInput(scanner, RED, 1, assignments.size()) - 1;
        System.out.println("Enter the number of days to extend the deadline:");
        int days = scanner.nextInt();
        Assignment selectedAssignment = assignments.get(assignmentIndex);
        selectedAssignment.extendDeadlineByDays(days);
        System.out.println(GREEN + "Deadline extended successfully.");
        rewrite();
    }


    private static void extendProjectDeadline(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Extending a project's deadline:");
        if (projects.isEmpty()) {
            System.out.println(RED + "No projects available.");
            return;
        }

        System.out.println("Select a project from the list:");
        displayList(projects);
        int projectIndex = getValidatedInput(scanner, RED, 1, projects.size()) - 1;
        System.out.println("Enter the number of days to extend the deadline:");
        int days = scanner.nextInt();
        Project selectedProject = projects.get(projectIndex);
        selectedProject.extendDeadlineByDays(days);
        System.out.println(GREEN + "Deadline extended successfully.");
        rewrite();
    }


    private static void activateAssignment(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Activating an assignment:");
        if (assignments.isEmpty()) {
            System.out.println(RED + "No assignments available for activation.");
            return;
        }

        System.out.println("Select an assignment to activate from the list:");
        displayList(assignments);
        int assignmentIndex = getValidatedInput(scanner, RED, 1, assignments.size()) - 1;
        Assignment selectedAssignment = assignments.get(assignmentIndex);
        selectedAssignment.setAvailable(true);
        System.out.println(GREEN + "Assignment activated successfully.");
        rewrite();
    }


    private static void activateProject(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Activating a project:");
        if (projects.isEmpty()) {
            System.out.println(RED + "No projects available for activation.");
            return;
        }

        System.out.println("Select a project to activate from the list:");
        displayList(projects);
        int projectIndex = getValidatedInput(scanner, RED, 1, projects.size()) - 1;
        Project selectedProject = projects.get(projectIndex);
        selectedProject.setAvailable(true);
        System.out.println(GREEN + "Project activated successfully.");
        rewrite();
    }


    private static void addStudentToCourse(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Adding a student to a course:");

        if (students.isEmpty()) {
            System.out.println(RED + "No students available.");
            return;
        }

        System.out.println("Select a student from the list:");
        displayList(students);
        int studentIndex = getValidatedInput(scanner, RED, 1, students.size()) - 1;
        Student selectedStudent = students.get(studentIndex);

        if (courses.isEmpty()) {
            System.out.println(RED + "No courses available.");
            return;
        }

        System.out.println("Select a course from the list:");
        displayList(courses);
        int courseIndex = getValidatedInput(scanner, RED, 1, courses.size()) - 1;
        Course selectedCourse = courses.get(courseIndex);

        if (!selectedStudent.getCourses().contains(selectedCourse)) {
            selectedCourse.addStudent(selectedStudent);
            selectedStudent.addCourse(selectedCourse);
            Teacher courseTeacher = selectedCourse.getTeacher();
            if (courseTeacher != null) {
                courseTeacher.addCourse(selectedCourse);
            }
            System.out.println(GREEN + "Student added to the course successfully!");
        } else {
            System.out.println(RED + "Student is already enrolled in this course.");
        }

        rewrite();
    }


    private static void removeStudentFromCourse(Scanner scanner, Admin admin) {
        reload();
        System.out.println(YELLOW + "Removing a student from a course: ");
        if (courses.isEmpty()) {
            System.out.println(RED + "No courses available.");
            return;
        }

        System.out.println("Select a course from the list:");
        displayList(courses);
        int courseIndex = getValidatedInput(scanner, RED, 1, courses.size()) - 1;
        Course selectedCourse = courses.get(courseIndex);

        if (selectedCourse.getStudents().isEmpty()) {
            System.out.println(RED + "No students enrolled in this course.");
            return;
        }

        System.out.println("Select a student from the course to remove:");
        displayList(selectedCourse.getStudents());
        int studentIndex = getValidatedInput(scanner, RED, 1, selectedCourse.getStudents().size()) - 1;
        Student selectedStudent = selectedCourse.getStudents().get(studentIndex);

        selectedCourse.removeStudent(selectedStudent);
        System.out.println(GREEN + "Student removed from the course successfully.");
        rewrite();
    }


    private static void displayList(List<?> list) {
        int index = 1;
        for (Object item : list) {
            if (item instanceof Course) {
                Course course = (Course) item;
                System.out.println(index++ + ") " + course.getName() + ", ID: " + course.getID());
            } else if (item instanceof Teacher) {
                Teacher teacher = (Teacher) item;
                System.out.println(index++ + ") " + teacher.getFirstName() + " " + teacher.getLastName() + ", ID: " + teacher.getID());
            } else if (item instanceof Assignment) {
                Assignment assignment = (Assignment) item;
                System.out.println(index++ + ") " + "ID: " + assignment.getID());
            } else if (item instanceof Project) {
                Project project = (Project) item;
                System.out.println(index++ + ") " + project.getName() + ", ID: " + project.getID());
            } else if (item instanceof Student) {
                Student student = (Student) item;
                System.out.println(index++ + ") " + student.getFirstName() + " " + student.getLastName() + ", ID: " + student.getID());
            } else {
                System.out.println(index++ + ") " + item.toString());
            }
        }
    }


    private static class FileUtils<T> {
        protected void writeAll(List<T> objects, String filePath) {
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                for (T object : objects) {
                    fileWriter.write(object.toString() + "\n");
                }
            } catch (IOException ignored) {
            }
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

    private static void rewrite() {
        fileUtilsTeacher.writeAll(teachers, TEACHERS_FILE);
        fileUtilsStudent.writeAll(students, STUDENTS_FILE);
        fileUtilsCourse.writeAll(courses, COURSES_FILE);
        fileUtilsAssignment.writeAll(assignments, ASSIGNMENTS_FILE);
        fileUtilsProject.writeAll(projects, PROJECTS_FILE);
        writeCourseMaps();
    }

    private static void removeCourseFromAllObjects(Course course) {
        for (Teacher teacher : teachers) {
            teacher.getCourses().remove(course);
        }

        assignments.removeIf(assignment -> assignment.getCourse().equals(course));

        projects.removeIf(project -> project.getCourse().equals(course));
        for (Student student : students) {
            student.getCourses().remove(course);
        }
        rewrite();
    }

}



