import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class SignupPage extends StatefulWidget {
  @override
  _SignupPageState createState() => _SignupPageState();
}

class _SignupPageState extends State<SignupPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _firstNameController = TextEditingController();
  final TextEditingController _lastNameController = TextEditingController();
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _verifyPasswordController =
      TextEditingController();
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
    });
  }

  @override
  void dispose() {
    _firstNameController.dispose();
    _lastNameController.dispose();
    _usernameController.dispose();
    _passwordController.dispose();
    _verifyPasswordController.dispose();
    _socket.close();
    super.dispose();
  }

  void _handleSocketResponse(String response) {
    if (response == 'SIGNUP_SUCCESS') {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
            content: Text('ثبت نام موفقیت آمیز بود. لطفا وارد شوید.',
                textAlign: TextAlign.right, textDirection: TextDirection.rtl)),
      );
      Navigator.pop(context);
    } else if (response == 'USER_ALREADY_EXISTS') {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
            content: Text('کاربر از قبل وجود دارد.',
                textAlign: TextAlign.right, textDirection: TextDirection.rtl)),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
            content: Text('ثبت نام موفقیت آمیز نبود.',
                textAlign: TextAlign.right, textDirection: TextDirection.rtl)),
      );
    }
  }

  void _submit() {
    if (_formKey.currentState?.validate() ?? false) {
      _socket.write(
          'SIGNUP ${_firstNameController.text} ${_lastNameController.text} ${_usernameController.text} ${_passwordController.text}\n');
    }
  }

  String? _validateName(String? value) {
    if (value == null || value.isEmpty) {
      return 'این فیلد نمی‌تواند خالی باشد';
    }
    return null;
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

    if (!hasUppercase) {
      return 'رمز عبور باید شامل حداقل یک حرف بزرگ باشد';
    }
    if (!hasLowercase) {
      return 'رمز عبور باید شامل حداقل یک حرف کوچک باشد';
    }
    if (!hasDigit) {
      return 'رمز عبور باید شامل حداقل یک عدد باشد';
    }

    if (_usernameController.text.isNotEmpty &&
        value.contains(_usernameController.text)) {
      return 'رمز عبور نباید شامل نام کاربری باشد';
    }
    return null;
  }

  String? _validateVerifyPassword(String? value) {
    if (value != _passwordController.text) {
      return 'رمز عبورها با هم مطابقت ندارند';
    }
    return null;
  }

  void _toggleObscureText() {
    setState(() {
      _obscureText = !_obscureText;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('ثبت نام')),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.symmetric(horizontal: 24.0),
          child: Form(
            key: _formKey,
            child: Column(
              children: [
                const SizedBox(
                  height: 50,
                ),
                const Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text("ثبت نام",
                        style: TextStyle(fontFamily: "Badee", fontSize: 70)),
                    const SizedBox(width: 15),
                    const Icon(
                      CupertinoIcons.person_add,
                      size: 70,
                      color: Colors.blueAccent,
                    ),
                  ],
                ),
                const SizedBox(height: 50),
                TextFormField(
                  controller: _firstNameController,
                  decoration: InputDecoration(
                    labelText: 'نام',
                    prefixIcon:
                        Icon(CupertinoIcons.person, color: Colors.blueAccent),
                  ),
                  validator: _validateName,
                ),
                const SizedBox(height: 20),
                TextFormField(
                  controller: _lastNameController,
                  decoration: InputDecoration(
                    labelText: 'نام خانوادگی',
                    prefixIcon:
                        Icon(CupertinoIcons.person, color: Colors.blueAccent),
                  ),
                  validator: _validateName,
                ),
                const SizedBox(height: 20),
                TextFormField(
                  controller: _usernameController,
                  decoration: InputDecoration(
                    labelText: 'نام کاربری',
                    prefixIcon: Icon(CupertinoIcons.person_alt_circle,
                        color: Colors.blueAccent),
                  ),
                  validator: _validateUsername,
                ),
                const SizedBox(height: 20),
                TextFormField(
                  controller: _passwordController,
                  obscureText: _obscureText,
                  decoration: InputDecoration(
                    labelText: 'رمز عبور',
                    prefixIcon:
                        Icon(CupertinoIcons.lock, color: Colors.blueAccent),
                    suffixIcon: IconButton(
                      icon: Icon(_obscureText
                          ? CupertinoIcons.eye
                          : CupertinoIcons.eye_slash),
                      onPressed: _toggleObscureText,
                    ),
                  ),
                  validator: _validatePassword,
                ),
                const SizedBox(height: 20),
                TextFormField(
                  controller: _verifyPasswordController,
                  obscureText: true,
                  decoration: InputDecoration(
                    labelText: 'تایید رمز عبور',
                    prefixIcon:
                        Icon(CupertinoIcons.lock, color: Colors.blueAccent),
                  ),
                  validator: _validateVerifyPassword,
                ),
                const SizedBox(height: 20),
                SizedBox(height: 30),
                ElevatedButton.icon(
                  onPressed: _submit,
                  icon: const Icon(CupertinoIcons.person_add),
                  style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.blueAccent,
                      minimumSize: Size(double.infinity, 56)),
                  label: Text('ثبت نام', style: TextStyle(color: Colors.white)),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
