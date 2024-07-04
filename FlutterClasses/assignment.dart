import 'course.dart';

class Assignment {
  final DateTime creationDate;
  DateTime deadline;
  bool isAvailable;
  final Course course;
  static final List<Assignment> archive = [];
  final String ID;

  Assignment(this.deadline, this.isAvailable, this.course, this.ID)
      : creationDate = DateTime.now() {
    if (!isAvailable) {
      archive.add(this);
    }
  }
}
