import java.io.*;
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
    private static List<Teacher> teachers = new ArrayList<>();
    private static List<Student> students = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();

    static {
        loadTeachers();
        loadStudents();
    }

    private static void loadTeachers() {
        Path teachersPath = Paths.get(TEACHERS_FILE);
        List<String> teachersDetails = new ArrayList<>();
        try {
            teachersDetails = Files.readAllLines(teachersPath);
        } catch (IOException io) {
        }
        for (String s : teachersDetails) {
            String[] details = s.split(",");
            String firstName = details[0];
            String lastName = details[1];
            String coursesIDs1 = details[2].replace("[", "");
            coursesIDs1 = coursesIDs1.replace("]", "");
            String[] coursesIDs = coursesIDs1.split(",");
            List<Course> teacherCourses = new ArrayList<>();
            loadCourses();
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
        Path studentsPath = Paths.get(STUDENTS_FILE);
        List<String> studentsDetails = new ArrayList<>();
        try {
            studentsDetails = Files.readAllLines(studentsPath);
        } catch (IOException io) {
        }
        for (String s : studentsDetails) {
            String[] details = s.split(",");
            String firstName = details[0];
            String lastName = details[1];
            String coursesIDs1 = details[2].replace("[", "");
            coursesIDs1 = coursesIDs1.replace("]", "");
            String[] coursesIDs = coursesIDs1.split(",");
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
        Path coursesPath = Paths.get(COURSES_FILE);
        List<String> coursesDetails = new ArrayList<>();
        try {
            coursesDetails = Files.readAllLines(coursesPath);
        } catch (IOException io) {
        }
        for (String s : coursesDetails) {
            String[] details = s.split(",");
            String name = details[0];
            Integer creditUnit = Integer.parseInt(details[1]);
            /////////
            String coursesIDs1 = details[2].replace("[", "");
            coursesIDs1 = coursesIDs1.replace("]", "");
            String[] coursesIDs = coursesIDs1.split(",");
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


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        List<Teacher> teachers = readSerializedFileTeacher("teachers.ser");
        if (teachers == null) {
            teachers = new ArrayList<>();
        }

        Admin admin = new Admin("admin", "admin", new ArrayList<>(), "admin".toCharArray(), teachers);

        clearScreen();
        displayWelcomeMessage();

        int role = getRole(scanner);

        clearScreen();

        if (role == 1) {
            System.out.print("Enter your Teacher ID: ");
            String teacherID = scanner.next();
            Teacher teacher = admin.findTeacherByID(teacherID);
            if (teacher != null) {
                handleTeacher(scanner, admin);
            } else {
                System.out.println(RED + "Teacher not found.");
            }
        } else {
            handleAdmin(scanner, console, admin, teachers);
        }

        scanner.close();
        writeSerializedFile("teachers.ser", teachers);
    }

    private static void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    private static void displayWelcomeMessage() {
        System.out.println(BLUE + "Welcome to the CLI interface!");
        System.out.println(GREEN + "Choose your role:\n1) Teacher\n2) Admin");
    }

    private static int getRole(Scanner scanner) {
        return getValidatedInput(scanner, RED, 1, 2);
    }

    private static void handleTeacher(Scanner scanner, Admin admin) {
        boolean exit = false;
        while (!exit) {
            int input = showTeacherMenu(scanner, GREEN);
            clearScreen();
            switch (input) {
                case 1 -> addCourse(scanner, admin);
                case 2 -> removeCourse(scanner, admin);
                case 3 -> addStudent(scanner, admin);
                case 4 -> removeStudent(scanner, admin);
                case 5 -> addAssignment(scanner, admin);
                case 6 -> removeAssignment(scanner, admin);
                case 7 -> addProject(scanner, admin);
                case 8 -> removeProject(scanner, admin);
                case 9 -> rewardStudent(scanner, admin);
                case 10 -> gradeStudent(scanner, admin);
                case 11 -> extendAssignmentDeadline(scanner, admin);
                case 12 -> extendProjectDeadline(scanner, admin);
                case 13 -> activateAssignment(scanner, admin);
                case 14 -> activateProject(scanner, admin);
                case 15 -> addStudentToCourse(scanner, admin);
                case 16 -> exit = true;
            }
            if (!exit) clearScreen();
        }
    }

    private static void handleAdmin(Scanner scanner, Console console, Admin admin, List<Teacher> teachers) {
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
                case 17 -> addStudentToCourse(scanner, admin);
                case 18 -> exit = true;
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
        System.out.println("17) add student to a course");
        System.out.println("18) exit");

        return getValidatedInput(scanner, color, 1, 18);
    }

    private static int showTeacherMenu(Scanner scanner, String color) {
        System.out.println(color + "Teacher Dashboard:");
        System.out.println("1) add course");
        System.out.println("2) remove course");
        System.out.println("3) add student");
        System.out.println("4) remove student");
        System.out.println("5) add assignment");
        System.out.println("6) remove assignment");
        System.out.println("7) add project");
        System.out.println("8) remove project");
        System.out.println("9) reward student");
        System.out.println("10) grade student");
        System.out.println("11) extend deadline of assignment");
        System.out.println("12) extend deadline of project");
        System.out.println("13) activate assignment");
        System.out.println("14) activate project");
        System.out.println("15) add student to a course");
        System.out.println("16) exit");

        return getValidatedInput(scanner, color, 1, 15);
    }

    private static void addTeacher(Scanner scanner, Console console, Admin admin) {
        System.out.println(YELLOW + "Adding a teacher: ");
        System.out.print("First Name: ");
        String firstName = scanner.next();
        System.out.print("Last Name: ");
        String lastName = scanner.next();
        System.out.print("Username: ");
        String ID = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();

        Teacher teacher = new Teacher(firstName, lastName, new ArrayList<>(), ID, password.toCharArray());
        admin.addTeacher(teacher);
        writeSerializedFile("teachers.ser", admin.getTeachers());

        System.out.println(GREEN + "Teacher added successfully!");
    }

    private static void removeTeacher(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Removing a teacher: ");
        System.out.print("Username: ");
        String ID = scanner.next();
        String result = admin.removeTeacherByID(ID);

        if (result == null) {
            System.out.println(GREEN + "Teacher removed successfully!");
            writeSerializedFile("teachers.ser", admin.getTeachers());
        } else {
            System.out.println(RED + result);
        }
    }

    private static void addCourse(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Adding a course: ");
        System.out.print("Name: ");
        String name = scanner.next();

        System.out.print("Choose the teacher: ");
        List<Teacher> teachers = readSerializedFileTeacher("teachers.ser");
        if (teachers == null) return;

        displayList(teachers);
        scanner.nextLine();
        int chosenTeacherNum = scanner.nextInt();
        Teacher chosenTeacher = teachers.get(chosenTeacherNum - 1);

        System.out.print("Credit Unit: ");
        int creditUnit = scanner.nextInt();
        System.out.print("Is Available? 1) Yes 2) No: ");
        boolean isAvailable = scanner.nextInt() == 1;
        scanner.nextLine();  // Consume newline
        System.out.print("Exam Date: ");
        String examDate = scanner.nextLine();
        System.out.print("Semester: ");
        Semester semester = Semester.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("ID: ");
        String ID = scanner.nextLine();
        Course course = new Course(name, creditUnit, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), isAvailable, examDate, semester, chosenTeacher, ID);

        admin.addCourse(course);
        chosenTeacher.addCourse(course);
        writeSerializedFile("teachers.ser", teachers);

        System.out.println(GREEN + "Course added successfully!");
    }

    private static void removeCourse(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Removing a course: ");
        System.out.print("Choose the course by entering the ID: ");
        List<Course> courses = readSerializedFileCourse("courses.ser");
        if (courses == null) return;

        displayList(courses);
        scanner.nextLine();
        int chosenCourseNum = scanner.nextInt();
        Course chosenCourse = courses.get(chosenCourseNum - 1);
        String result = admin.removeCourseByID(chosenCourse.getID());

        if (result == null) {
            System.out.println(GREEN + "Course removed successfully!");
            writeSerializedFile("courses.ser", admin.getCourses());
        } else {
            System.out.println(RED + result);
        }
    }

    private static void addStudent(Scanner scanner, Admin admin) {
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

        Student student = new Student(firstName, lastName, new ArrayList<>(), password.toCharArray(), ID, semester);
        admin.getStudents().add(student);
        writeSerializedFile("students.ser", admin.getStudents());

        System.out.println(GREEN + "Student added successfully!");
    }

    private static void removeStudent(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Removing a student: ");
        System.out.print("ID: ");
        String ID = scanner.next();
        String result = admin.removeStudentByID(ID);

        if (result == null) {
            System.out.println(GREEN + "Student removed successfully!");
            writeSerializedFile("students.ser", admin.getStudents());
        } else {
            System.out.println(RED + result);
        }
    }

    private static void addAssignment(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Adding an assignment: ");
        System.out.print("Deadline: (from now by days): ");
        int n = scanner.nextInt();
        LocalDate deadline = LocalDate.now().plusDays(n);
        System.out.print("Is Available? 1) Yes 2) No: ");
        boolean isAvailable = scanner.nextInt() == 1;
        scanner.nextLine();  // Consume newline
        System.out.print("ID: ");
        String ID = scanner.nextLine();
        System.out.print("Choose the course by entering the ID: ");
        List<Course> courses = readSerializedFileCourse("courses.ser");
        if (courses == null) return;

        displayList(courses);
        scanner.nextLine();
        int chosenCourseNum = scanner.nextInt();
        Course chosenCourse = courses.get(chosenCourseNum - 1);

        Assignment assignment = new Assignment(deadline, isAvailable, chosenCourse, ID);
        chosenCourse.addAssignment(assignment);
        admin.addAssignmentAdmin(assignment);
        writeSerializedFile("assignments.ser", admin.getAssignments());

        System.out.println(GREEN + "Assignment added successfully!");
    }

    private static void removeAssignment(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Removing an assignment: ");
        System.out.print("ID: ");
        String ID = scanner.next();
        String result = admin.removeAssignmentByID(ID);

        if (result == null) {
            System.out.println(GREEN + "Assignment removed successfully!");
            writeSerializedFile("assignments.ser", admin.getAssignments());
        } else {
            System.out.println(RED + result);
        }
    }

    private static void addProject(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Adding a project: ");
        System.out.print("Name: ");
        String name = scanner.next();
        System.out.print("Deadline: (from now by days): ");
        int n = scanner.nextInt();
        LocalDate deadline = LocalDate.now().plusDays(n);
        System.out.print("Is Available? 1) Yes 2) No: ");
        boolean isAvailable = scanner.nextInt() == 1;
        scanner.nextLine();  // Consume newline
        System.out.print("ID: ");
        String ID = scanner.nextLine();
        System.out.print("Choose the course by entering the ID: ");
        List<Course> courses = readSerializedFileCourse("courses.ser");
        if (courses == null) return;

        displayList(courses);
        scanner.nextLine();
        int chosenCourseNum = scanner.nextInt();
        Course chosenCourse = courses.get(chosenCourseNum - 1);

        Project project = new Project(deadline, isAvailable, chosenCourse, ID, name);
        chosenCourse.addProject(project);
        admin.addProject(chosenCourse, project);
        admin.addProjectAdmin(project);
        writeSerializedFile("projects.ser", admin.getProjects());

        System.out.println(GREEN + "Project added successfully!");
    }

    private static void removeProject(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Removing a project: ");
        System.out.print("ID: ");
        String ID = scanner.next();
        String result = admin.removeProjectByID(ID);

        if (result == null) {
            System.out.println(GREEN + "Project removed successfully!");
            writeSerializedFile("projects.ser", admin.getProjects());
        } else {
            System.out.println(RED + result);
        }
    }

    private static void rewardStudent(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Rewarding a student: ");
        System.out.print("Student ID: ");
        String studentID = scanner.next();
        System.out.print("Course ID: ");
        String courseID = scanner.next();
        Student student = admin.findStudentByID(studentID);
        Course course = admin.findCourseByID(courseID);

        if (student != null && course != null) {
            admin.rewardStudent(course, student, 1);
            System.out.println(GREEN + "Student rewarded successfully!");
        } else {
            if (student == null) System.out.println(RED + "Student not found.");
            if (course == null) System.out.println(RED + "Course not found.");
        }
    }

    private static void gradeStudent(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Grading a student: ");
        System.out.print("Student ID: ");
        String studentID = scanner.next();
        System.out.print("Course ID: ");
        String courseID = scanner.next();
        System.out.print("Grade: ");
        double grade = scanner.nextDouble();
        Student student = admin.findStudentByID(studentID);
        Course course = admin.findCourseByID(courseID);

        if (student != null && course != null) {
            admin.gradeStudent(course, student, grade);
            System.out.println(GREEN + "Student graded successfully!");
        } else {
            if (student == null) System.out.println(RED + "Student not found.");
            if (course == null) System.out.println(RED + "Course not found.");
        }
    }

    private static void extendAssignmentDeadline(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Extending an assignment's deadline: ");
        System.out.print("ID: ");
        String ID = scanner.next();
        System.out.print("Days to extend: ");
        int days = scanner.nextInt();
        Assignment assignment = admin.findAssignmentByID(ID);

        if (assignment != null) {
            assignment.extendDeadlineByDays(days);
            System.out.println(GREEN + "Assignment deadline extended successfully!");
        } else {
            System.out.println(RED + "Assignment not found.");
        }
    }

    private static void extendProjectDeadline(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Extending a project's deadline: ");
        System.out.print("ID: ");
        String ID = scanner.next();
        System.out.print("Days to extend: ");
        int days = scanner.nextInt();
        Project project = admin.findProjectByID(ID);

        if (project != null) {
            project.extendDeadlineByDays(days);
            System.out.println(GREEN + "Project deadline extended successfully!");
        } else {
            System.out.println(RED + "Project not found.");
        }
    }

    private static void activateAssignment(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Activating an assignment: ");
        System.out.print("ID: ");
        String ID = scanner.next();
        Assignment assignment = admin.findAssignmentByID(ID);

        if (assignment != null) {
            assignment.setAvailable(true);
            System.out.println(GREEN + "Assignment activated successfully!");
        } else {
            System.out.println(RED + "Assignment not found.");
        }
    }

    private static void activateProject(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Activating a project: ");
        System.out.print("ID: ");
        String ID = scanner.next();
        Project project = admin.findProjectByID(ID);

        if (project != null) {
            project.setAvailable(true);
            System.out.println(GREEN + "Project activated successfully!");
        } else {
            System.out.println(RED + "Project not found.");
        }
    }

    private static void addStudentToCourse(Scanner scanner, Admin admin) {
        System.out.println(YELLOW + "Adding a student to a course: ");
        System.out.print("Student ID: ");
        String studentID = scanner.next();
        System.out.print("Course ID: ");
        String courseID = scanner.next();
        Course course = admin.findCourseByID(courseID);
        Student student = admin.findStudentByID(studentID);

        if (course != null && student != null) {
            admin.addStudent(course, student);
            System.out.println(GREEN + "Student added to the course successfully!");
        } else {
            if (course == null) System.out.println(RED + "Course not found.");
            if (student == null) System.out.println(RED + "Student not found.");
        }
    }

    private static List<Teacher> readSerializedFileTeacher(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Teacher>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(RED + "Failed to read from file: " + e.getMessage());
            return null;
        }
    }

    private static List<Course> readSerializedFileCourse(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(RED + "Failed to read from file: " + e.getMessage());
            return null;
        }
    }

    private static void writeSerializedFile(String fileName, List<?> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println(RED + "Failed to write to file: " + e.getMessage());
        }
    }

    private static void displayList(List<?> list) {
        int i = 1;
        for (Object obj : list) {
            if (obj != null) {
                System.out.println(i + " " + obj.toString());
                i++;
            }
        }
    }
}
