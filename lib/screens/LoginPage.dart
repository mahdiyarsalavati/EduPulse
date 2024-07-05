import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'HomePage.dart';

class LoginPage extends StatefulWidget {
  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _obscureText = true;
  late Socket _socket;

  @override
  void initState() {
    super.initState();
    _connectToSocket();
  }

  Future<void> _connectToSocket() async {
    _socket = await Socket.connect('127.0.0.1', 12345);
    _socket.listen((List<int> event) {
      final response = String.fromCharCodes(event).trim();
      _handleSocketResponse(response);
    }, onError: (error) {
      print('Socket error: $error');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Connection error. Please try again.')),
      );
    });
  }

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    _socket.close();
    super.dispose();
  }

  void _handleSocketResponse(String response) {
    if (response.startsWith('LOGIN_SUCCESS')) {
      _socket.write('GET_HOMEPAGE_DATA ${_usernameController.text}\n');
    } else if (response.startsWith('HOMEPAGE_DATA')) {
      final parts = response.split(' ');
      final firstName = parts[1];
      final lastName = parts[2];
      final numberOfAssignments = int.parse(parts[3]);
      final numberOfExams = int.parse(parts[4]);
      final highestGrade = double.parse(parts[5]);
      final lowestGrade = double.parse(parts[6]);
      final activeAssignments =
          parts.sublist(7, parts.indexOf('END_ACTIVE')).toList();
      final notActiveAssignments =
          parts.sublist(parts.indexOf('END_ACTIVE') + 1).toList();

      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (context) => HomePage(
            socket: _socket,
            username: _usernameController.text,
            firstName: firstName,
            lastName: lastName,
            numberOfAssignments: numberOfAssignments,
            numberOfExams: numberOfExams,
            highestGrade: highestGrade,
            lowestGrade: lowestGrade,
            activeAssignments: activeAssignments,
            notActiveAssignments: notActiveAssignments,
          ),
        ),
      );
    } else if (response == "LOGIN_FAILED") {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('ورود ناموفق بود. دوباره تلاش کنید.')),
      );
    }
  }

  void _submit() {
    if (_formKey.currentState?.validate() ?? false) {
      _socket.write(
          'LOGIN ${_usernameController.text} ${_passwordController.text}\n');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("ورود"),
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.symmetric(horizontal: 24.0),
          child: Form(
            key: _formKey,
            child: Center(
              child: Column(
                children: [
                  const SizedBox(height: 150),
                  const Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text("ورود",
                          style: TextStyle(fontFamily: "Badee", fontSize: 100)),
                      const SizedBox(width: 15),
                      const Icon(CupertinoIcons.person_alt_circle,
                          size: 70, color: Colors.blueAccent),
                    ],
                  ),
                  const SizedBox(height: 20),
                  const Text(
                    "درصورتی که قبلا ثبت نام کرده اید، می توانید وارد شوید",
                    style: TextStyle(fontSize: 15),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 40),
                  TextFormField(
                    controller: _usernameController,
                    decoration: const InputDecoration(
                      labelText: "نام کاربری",
                      prefixIcon: Padding(
                        padding: EdgeInsets.symmetric(horizontal: 8),
                        child: Icon(CupertinoIcons.person_alt_circle,
                            color: Colors.blueAccent),
                      ),
                    ),
                    validator: _validateUsername,
                  ),
                  const SizedBox(height: 30),
                  TextFormField(
                    controller: _passwordController,
                    obscureText: _obscureText,
                    decoration: InputDecoration(
                      labelText: "رمز عبور",
                      prefixIcon: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 8),
                        child:
                            Icon(CupertinoIcons.lock, color: Colors.blueAccent),
                      ),
                      suffixIcon: IconButton(
                        icon: Icon(
                          _obscureText
                              ? CupertinoIcons.eye
                              : CupertinoIcons.eye_slash,
                          color: Colors.blueAccent,
                        ),
                        onPressed: _toggleObscureText,
                      ),
                    ),
                    validator: _validatePassword,
                  ),
                  const SizedBox(height: 30),
                  ElevatedButton.icon(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.blueAccent,
                      minimumSize: Size(double.infinity, 56),
                    ),
                    onPressed: _submit,
                    icon: const Icon(CupertinoIcons.person_alt_circle),
                    label: const Text("ورود",
                        style: TextStyle(color: Colors.white)),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  String? _validateUsername(String? value) {
    if (value == null || value.isEmpty) {
      return 'نام کاربری نمی‌تواند خالی باشد';
    }
    if (!RegExp(r'^\d{9}$').hasMatch(value)) {
      return 'نام کاربری باید شامل 9 عدد باشد';
    }
    return null;
  }

  String? _validatePassword(String? value) {
    if (value == null || value.isEmpty) {
      return 'رمز عبور نمی‌تواند خالی باشد';
    }
    if (value.length < 8) {
      return 'رمز عبور باید حداقل 8 کاراکتر باشد';
    }
    bool hasUppercase = value.contains(RegExp(r'[A-Z]'));
    bool hasLowercase = value.contains(RegExp(r'[a-z]'));
    bool hasDigit = value.contains(RegExp(r'\d'));

    if (!hasUppercase || !hasLowercase || !hasDigit) {
      return 'رمز عبور باید شامل حرف بزرگ، حرف کوچک و عدد باشد';
    }

    if (_usernameController.text.isNotEmpty &&
        value.contains(_usernameController.text)) {
      return 'رمز عبور نباید شامل نام کاربری باشد';
    }
    return null;
  }

  void _toggleObscureText() {
    setState(() {
      _obscureText = !_obscureText;
    });
  }
}
