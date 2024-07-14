import 'dart:io';

import 'package:flutter/material.dart';

class EditInfoPage extends StatefulWidget {
  final String firstName;
  final String lastName;
  final String username;

  const EditInfoPage({
    Key? key,
    required this.firstName,
    required this.lastName,
    required this.username,
  }) : super(key: key);

  @override
  _EditInfoPageState createState() => _EditInfoPageState();
}

class _EditInfoPageState extends State<EditInfoPage> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _firstName;
  late TextEditingController _lastName;
  late Socket _socket;

  @override
  void initState() {
    super.initState();
    _firstName = TextEditingController(text: widget.firstName);
    _lastName = TextEditingController(text: widget.lastName);
    _connectToServer();
  }

  @override
  void dispose() {
    _firstName.dispose();
    _lastName.dispose();
    _socket.close();
    super.dispose();
  }

  void _connectToServer() async {
    _socket = await Socket.connect('127.0.0.1', 12345);
  }

  void _sendUpdatedInfo(String username, String firstName, String lastName) {
    String message = 'CHANGE_NAME ' + username + firstName + lastName;
    _socket.write(message);
  }

  void _submit() {
    if (_formKey.currentState?.validate() ?? false) {
      String updatedFirstName = _firstName.text;
      String updatedLastName = _lastName.text;
      _sendUpdatedInfo(widget.username, updatedFirstName, updatedLastName);
      Navigator.pop(
        context,
        {
          'firstName': updatedFirstName,
          'lastName': updatedLastName,
        },
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('ویرایش مشخصات'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              Text("نام",
                  textAlign: TextAlign.center, style: TextStyle(fontSize: 14)),
              TextFormField(
                controller: _firstName,
                textDirection: TextDirection.rtl,
                textAlign: TextAlign.right,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'نام نمی‌تواند خالی باشد';
                  }
                  return null;
                },
              ),
              Text(" نام خانوادگی",
                  textAlign: TextAlign.center, style: TextStyle(fontSize: 14)),
              TextFormField(
                controller: _lastName,
                textDirection: TextDirection.rtl,
                textAlign: TextAlign.right,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'نام خانوادگی نمی‌تواند خالی باشد';
                  }
                  return null;
                },
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: _submit,
                child: Text('ذخیره تغییرات'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
