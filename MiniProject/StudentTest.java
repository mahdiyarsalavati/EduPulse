import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {
    private Student student;
    private Teacher teacher;
    private Course course1, course2;

    @BeforeEach
    public void setUp() {
        teacher = new Teacher("Ali", "Alavi", new ArrayList<>());

        course1 = new Course("Course 1", "Ali Alavi", 3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, "2024-05-01", Semester.FIRST, teacher);
        course2 = new Course("Course 2", "Ali Alavi", 3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, "2024-05-01", Semester.FIRST, teacher);

        teacher.addCourse(course1);
        teacher.addCourse(course2);

        student = new Student("Test", "Student", new ArrayList<>(), "402243104", Semester.SECOND);

        teacher.addStudent(course1, student);
        teacher.addStudent(course2, student);
    }

    @Test
    public void testAddRemoveCourse() {
        Course course3 = new Course("Course 3", "Prof 3", 3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, "2024-05-01", Semester.FIRST, new Teacher("Ali", "Alavi", new ArrayList<>()));
        student.addCourse(course3);
        assertEquals(9, student.getCreditUnits());
        assertTrue(student.getCourses().contains(course3));

        student.removeCourse(course3);
        assertEquals(6, student.getCreditUnits());
        assertFalse(student.getCourses().contains(course3));
    }

    @Test
    public void testGetCreditUnits() {
        assertEquals(6, student.getCreditUnits());
    }

    @Test
    public void testGetAverageGrade() {
        teacher.gradeStudent(course1, student, 19);
        teacher.gradeStudent(course2, student, 20);
        double expectedAverage = (20 * 3 + 19 * 3) / 6.0;
        assertEquals(expectedAverage, student.getAverageGrade());
    }

    @Test
    public void testGetSemesterGrade() {
        course1.setSemester(Semester.FIRST);
        course2.setSemester(Semester.SECOND);
        student.setSemester(Semester.SECOND);
        teacher.gradeStudent(course1, student, 17);
        teacher.gradeStudent(course2, student, 20);
        assertEquals(20, student.getSemesterGrade());
    }
}
