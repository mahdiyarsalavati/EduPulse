import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

import 'ChangePassword.dart';
import 'EditInfoPage.dart';
import 'RemoveAccountPage.dart';

class UserInfoPage extends StatefulWidget {
  final String firstName;
  final String lastName;
  final String username;

  const UserInfoPage({
    Key? key,
    required this.firstName,
    required this.lastName,
    required this.username,
  }) : super(key: key);

  @override
  _UserInfoPageState createState() => _UserInfoPageState();
}

class _UserInfoPageState extends State<UserInfoPage> {
  String _avatarUrl = '';
  late String _firstName;
  late String _lastName;
  late Socket _socket;
  final ImagePicker _picker = ImagePicker();

  String _averageGrade = '...';
  String _semester = '...';
  String _creditUnit = '...';
  bool _loading = true;

  @override
  void initState() {
    super.initState();
    _firstName = widget.firstName;
    _lastName = widget.lastName;
    _connectToSocket().then((_) {
      _socket.write('GET_INFOPAGE_DATA ${widget.username}\n');
    });
  }

  Future<void> _connectToSocket() async {
    try {
      _socket = await Socket.connect('127.0.0.1', 12345);
      _socket.listen(
            (data) {
          String response = String.fromCharCodes(data).trim();
          _handleSocketResponse(response);
        },
      );
    } catch (e) {}
  }

  void _handleSocketResponse(String response) {
    if (response.startsWith('INFOPAGE ')) {
      List<String> data = response.split(' ');
      if (data.length >= 4) {
        setState(() {
          _averageGrade = data[1];
          _semester = data[2];
          _creditUnit = data[3];
          _loading = false;
        });
      }
    }
  }

  Future<void> _pickImage(ImageSource source) async {
    final pickedFile = await _picker.pickImage(source: source);
    if (pickedFile != null) {
      setState(() {
        _avatarUrl = pickedFile.path;
      });
    }
  }

  void _editInfo() async {
    final result = await Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => EditInfoPage(
          firstName: _firstName,
          lastName: _lastName,
          username: widget.username,
        ),
      ),
    );

    if (result != null && result is Map<String, String>) {
      setState(() {
        _firstName = result['firstName']!;
        _lastName = result['lastName']!;
      });
    }
  }

  void _changePassword() {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) =>
                ChangePasswordPage(username: widget.username)));
  }

  void _removeAccount() {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) =>
                RemoveAccountPage(username: widget.username)));
  }

  @override
  void dispose() {
    _socket.destroy();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('اطلاعات کاربر')),
      body: _loading
          ? Center(child: CircularProgressIndicator())
          : Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            GestureDetector(
              onTap: () async {
                final action = await showDialog<String>(
                  context: context,
                  builder: (BuildContext context) => AlertDialog(
                    title: const Text('یک گزینه را انتخاب کنید',
                        textAlign: TextAlign.center),
                    actions: <Widget>[
                      TextButton(
                          onPressed: () =>
                              Navigator.pop(context, 'Gallery'),
                          child: const Text('گالری')),
                      TextButton(
                          onPressed: () =>
                              Navigator.pop(context, 'Camera'),
                          child: const Text('دوربین')),
                    ],
                  ),
                );
                if (action == 'Gallery') {
                  _pickImage(ImageSource.gallery);
                } else if (action == 'Camera') {
                  _pickImage(ImageSource.camera);
                }
              },
              child: Stack(
                alignment: Alignment.center,
                children: [
                  CircleAvatar(
                    radius: 50,
                    backgroundImage: _avatarUrl.isNotEmpty
                        ? FileImage(File(_avatarUrl))
                        : null,
                    child: _avatarUrl.isEmpty
                        ? Icon(CupertinoIcons.person_alt_circle, size: 50)
                        : null,
                  ),
                ],
              ),
            ),
            SizedBox(height: 10),
            Text('$_firstName $_lastName',
                textAlign: TextAlign.center,
                style:
                TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            SizedBox(height: 20),
            Expanded(
              child: Directionality(
                textDirection: TextDirection.rtl,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _buildInfoRow(
                        'شماره دانشجویی', toPersian(widget.username)),
                    _buildInfoRow(
                        'ترم جاری', semesterToPersian(_semester)),
                    _buildInfoRow('تعداد واحد', toPersian(_creditUnit)),
                    _buildInfoRow('معدل کل', toPersian(_averageGrade)),
                  ],
                ),
              ),
            ),
            SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: _editInfo,
              icon: Icon(CupertinoIcons.pencil),
              label: Text('ویرایش مشخصات'),
              style:
              ElevatedButton.styleFrom(minimumSize: Size(1000, 56)),
            ),
            SizedBox(height: 10),
            ElevatedButton.icon(
              onPressed: _changePassword,
              icon: Icon(CupertinoIcons.lock),
              label: Text('ویرایش رمز عبور'),
              style:
              ElevatedButton.styleFrom(minimumSize: Size(1000, 56)),
            ),
            SizedBox(height: 10),
            ElevatedButton.icon(
              onPressed: _removeAccount,
              icon: Icon(CupertinoIcons.trash, color: Colors.white),
              label: Text('حذف حساب کاربری',
                  style: TextStyle(color: Colors.white)),
              style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.red,
                  minimumSize: Size(1000, 56)),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildInfoRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label,
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500)),
          Text(value,
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.w400)),
        ],
      ),
    );
  }
}

String toPersian(String input) {
  String result = "";

  for (var char in input.split('')) {
    switch (char) {
      case '0':
        result += '۰';
        break;
      case '1':
        result += '۱';
        break;
      case '2':
        result += '۲';
        break;
      case '3':
        result += '۳';
        break;
      case '4':
        result += '۴';
        break;
      case '5':
        result += '۵';
        break;
      case '6':
        result += '۶';
        break;
      case '7':
        result += '۷';
        break;
      case '8':
        result += '۸';
        break;
      case '9':
        result += '۹';
        break;
      default:
        result += char;
    }
  }

  return result;
}

String semesterToPersian(String semester) {
  String result = "";
  switch (semester) {
    case 'FIRST':
      result = "یک";
      break;
    case 'SECOND':
      result = "دو";
      break;
    case 'THIRD':
      result = "سه";
      break;
    case 'FOURTH':
      result = "چهار";
      break;
    case 'FIFTH':
      result = "پنج";
      break;
    case 'SIXTH':
      result = "شش";
      break;
    case 'SEVENTH':
      result = "هفت";
      break;
    case 'EIGHTH':
      result = "هشت";
      break;
    default:
      result = "تابستان";
  }
  return result;
}