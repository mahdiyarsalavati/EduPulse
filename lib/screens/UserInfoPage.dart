import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:flutter/cupertino.dart';

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

  final TextEditingController _averageGradeController =
      TextEditingController(text: '۱۸.۶۴');
  final TextEditingController _semesterController =
      TextEditingController(text: 'بهار ۱۴۰۲ - ۱۴۰۳');
  final TextEditingController _creditUnitController =
      TextEditingController(text: '۱۶');

  final ImagePicker _picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    _firstName = widget.firstName;
    _lastName = widget.lastName;
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
      MaterialPageRoute(builder: (context) => ChangePasswordPage()),
    );
  }

  void _removeAccount() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => RemoveAccountPage()),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('اطلاعات کاربر'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            GestureDetector(
              onTap: () async {
                final action = await showDialog<String>(
                  context: context,
                  builder: (BuildContext context) => AlertDialog(
                    title: const Text(
                      'یک گزینه را انتخاب کنید',
                      textAlign: TextAlign.center,
                    ),
                    actions: <Widget>[
                      TextButton(
                        onPressed: () => Navigator.pop(context, 'Gallery'),
                        child: const Text('گالری'),
                      ),
                      TextButton(
                        onPressed: () => Navigator.pop(context, 'Camera'),
                        child: const Text('دوربین'),
                      ),
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
            Text(
              '$_firstName $_lastName',
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 20),
            Expanded(
              child: Directionality(
                textDirection: TextDirection.rtl,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _buildInfoRow('شماره دانشجویی', widget.username),
                    _buildInfoRow('ترم جاری', _semesterController.text),
                    _buildInfoRow('تعداد واحد', _creditUnitController.text),
                    _buildInfoRow('معدل کل', _averageGradeController.text),
                  ],
                ),
              ),
            ),
            SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: _editInfo,
              icon: Icon(CupertinoIcons.pencil),
              label: Text('ویرایش مشخصات'),
              style: ElevatedButton.styleFrom(
                minimumSize: Size(1000, 56),
              ),
            ),
            SizedBox(height: 10),
            ElevatedButton.icon(
              onPressed: _changePassword,
              icon: Icon(CupertinoIcons.lock),
              label: Text('ویرایش رمز عبور'),
              style: ElevatedButton.styleFrom(
                minimumSize: Size(1000, 56),
              ),
            ),
            SizedBox(height: 10),
            ElevatedButton.icon(
              onPressed: _removeAccount,
              icon: Icon(
                CupertinoIcons.trash,
                color: Colors.white,
              ),
              label: Text(
                'حذف حساب کاربری',
                style: TextStyle(color: Colors.white),
              ),
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

  Widget _buildInfoRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            label,
            style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
          ),
          Text(
            value,
            style: TextStyle(fontSize: 16, fontWeight: FontWeight.w400),
          ),
        ],
      ),
    );
  }
}
