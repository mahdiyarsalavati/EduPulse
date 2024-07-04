import 'person.dart';
import 'course.dart';
import 'semester.dart';

class Student extends Person {
  late int creditUnits;
  late double averageGrade;
  late double semesterGrade;
  Semester semester;

  Student(String firstName, String lastName, List<Course> courses, String id,
      String password, this.semester)
      : super(firstName, lastName, courses, id, password) {
  }
}
