import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    private Course course;
    private Student student1, student2;
    private Assignment assignment1, assignment2;
    private Project project1, project2;
    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        teacher = new Teacher("Bob", "Anderson", new ArrayList<>(), "402243010", "1234".toCharArray());
        course = new Course("Test Course", 3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, "2024-05-01", Semester.FIRST, teacher, "4021");
        teacher.addCourse(course);
        student1 = new Student("Test", "Student1", new ArrayList<>(), "1234".toCharArray(), "402243104", Semester.SECOND);
        student2 = new Student("Test", "Student2", new ArrayList<>(), "1234".toCharArray(), "402243001", Semester.SECOND);
        assignment1 = new Assignment(LocalDate.now().plusDays(7), true, course);
        assignment2 = new Assignment(LocalDate.now().plusDays(10), false, course);
        project1 = new Project(LocalDate.now().plusDays(7), true, course, "Test Project1");
        project2 = new Project(LocalDate.now().plusDays(10), false, course, "Test Project2");
    }

    @Test
    public void testAddRemoveStudent() {
        teacher.addStudent(course, student1);
        boolean test1 = false;
        for (StudentItem si : course.getStudentItems()) {
            if (si.getStudent().equals(student1)) {
                test1 = true;
                break;
            }
        }
        assertTrue(test1);

        teacher.removeStudent(course, student1);
        boolean test2 = false;
        for (StudentItem si : course.getStudentItems()) {
            if (si.getStudent().equals(student1)) {
                test2 = true;
                break;
            }
        }
        assertFalse(test2);

        teacher.addStudent(course, student1);
        teacher.addStudent(course, student2);
        boolean test3 = false, test4 = false;
        for (StudentItem si : course.getStudentItems()) {
            if (si.getStudent().equals(student1)) {
                test3 = true;
            }
            if (si.getStudent().equals(student2)) {
                test4 = true;
            }
        }
        assertTrue(test3);
        assertTrue(test4);

        teacher.removeStudent(course, student1);
        test3 = false;
        test4 = false;
        for (StudentItem si : course.getStudentItems()) {
            if (si.getStudent().equals(student1)) {
                test3 = true;
            }
            if (si.getStudent().equals(student2)) {
                test4 = true;
            }
        }
        assertFalse(test3);
        assertTrue(test4);

        assertThrows(IllegalArgumentException.class, () -> course.removeStudent(new Student("Test", "Student3", new ArrayList<>(), "1234".toCharArray(), "402243104", Semester.SECOND)));
    }

    @Test
    public void testAddRemoveAssignment() {
        teacher.addAssignment(course, assignment1);
        assertTrue(course.getAssignments().contains(assignment1));

        teacher.removeAssignment(course, assignment1);
        assertFalse(course.getAssignments().contains(assignment1));

        teacher.addAssignment(course, assignment1);
        teacher.addAssignment(course, assignment2);
        assertTrue(course.getAssignments().contains(assignment1));
        assertTrue(course.getAssignments().contains(assignment2));

        teacher.removeAssignment(course, assignment1);
        assertFalse(course.getAssignments().contains(assignment1));
        assertTrue(course.getAssignments().contains(assignment2));

        assertThrows(IllegalArgumentException.class, () -> course.removeAssignment(new Assignment(LocalDate.now().plusDays(5), true, course)));
    }

    @Test
    public void testAddRemoveProject() {
        teacher.addProject(course, project1);
        assertTrue(course.getProjects().contains(project1));

        teacher.removeProject(course, project1);
        assertFalse(course.getProjects().contains(project1));

        teacher.addProject(course, project1);
        teacher.addProject(course, project2);
        assertTrue(course.getProjects().contains(project1));
        assertTrue(course.getProjects().contains(project2));

        teacher.removeProject(course, project1);
        assertFalse(course.getProjects().contains(project1));
        assertTrue(course.getProjects().contains(project2));

        assertThrows(IllegalArgumentException.class, () -> course.removeProject(new Project(LocalDate.now().plusDays(5), true, course, "Test Project3")));
    }

    @Test
    public void testGetHighestGradeValue() {
        teacher.addStudent(course, student1);
        teacher.addStudent(course, student2);
        teacher.gradeStudent(course, student1, 16);
        teacher.gradeStudent(course, student2, 20);

        assertEquals(20.0, course.getHighestGradeValue());
    }

    @Test
    public void testGetHighestGradeStudent() {
        teacher.addStudent(course, student1);
        teacher.addStudent(course, student2);
        teacher.gradeStudent(course, student1, 17);
        teacher.gradeStudent(course, student2, 19);

        assertEquals(student2, course.getHighestGradeStudent());
    }

}
