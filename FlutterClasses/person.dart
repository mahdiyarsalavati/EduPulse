import 'course.dart';

class Person {
  String firstName;
  String lastName;
  List<Course> courses;
  late int coursesLength;
  String ID;
  String password;

  Person(this.firstName, this.lastName, this.courses, this.ID, this.password) {
    coursesLength = courses?.length ?? 0;
  }
}
