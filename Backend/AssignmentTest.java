import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentTest {
    private Assignment assignment;
    private Course course;

    @BeforeEach
    public void setUp() {
        Teacher teacher = new Teacher("Ali", "Alavi", new ArrayList<>(), "402243010", "1234".toCharArray());
        course = new Course("Test Course", 3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, "2024-05-01", Semester.FIRST, teacher, "4021");
        assignment = new Assignment(LocalDate.now().plusDays(7), true, course, "40221", "3");
    }

    @Test
    public void testSetDeadline() {
        LocalDate newDeadline = LocalDate.now().plusDays(10);
        assignment.setDeadline(newDeadline);
        assertEquals(newDeadline, assignment.getDeadline());
    }

    @Test
    public void testExtendDeadlineByDays() {
        LocalDate expectedDeadline = assignment.getDeadline().plusDays(3);
        assignment.extendDeadlineByDays(3);
        assertEquals(expectedDeadline, assignment.getDeadline());
    }

    @Test
    public void testSetAvailable() {
        assignment.setAvailable(false);
        assertFalse(assignment.isAvailable());
    }

    @Test
    public void testGetDeadline() {
        assertEquals(LocalDate.now().plusDays(7), assignment.getDeadline());
    }

    @Test
    public void testIsAvailable() {
        assertTrue(assignment.isAvailable());
    }

    @Test
    public void testGetCourse() {
        assertEquals(course, assignment.getCourse());
    }
}
