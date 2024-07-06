import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Teacher teacher = new Teacher("Bob", "Anderson", new ArrayList<>(), "402243010", "1234".toCharArray());

        Student student1 = new Student("Ali", "Alavi", new ArrayList<>(), "1234".toCharArray(), "402243104", Semester.SECOND);
        Student student2 = new Student("Taghi", "Taghavi", new ArrayList<>(), "1234".toCharArray(), "402243001", Semester.FIRST);

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        Course course1 = new Course("Math", 3, students, new ArrayList<>(), new ArrayList<>(), true, "2024-05-01", Semester.FIRST, teacher, "4021");
        Course course2 = new Course("English", 3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, "2024-05-01", Semester.SECOND, teacher, "4021");

        teacher.addCourse(course1);
        teacher.addCourse(course2);

        teacher.addStudent(course2, student1);

        Assignment assignment1 = new Assignment(LocalDate.now().plusDays(7), true, course1, "40221", "3");
        Assignment assignment2 = new Assignment(LocalDate.now().plusDays(10), false, course2, "40222", "3");

        teacher.addAssignment(course1, assignment1);
        teacher.addAssignment(course2, assignment2);

        Project project1 = new Project(LocalDate.now().plusDays(7), true, course1, "40223", "Math Project", "3");
        Project project2 = new Project(LocalDate.now().plusDays(10), false, course2, "40224", "English Project", "3");

        teacher.addProject(course1, project1);
        teacher.addProject(course2, project2);

        teacher.gradeStudent(course1, student1, 18.5);
        teacher.gradeStudent(course1, student2, 17);
        teacher.gradeStudent(course2, student1, 20);

        student1.printCourses();
        student2.printCourses();

        student1.printAverageGrade();
        student2.printAverageGrade();

        student1.printCreditUnits();
        student2.printCreditUnits();

        student1.setSemester(Semester.SECOND);
        student2.setSemester(Semester.FIRST);

        System.out.println(student1);
        System.out.println(student2);
    }
}
