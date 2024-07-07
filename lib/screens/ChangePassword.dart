import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'InitialPage.dart';

class RemoveAccountPage extends StatelessWidget {
  final String username;

  const RemoveAccountPage({super.key, required this.username});

  void _removeAccount(BuildContext context) async {
    bool success = await _sendRemoveAccountCommand(username);
    if (success) {
      await _clearSharedPreferences();
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => InitialPage()),
      );
    } else {
      _showErrorDialog(context);
    }
  }

  Future<bool> _sendRemoveAccountCommand(String username) async {
    try {
      Socket socket = await Socket.connect('127.0.0.1', 8280);
      String message = 'REMOVE_STUDENT $username';
      socket.write(message);
      await socket.flush();
      socket.close();
      return true;
    } catch (e) {
      print('Error: $e');
      return false;
    }
  }

  Future<void> _clearSharedPreferences() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.remove('username');
    await prefs.remove('password');
  }

  void _showErrorDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Error'),
          content: Text('Failed to remove account. Please try again later.'),
          actions: [
            ElevatedButton(
              child: Text('OK'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('حذف حساب کاربری'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('آیا از حذف حساب کاربری خود مطمئن هستید؟',
                textAlign: TextAlign.center),
            SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: () => _removeAccount(context),
              icon: Icon(CupertinoIcons.trash, color: Colors.white),
              label: Text('حذف حساب کاربری',
                  style: TextStyle(color: Colors.white)),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.red,
                minimumSize: Size(1000, 56),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
