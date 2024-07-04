import 'course.dart';
import 'person.dart';

class Teacher extends Person {
  Teacher(String firstName, String lastName, List<Course> courses, String ID, String password)
      : super(firstName, lastName, courses, ID, password);
}
