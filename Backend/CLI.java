import java.awt.*;
import java.io.Console;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        String BLACK = "\u001B[30m";
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        String BLUE = "\u001B[34m";
        String PURPLE = "\u001B[35m";
        String CYAN = "\u001B[36m";
        String WHITE = "\u001B[37m";
        final String CLEAR_SCREEN = "\033[H\033[2J";

        System.out.print(CLEAR_SCREEN);
        System.out.flush();

        System.out.println(BLUE + "Welcome to the CLI interface!");
        System.out.print(GREEN + "Choose your role:" + "\n" + "1) Teacher" + "\n" + "2) Admin" + "\n");

        int role = getAnInt(scanner, RED);

        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        Admin admin = new Admin("admin", "admin", null, "admin".toCharArray(), new ArrayList<>());
        if (role == 1) {
            int input = showTeacherMenu(scanner, GREEN);
        } else {
            boolean exit = false;
            while (!exit) {
                int input = showAdminMenu(scanner, YELLOW);
                System.out.print(CLEAR_SCREEN);
                switch (input) {
                    case 1:
                        System.out.println(YELLOW + "Adding a teacher: ");
                        System.out.print("First Name: ");
                        String first_name = scanner.nextLine();
                        System.out.print("Last Name: ");
                        String last_name = scanner.nextLine();
                        System.out.print("Username: ");
                        String ID = scanner.nextLine();
                        char[] password = console.readPassword("Password: ");

                        Teacher teacher = new Teacher(first_name, last_name, new ArrayList<>(), ID, password);
                        admin.addTeacher(teacher);

                        try (FileWriter writer = new FileWriter("teachers.txt", true)) {
                            writer.write(teacher.toString() + "\n");
                            System.out.println(GREEN + "Teacher added successfully!");
                        } catch (IOException e) {
                            System.out.println(RED + "Failed to write to file");
                        }
                        break;

                    case 2:
                        System.out.println(YELLOW + "removing a teacher: ");
                        System.out.print("Username: ");
                        ID = scanner.nextLine();
                        String result = admin.removeTeacherByID(ID);
                        if (result == null) System.out.println(GREEN + "Teacher removed successfully!");
                        else System.out.println(result);

                        // deleting teacher from teachers file
                        if (result == null) {
                            List<String> lines = new ArrayList<>();
                            try {
                                lines = Files.readAllLines(Paths.get("teachers.txt"));
                            } catch (IOException e) {
                                System.out.println(RED + "Failed to read from file: " + e.getMessage());
                                break;
                            }

                            List<String> updatedLines = new ArrayList<>();
                            boolean found = false;
                            for (String line : lines) {
                                if (!line.contains(ID)) {
                                    updatedLines.add(line);
                                }
                            }

                            try (FileWriter writer = new FileWriter("teachers.txt", false)) {
                                for (String line : updatedLines) {
                                    writer.write(line + "\n");
                                }
                            } catch (IOException e) {
                                System.out.println(RED + "Failed to write to file: " + e.getMessage());
                            }
                        }
                        break;
                    case 3:
                        System.out.println(YELLOW + "adding a course: ");
                        System.out.print("Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Choose the teacher: ");
                        List<String> lines = new ArrayList<>();
                        try {
                            lines = Files.readAllLines(Paths.get("teachers.txt"));
                        } catch (IOException e) {
                            System.out.println(RED + "Failed to read from file: " + e.getMessage());
                            break;
                        }
                        int i = 1;
                        for (String s : lines) {
                            System.out.println(i + s.split(" ")[1] + " " + s.split(" ")[2] + " " + s.split(" ")[3]);
                        }
                        int chosenTeacherNum = scanner.nextInt();
                        String chosenTeacherID = lines.get(chosenTeacherNum - 1).split(" ")[3].substring(3);
                        Teacher chosenTeacher = admin.findTeacherByID(chosenTeacherID);

                        System.out.print("Credit Unit: ");
                        int creditUnit = scanner.nextInt();
                        System.out.print("Is Available? 1) Yes 2) No");
                        boolean isAvailable = scanner.nextInt() == 1;
                        System.out.println("Exam Date: ");
                        String examDate = scanner.nextLine();
                        System.out.println("Semester: ");
                        Semester semester = Semester.valueOf(scanner.nextLine().toUpperCase());
                        System.out.println("ID: ");
                        ID = scanner.nextLine();
                        Course course = new Course(name, creditUnit, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), isAvailable, examDate, semester, chosenTeacher, ID);

                        try (FileWriter writer = new FileWriter("courses.txt", true)) {
                            writer.write(course.toString() + "\n");
                            System.out.println(GREEN + "Course added successfully!");
                        } catch (IOException e) {
                            System.out.println(RED + "Failed to write to file");
                        }

                        admin.addCourse(course);
                        chosenTeacher.addCourse(course);
                        break;

                    case 4:
                        System.out.println(YELLOW + "removing a course: ");
                        System.out.print("ID: ");
                        ID = scanner.nextLine();
                        result = admin.removeCourseByID(ID);
                        if (result == null) System.out.println(GREEN + "Course removed successfully!");
                        else System.out.println(result);

                        // deleting course from teachers file
                        if (result == null) {
                            lines = new ArrayList<>();
                            try {
                                lines = Files.readAllLines(Paths.get("courses.txt"));
                            } catch (IOException e) {
                                System.out.println(RED + "Failed to read from file: " + e.getMessage());
                                break;
                            }

                            List<String> updatedLines = new ArrayList<>();
                            boolean found = false;
                            for (String line : lines) {
                                if (!line.contains(ID)) {
                                    updatedLines.add(line);
                                }
                            }

                            try (FileWriter writer = new FileWriter("courses.txt", false)) {
                                for (String line : updatedLines) {
                                    writer.write(line + "\n");
                                }
                            } catch (IOException e) {
                                System.out.println(RED + "Failed to write to file: " + e.getMessage());
                            }
                        }

                        break;

                    case 5:
                        System.out.println(YELLOW + "adding a student: ");
                        System.out.print("First Name: ");
                        first_name = scanner.nextLine();
                        System.out.print("Last Name: ");
                        last_name = scanner.nextLine();
                        System.out.print("ID: ");
                        ID = scanner.nextLine();
                        password = console.readPassword("Password: ");
                        System.out.println("Semester: ");
                        semester = Semester.valueOf(scanner.nextLine());

                        Student student = new Student(first_name, last_name, new ArrayList<>(), password, ID, semester);
                        admin.getStudents().add(student);

                        try (FileWriter writer = new FileWriter("students.txt", true)) {
                            writer.write(student.toString() + "\n");
                            System.out.println(GREEN + "Student added successfully!");
                        } catch (IOException e) {
                            System.out.println(RED + "Failed to write to file");
                        }
                        break;
                }
                System.out.println(CLEAR_SCREEN);
                showAdminMenu(scanner, YELLOW);
            }
        }

        scanner.close();
    }

    private static int getAnInt(Scanner scanner, String color) {
        int result = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                result = scanner.nextInt();
                if (result == 1 || result == 2) {
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

        return getAnInt(scanner, color);
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
        System.out.println("15) exit");
        return getAnInt(scanner, color);
    }
}