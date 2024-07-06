import 'package:flutter/material.dart';
import 'package:EDUPULSE/screens/InitialPage.dart';
import 'package:EDUPULSE/screens/LoginPage.dart';
import 'package:EDUPULSE/screens/HomePage.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:timezone/data/latest.dart' as tz;
import 'package:timezone/timezone.dart' as tz;
import 'package:shared_preferences/shared_preferences.dart';

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

  runApp(MyApp(
      initialRoute: (username != null && password != null) ? '/home' : '/'));
}

class MyApp extends StatelessWidget {
  final String initialRoute;

  MyApp({required this.initialRoute});

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
        '/home': (context) => HomePage(),
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