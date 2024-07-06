import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'InitialPage.dart';

class RemoveAccountPage extends StatelessWidget {
  final String username;

  const RemoveAccountPage({super.key, required this.username});

  void _removeAccount(BuildContext context) {
    _sendRemoveAccountCommand(username);
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => InitialPage()),
    );
  }

  void _sendRemoveAccountCommand(String username) async {
    Socket socket = await Socket.connect('127.0.0.1', 12345);
    String message = 'REMOVE_STUDENT $username';
    socket.write(message);
    socket.close();
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
