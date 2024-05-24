import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {
    private Teacher teacher;
    private Course course1;
    private Course course2;
    private Student student1, student2;
    private Assignment assignment;

    @BeforeEach
    void setup() {
        teacher = new Teacher("Bob", "Anderson", new ArrayList<>(), "402243010", "1234".toCharArray());
        course1 = new Course("Test Course1", 3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, "2024-05-01", Semester.SECOND, teacher, "4021");
        course2 = new Course("Test Course2", 4, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, "2024-05-02", Semester.SECOND, teacher, "4022");
        teacher.addCourse(course1);
        teacher.addCourse(course2);
        student1 = new Student("Test", "Student1", new ArrayList<>(), "1234".toCharArray(), "402243104", Semester.SECOND);
        student2 = new Student("Test", "Student2", new ArrayList<>(), "1234".toCharArray(), "402243001", Semester.SECOND);
        assignment = new Assignment(LocalDate.now().plusDays(7), true, course1);
    }

    @Test
    public void addAssignmentTest() {
        teacher.addAssignment(course1, assignment);
        assertTrue(course1.getAssignments().contains(assignment));
    }

    @Test
    public void removeAssignmentTest() {
        teacher.addAssignment(course1, assignment);
        assertEquals(1, course1.getAssignmentsLength());
        assertTrue(course1.getAssignments().contains(assignment));

        teacher.removeAssignment(course1, assignment);
        assertEquals(0, course1.getAssignmentsLength());
        assertFalse(course1.getAssignments().contains(assignment));

    }

    @Test
    public void addStudentTest() {
        teacher.addStudent(course1, student1);
        assertEquals(1, course1.getStudentItemsLength());
        assertTrue(course1.getStudentItems().getFirst().getStudent().equals(student1));
    }

    @Test
    public void removeStudentTest() {
        teacher.addStudent(course1, student1);
        assertEquals(1, course1.getStudentItemsLength());
        assertEquals(course1.getCreditUnit(), student1.getCreditUnits());
        assertTrue(course1.getStudentItems().getFirst().getStudent().equals(student1));

        teacher.removeStudent(course1, student1);
        assertEquals(0, student1.getCreditUnits());
        assertEquals(0, course1.getStudentItemsLength());
        assertTrue(course1.getStudentItems().isEmpty());
    }

    @Test
    public void gradeStudentTest() {
        teacher.addStudent(course1, student1); // Credit Unit = 3
        teacher.addStudent(course1, student2); // Credit Unit = 4

        teacher.addStudent(course2, student1);
        teacher.addStudent(course2, student2);

        teacher.gradeStudent(course1, student1, 18);
        teacher.gradeStudent(course1, student2, 17.4);
        teacher.gradeStudent(course2, student1, 20);
        teacher.gradeStudent(course2, student2, 16);

        assertEquals(18, course1.getGrade(student1));
        assertEquals(17.4, course1.getGrade(student2));
        assertEquals(20, course2.getGrade(student1));
        assertEquals(16, course2.getGrade(student2));

        double expectedStudent1SemesterGrade = (18 * 3 + 20 * 4) / 7.0;
        double expectedStudent2SemesterGrade = (17.4 * 3 + 16 * 4) / 7.0;

        assertEquals(expectedStudent1SemesterGrade, student1.getSemesterGrade());
        assertEquals(expectedStudent2SemesterGrade, student2.getSemesterGrade());
    }

    @Test
    public void rewardStudentTest() {
        teacher.addStudent(course1, student1);
        teacher.gradeStudent(course1, student1, 19);
        assertEquals(19, student1.getSemesterGrade());

        teacher.rewardStudent(course1, student1, 1);
        assertEquals(20, student1.getSemesterGrade());

    }
}
