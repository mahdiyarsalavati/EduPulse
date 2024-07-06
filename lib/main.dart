import 'package:flutter/material.dart';
import 'package:EDUPULSE/screens/InitialPage.dart';
import 'package:EDUPULSE/screens/LoginPage.dart';
import 'package:EDUPULSE/screens/HomePage.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:timezone/data/latest.dart' as tz;
import 'package:timezone/timezone.dart' as tz;
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:io';

final FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin =
FlutterLocalNotificationsPlugin();

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  tz.initializeTimeZones();
  final String timeZoneName = await tz.local.name;
  tz.setLocalLocation(tz.getLocation(timeZoneName));

  const DarwinInitializationSettings initializationSettingsIOS =
  DarwinInitializationSettings();

  final InitializationSettings initializationSettings = InitializationSettings(
    iOS: initializationSettingsIOS,
  );

  await flutterLocalNotificationsPlugin.initialize(initializationSettings);

  SharedPreferences prefs = await SharedPreferences.getInstance();
  String? username = prefs.getString('username');
  String? password = prefs.getString('password');

  if (username != null && password != null) {
    try {
      Socket socket = await Socket.connect('127.0.0.1', 12345);
      await autoLogin(username, password, socket);
    } catch (e) {
      print('Error connecting to socket: $e');
      runApp(MyApp(initialRoute: '/'));
    }
  } else {
    runApp(MyApp(initialRoute: '/'));
  }
}

Future<void> autoLogin(String username, String password, Socket socket) async {
  socket.write('LOGIN $username $password\n');

  socket.listen((List<int> event) async {
    final response = String.fromCharCodes(event).trim();
    if (response.startsWith('LOGIN_SUCCESS')) {
      socket.write('GET_HOMEPAGE_DATA $username\n');
    } else if (response.startsWith('HOMEPAGE_DATA')) {
      final parts = response.split(' ');
      final firstName = parts[1];
      final lastName = parts[2];
      final numberOfAssignments = int.parse(parts[3]);
      final numberOfExams = int.parse(parts[4]);
      final highestGrade = double.parse(parts[5]);
      final lowestGrade = double.parse(parts[6]);
      final activeAssignments = parts.sublist(7, parts.indexOf('END_ACTIVE')).toList();
      final notActiveAssignments = parts.sublist(parts.indexOf('END_ACTIVE') + 1).toList();

      runApp(MyApp(
        initialRoute: '/home',
        socket: socket,
        username: username,
        firstName: firstName,
        lastName: lastName,
        numberOfAssignments: numberOfAssignments,
        numberOfExams: numberOfExams,
        highestGrade: highestGrade,
        lowestGrade: lowestGrade,
        activeAssignments: activeAssignments,
        notActiveAssignments: notActiveAssignments,
      ));
    } else {
      runApp(MyApp(initialRoute: '/'));
    }
  });
}

class MyApp extends StatelessWidget {
  final String initialRoute;
  final Socket? socket;
  final String? username;
  final String? firstName;
  final String? lastName;
  final int? numberOfAssignments;
  final int? numberOfExams;
  final double? highestGrade;
  final double? lowestGrade;
  final List<String>? activeAssignments;
  final List<String>? notActiveAssignments;

  MyApp({
    required this.initialRoute,
    this.socket,
    this.username,
    this.firstName,
    this.lastName,
    this.numberOfAssignments,
    this.numberOfExams,
    this.highestGrade,
    this.lowestGrade,
    this.activeAssignments,
    this.notActiveAssignments,
  });

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'EduPulse',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        fontFamily: "IRANSansX",
        textTheme: TextTheme(
          bodyText2: TextStyle(fontSize: 25.0, fontFamily: 'IRANSansX'),
        ),
        inputDecorationTheme: const InputDecorationTheme(
          filled: true,
          fillColor: Colors.white,
          errorStyle: TextStyle(height: 0),
          border: defaultInputBorder,
          enabledBorder: defaultInputBorder,
          focusedBorder: defaultInputBorder,
          errorBorder: defaultInputBorder,
        ),
      ),
      initialRoute: initialRoute,
      routes: {
        '/': (context) => InitialPage(),
        '/login': (context) => LoginPage(),
        '/home': (context) => HomePage(
          socket: socket!,
          username: username!,
          firstName: firstName!,
          lastName: lastName!,
          numberOfAssignments: numberOfAssignments!,
          numberOfExams: numberOfExams!,
          highestGrade: highestGrade!,
          lowestGrade: lowestGrade!,
          activeAssignments: activeAssignments!,
          notActiveAssignments: notActiveAssignments!,
        ),
      },
    );
  }
}

const defaultInputBorder = OutlineInputBorder(
  borderRadius: BorderRadius.all(Radius.circular(16)),
  borderSide: BorderSide(
    color: Color(0xFFDEE3F2),
    width: 1,
  ),
);
