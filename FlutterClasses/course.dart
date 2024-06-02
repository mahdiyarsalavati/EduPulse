import 'teacher.dart';
import 'student.dart';
import 'project.dart';
import 'assignment.dart';
import 'semester.dart';

class Course {
  String name;
  int creditUnit;
  Map<Student, double> studentGrades = {};
  List<Project> projects = [];
  List<Assignment> assignments = [];
  bool isAvailable;
  String examDate;
  Semester semester;
  Teacher teacher;
  String ID;

  Course(this.name, this.creditUnit, List<Student>? students, this.projects,
      this.assignments, this.isAvailable, this.examDate, this.semester,
      this.teacher, this.ID) {
    students?.forEach(addStudent);
    projects?.forEach(addProject);
    assignments?.forEach(addAssignment);
  }

  void addStudent(Student student) {
    studentGrades[student] = 0.0; 
  }

  void addProject(Project project) {
    projects.add(project);
  }

  void addAssignment(Assignment assignment) {
    assignments.add(assignment);
  }
}
