import 'dart:io';
import 'package:flutter/material.dart';

class AppState extends ChangeNotifier {
  final Socket socket;
  String username;
  String firstName;
  String lastName;
  int numberOfAssignments = 0;
  int numberOfExams = 0;
  double highestGrade = 0.0;
  double lowestGrade = 0.0;
  List<String> activeAssignments = [];
  List<String> notActiveAssignments = [];
  List<String> courses = [];

  AppState({
    required this.socket,
    required this.username,
    required this.firstName,
    required this.lastName,
  });

  void fetchHomePageData() {
    socket.write('GET_HOMEPAGE_DATA $username\n');
    socket.listen((List<int> event) {
      final response = String.fromCharCodes(event).trim();
      _handleSocketResponse(response);
    });
  }

  void fetchCoursesData() {
    socket.write('GET_COURSES_OF_STUDENT $username\n');
    socket.listen((List<int> event) {
      final response = String.fromCharCodes(event).trim();
      courses = response.split('\n');
      notifyListeners();
    });
  }

  void _handleSocketResponse(String response) {
    if (response.startsWith('HOMEPAGE_DATA')) {
      final parts = response.split(' ');
      firstName = parts[1];
      lastName = parts[2];
      numberOfAssignments = int.parse(parts[3]);
      numberOfExams = int.parse(parts[4]);
      highestGrade = double.parse(parts[5]);
      lowestGrade = double.parse(parts[6]);
      activeAssignments = parts.sublist(7, parts.indexOf('END_ACTIVE')).toList();
      notActiveAssignments = parts.sublist(parts.indexOf('END_ACTIVE') + 1).toList();
      notifyListeners();
    }
  }

  void completeTask(String task) {
    activeAssignments.remove(task);
    notActiveAssignments.add(task);
    notifyListeners();
  }

  void revertTask(String task) {
    notActiveAssignments.remove(task);
    activeAssignments.add(task);
    notifyListeners();
  }

  void uploadAssignment(String assignmentId, String filePath) {
    socket.write('UPLOAD_ASSIGNMENT $assignmentId $filePath\n');
  }
}
