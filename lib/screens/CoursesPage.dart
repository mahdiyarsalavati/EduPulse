import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class CoursesPage extends StatefulWidget {
  final String username;

  const CoursesPage({Key? key, required this.username}) : super(key: key);

  @override
  _CoursesPageState createState() => _CoursesPageState();
}

class _CoursesPageState extends State<CoursesPage> {
  List<Course> _courses = [];
  List<Course> _pendingCourses = [];
  final _courseCode = TextEditingController();
  late Socket _socket;

  @override
  void initState() {
    super.initState();
    _connectSocket();
  }

  void _connectSocket() async {
    _socket = await Socket.connect('127.0.0.1', 12345);
    _fetchCourses();
  }

  void _fetchCourses() {
    _socket.write('GET_COURSES_OF_STUDENT ' + widget.username + '\n');
    _socket.listen((data) {
      String response = String.fromCharCodes(data).trim();
      if (response.isNotEmpty) {
        List<String> courseDetails = response.split(' END ');
        setState(() {
          _courses.clear();
          for (var detail in courseDetails) {
            if (detail.isNotEmpty) {
              _courses.add(Course.parse(detail));
            }
          }
        });
      }
    });
  }

  void _requestJoinCourse(String courseCode) async {
    setState(() {
      _pendingCourses.add(Course("در حال لود شدن", '...', '0', 0, '...'));
    });
    Socket socket = await Socket.connect('127.0.0.1', 12345);
    socket.write('REQUEST_JOIN_COURSE ' + widget.username +' ' + courseCode + '\n');
    socket.flush();
    socket.listen((data) {
      String response = String.fromCharCodes(data).trim();
      if (response == 'JOIN_SUCCESS') {
        setState(() {
          for (int i = _pendingCourses.length - 1; i >= 0; i--) {
            if (_pendingCourses[i].name == courseCode) {
              _pendingCourses.removeAt(i);
            }
          }
          _fetchCourses();
        });
      } else {
        setState(() {
          for (int i = _pendingCourses.length - 1; i >= 0; i--) {
            if (_pendingCourses[i].name == courseCode) {
              _pendingCourses.removeAt(i);
            }
          }
        });
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text('کد درخواستی موجود نبود'),
          backgroundColor: Colors.red,
        ));
      }
      socket.close();
    });
  }

  void _showJoinCourseDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text('افزودن کلاس جدید'),
          content: TextField(
            controller: _courseCode,
            decoration:
                InputDecoration(hintText: 'کد گلستان درس را وارد کنید...'),
          ),
          actions: [
            ElevatedButton(
              onPressed: () {
                _requestJoinCourse(_courseCode.text);
                Navigator.of(context).pop();
                ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                  content: Text('درخواست شما برای آموزش ارسال شد'),
                  backgroundColor: Colors.green,
                ));
              },
              child: Text('افزودن'),
              style: ElevatedButton.styleFrom(
                foregroundColor: Colors.white,
                backgroundColor: Colors.deepPurple,
              ),
            ),
          ],
        );
      },
    );
  }

  @override
  void dispose() {
    _socket.close();
    _courseCode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Container(
              padding: EdgeInsets.symmetric(vertical: 16.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  ElevatedButton.icon(
                    onPressed: () => _showJoinCourseDialog(),
                    icon: Icon(CupertinoIcons.add),
                    label: Text('افزودن کلاس جدید',
                        style: TextStyle(fontFamily: "IRANSansX")),
                    style: ElevatedButton.styleFrom(
                      foregroundColor: Colors.white,
                      backgroundColor: Colors.deepPurple,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10),
                      ),
                      padding:
                          EdgeInsets.symmetric(vertical: 12, horizontal: 24),
                      textStyle: TextStyle(fontSize: 18),
                    ),
                  ),
                  Text(
                    'کلاس‌ها',
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                      color: Colors.deepPurple,
                    ),
                  ),
                ],
              ),
            ),
            Expanded(
              child: ListView(
                children: [
                  for (var course in _courses) CourseCard(course),
                  for (var course in _pendingCourses) CourseCard(course),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class Course {
  final String name;
  final String teacherName;
  final String creditUnit;
  final int noOfAssignments;
  final String bestStudent;

  Course(
    this.name,
    this.teacherName,
    this.creditUnit,
    this.noOfAssignments,
    this.bestStudent,
  );

  static Course parse(String str) {
    List<String> parts = str.trim().split(' ');
    return new Course(
        parts[0], parts[1], parts[2], int.parse(parts[3]), parts[4]);
  }
}

class CourseCard extends StatelessWidget {
  final Course course;

  const CourseCard(this.course);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 7, 0, 7),
      child: Container(
        decoration: const BoxDecoration(
            borderRadius: BorderRadius.all(Radius.circular(20)),
            gradient: LinearGradient(
                colors: [Colors.deepPurple, Colors.black],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight)),
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            textDirection: TextDirection.rtl,
            children: [
              Text(
                course.name,
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 26,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.right,
                textDirection: TextDirection.rtl,
              ),
              Divider(color: Colors.white),
              Text(
                'استاد: ' + course.teacherName,
                style: TextStyle(color: Colors.white, fontSize: 20),
                textAlign: TextAlign.right,
                textDirection: TextDirection.rtl,
              ),
              Text(
                'تعداد واحد: ' + course.creditUnit,
                style: TextStyle(color: Colors.white, fontSize: 20),
                textAlign: TextAlign.right,
                textDirection: TextDirection.rtl,
              ),
              Text(
                'تکالیف باقی‌مانده: ' + course.noOfAssignments.toString(),
                style: TextStyle(color: Colors.white, fontSize: 20),
                textAlign: TextAlign.right,
                textDirection: TextDirection.rtl,
              ),
              Text(
                'دانشجوی ممتاز: ' + course.bestStudent,
                style: TextStyle(color: Colors.white, fontSize: 20),
                textAlign: TextAlign.right,
                textDirection: TextDirection.rtl,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
