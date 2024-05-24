import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;


public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
                        System.out.print("Password: ");
                        String password = scanner.nextLine();

                        Teacher teacher = new Teacher(first_name, last_name, new ArrayList<>(), ID, password.toCharArray());
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
                        if (result == null) System.out.println("Teacher removed successfully!");
                        else System.out.println(result);
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
        System.out.println("17) exit");

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