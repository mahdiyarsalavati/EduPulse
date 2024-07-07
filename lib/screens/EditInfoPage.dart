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
  late TextEditingController _firstNameController;
  late TextEditingController _lastNameController;
  late Socket _socket;

  @override
  void initState() {
    super.initState();
    _firstNameController = TextEditingController(text: widget.firstName);
    _lastNameController = TextEditingController(text: widget.lastName);
    _connectToServer();
  }

  @override
  void dispose() {
    _firstNameController.dispose();
    _lastNameController.dispose();
    _socket.close();
    super.dispose();
  }

  void _connectToServer() async {
    _socket = await Socket.connect('127.0.0.1', 8280);
  }

  void _sendUpdatedInfo(String username, String firstName, String lastName) {
    String message = 'CHANGE_NAME $username $firstName $lastName';
    _socket.write(message);
  }

  void _submit() {
    if (_formKey.currentState?.validate() ?? false) {
      String updatedFirstName = _firstNameController.text;
      String updatedLastName = _lastNameController.text;
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
                controller: _firstNameController,
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
                controller: _lastNameController,
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
