import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

class ChangePasswordPage extends StatefulWidget {
  const ChangePasswordPage({super.key});

  @override
  _ChangePasswordPageState createState() => _ChangePasswordPageState();
}

class _ChangePasswordPageState extends State<ChangePasswordPage> {
  final _formKey = GlobalKey<FormState>();
  final _passwordController = TextEditingController();
  final _verifyPasswordController = TextEditingController();
  bool _obscurePassword = true;
  bool _obscureVerifyPassword = true;

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
    return null;
  }

  String? _validateVerifyPassword(String? value) {
    if (value != _passwordController.text) {
      return 'رمز عبورها مطابقت ندارند';
    }
    return null;
  }

  void _submit() {
    if (_formKey.currentState?.validate() ?? false) {
      Navigator.pop(context);
    }
  }

  void _togglePasswordVisibility() {
    setState(() {
      _obscurePassword = !_obscurePassword;
    });
  }

  void _toggleVerifyPasswordVisibility() {
    setState(() {
      _obscureVerifyPassword = !_obscureVerifyPassword;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('ویرایش رمزعبور'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              Text(
                "رمز عبور جدید",
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 14),
              ),
              TextFormField(
                controller: _passwordController,
                obscureText: _obscurePassword,
                decoration: InputDecoration(
                  suffixIcon: IconButton(
                    icon: Icon(
                      _obscurePassword ? CupertinoIcons.eye : CupertinoIcons.eye_slash,
                    ),
                    onPressed: _togglePasswordVisibility,
                  ),
                ),
                validator: _validatePassword,
              ),
              Text(
                "تایید رمز عبور",
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 14),
              ),
              TextFormField(
                controller: _verifyPasswordController,
                obscureText: true,
                validator: _validateVerifyPassword,
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: _submit,
                child: Text('ویرایش رمز عبور'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
