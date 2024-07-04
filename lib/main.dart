import 'dart:io';
import 'package:flutter/material.dart';
import 'screens/InitialPage.dart';

class SocketSingleton {
  static final SocketSingleton _instance = SocketSingleton._internal();
  late Socket socket;

  factory SocketSingleton() {
    return _instance;
  }

  SocketSingleton._internal();

  Future<void> connect() async {
    socket = await Socket.connect('127.0.0.1', 12345);
  }
}

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final socketSingleton = SocketSingleton();
  await socketSingleton.connect();
  runApp(EDUPULSE(socketSingleton: socketSingleton));
}

class EDUPULSE extends StatelessWidget {
  final SocketSingleton socketSingleton;

  const EDUPULSE({Key? key, required this.socketSingleton}) : super(key: key);

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
      home: InitialPage(),
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
