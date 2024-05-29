import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../screens/UserInfoPage.dart';

class SignIn extends StatefulWidget {
  const SignIn({super.key});

  @override
  _SignInState createState() => _SignInState();
}

class _SignInState extends State<SignIn> {
  bool _obscureText = true;
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();

  void _toggleObscureText() {
    setState(() {
      _obscureText = !_obscureText;
    });
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
    if (!RegExp(r'^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d!@#\$&*~]{8,}$')
        .hasMatch(value)) {
      return 'رمز عبور باید شامل حداقل یک حرف بزرگ، یک حرف کوچک و یک عدد باشد';
    }
    if (_usernameController.text.isNotEmpty &&
        value.contains(_usernameController.text)) {
      return 'رمز عبور نباید شامل نام کاربری باشد';
    }
    return null;
  }

  void _submit() {
    if (_formKey.currentState?.validate() ?? false) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (context) => UserInfoPage(
            firstName: 'علی',
            lastName: 'علوی',
            username: _usernameController.text,
          ),
        ),
      );
    }
  }


  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Column(
        children: [
          SizedBox(height: 40),
          Text("نام کاربری",
              textAlign: TextAlign.center, style: TextStyle(fontSize: 14)),
          TextFormField(
            controller: _usernameController,
            decoration: InputDecoration(
              prefixIcon: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 8),
                child: Icon(CupertinoIcons.person_alt_circle,
                    color: Colors.blueAccent),
              ),
            ),
            validator: _validateUsername,
          ),
          SizedBox(height: 30),
          Text("رمز عبور",
              textAlign: TextAlign.center, style: TextStyle(fontSize: 14)),
          TextFormField(
            controller: _passwordController,
            obscureText: _obscureText,
            decoration: InputDecoration(
              prefixIcon: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 8),
                child: Icon(CupertinoIcons.lock, color: Colors.blueAccent),
              ),
              suffixIcon: IconButton(
                icon: Icon(
                  _obscureText ? CupertinoIcons.eye : CupertinoIcons.eye_slash,
                  color: Colors.blueAccent,
                ),
                onPressed: _toggleObscureText,
              ),
            ),
            validator: _validatePassword,
          ),
          SizedBox(height: 30),
          ElevatedButton.icon(
            style: ElevatedButton.styleFrom(
                backgroundColor: Colors.blueAccent,
                minimumSize: Size(1000, 56)),
            onPressed: _submit,
            icon: Icon(CupertinoIcons.arrow_right_circle),
            label: Text("ورود", style: TextStyle(color: Colors.white)),
          )
        ],
      ),
    );
  }
}
