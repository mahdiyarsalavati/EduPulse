import 'package:EDUPULSE/screens/onboding/welcomePage.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const EDUPULSE());
}

class EDUPULSE extends StatelessWidget {
  const EDUPULSE({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'The Flutter Way',
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
      home: const WelcomePage(),
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
