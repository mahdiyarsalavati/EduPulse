import 'course.dart';
import 'assignment.dart';

class Project extends Assignment {
  String name;

  Project(DateTime deadline, bool isAvailable, Course course, String ID, this.name)
      : super(deadline, isAvailable, course, ID);
}
