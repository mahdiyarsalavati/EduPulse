import java.awt.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.awt.Color.*;

public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        final String CLEAR_SCREEN = "\033[H\033[2J";

        System.out.print(CLEAR_SCREEN);
        System.out.flush();

        System.out.println(BLUE + "Welcome to the CLI interface!");
        System.out.print(GREEN + "Choose your role:" + "\n" + "1) Teacher" + "\n" + "2) Admin" + "\n");

        int role = 0;
        role = getAnInt(scanner);

        System.out.print(CLEAR_SCREEN);
        System.out.flush();

        if (role == 1) {
            showTeacherMenu(GREEN);
        } else {
            showAdminMenu(YELLOW);
        }

        scanner.close();
    }

    private static int getAnInt(Scanner scanner) {
        int result = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                result = scanner.nextInt();
                if (result == 1 || result == 2) {
                    validInput = true;
                } else {
                    System.out.println(RED + "Please enter a valid number:");
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Invalid input. Please enter a number:");
                scanner.nextLine();
            }
        }
        return result;
    }

    private static void showAdminMenu(Color color) {
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
        System.out.println("15) active assignment");
        System.out.println("16) active project");
    }

    private static void showTeacherMenu(Color color) {
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
        System.out.println("13) active assignment");
        System.out.println("14) active project");
    }
}