import java.util.ArrayList;
import java.util.List;

public class StudentItem {
    Student student;
    Number grade;

    public StudentItem(Student student, Number grade) {
        this.student = student;
        this.grade = grade;
    }

    public StudentItem(Student student) {
        this.student = student;
        this.grade = 0;
    }

    static List<Student> savedStudents = new ArrayList<Student>();

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        savedStudents.add(student);
        this.student = student;
    }

    public static void removeStudent(Student student) {
        if (savedStudents.contains(student)) savedStudents.remove(student);
    }

    public Number getGrade() {
        return grade;
    }

    public void setGrade(Number grade) {
        this.grade = grade;
    }

    public static List<Student> toList() {
        return savedStudents;
    }
}