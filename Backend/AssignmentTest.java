import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentTest {
    private Assignment assignment;
    private Course course;

    @BeforeEach
    public void setUp() {
        Teacher teacher = new Teacher("Ali", "Alavi", new ArrayList<>(), "402243010", "1234".toCharArray());
        course = new Course("Test Course", "Test Prof", 3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, "2024-05-01", Semester.FIRST, teacher);
        assignment = new Assignment(7, true, course);
    }

    @Test
    public void testSetDeadline() {
        assignment.setDeadline(10);
        assertEquals(10, assignment.getDeadline());
    }

    @Test
    public void testExtendDeadlineByDays() {
        assignment.extendDeadlineByDays(3);
        assertEquals(10, assignment.getDeadline());
    }

    @Test
    public void testSetAvailable() {
        assignment.setAvailable(false);
        assertFalse(assignment.isAvailable());
    }

    @Test
    public void testGetDeadline() {
        assertEquals(7, assignment.getDeadline());
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
