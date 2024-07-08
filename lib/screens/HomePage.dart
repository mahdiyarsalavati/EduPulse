import 'dart:io';

import 'package:EDUPULSE/screens/AssignmentsPage.dart';
import 'package:EDUPULSE/screens/CoursesPage.dart';
import 'package:EDUPULSE/screens/NewsPage.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'ToDoPage.dart';
import 'UserInfoPage.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int _selectedIndex = 0;
  late Socket _socket;
  String _username = '';
  String _firstName = '';
  String _lastName = '';
  int _numberOfAssignments = 0;
  int _numberOfExams = 0;
  double _highestGrade = 0.0;
  double _lowestGrade = 0.0;
  List<String> _activeAssignments = [];
  List<String> _notActiveAssignments = [];
  int _completedAssignments = 0;

  static const List<String> _pageTitles = [
    'سرا',
    'کارا',
    'کلاسا',
    'خبرا',
    'تمرینا',
  ];
  static const List<IconData> _pageIcons = [
    CupertinoIcons.house_fill,
    CupertinoIcons.pencil_circle,
    CupertinoIcons.book_circle,
    CupertinoIcons.news,
    CupertinoIcons.pencil_circle,
  ];

  @override
  void initState() {
    super.initState();
    _loadUserData();
  }

  Future<void> _loadUserData() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? username = prefs.getString('username');
    String? password = prefs.getString('password');

    if (username != null && password != null) {
      try {
        _socket = await Socket.connect('127.0.0.1', 12345);
        await _autoLogin(username, password);
      } catch (e) {
      }
    } else {
      Navigator.pushReplacementNamed(context, '/login');
    }
  }

  Future<void> _autoLogin(String username, String password) async {
    _socket.write('LOGIN $username $password\n');

    _socket.listen((List<int> event) async {
      final response = String.fromCharCodes(event).trim();
      if (response.startsWith('LOGIN_SUCCESS')) {
        _socket.write('GET_HOMEPAGE_DATA $username\n');
      } else if (response.startsWith('HOMEPAGE_DATA')) {
        final parts = response.split(' ');
        setState(() {
          _username = username;
          _firstName = parts[1];
          _lastName = parts[2];
          _numberOfAssignments = int.parse(parts[3]);
          _numberOfExams = int.parse(parts[4]);
          _highestGrade = double.parse(parts[5]);
          _lowestGrade = double.parse(parts[6]);
          _activeAssignments =
              parts.sublist(7, parts.indexOf('END')).toList();
          _notActiveAssignments =
              parts.sublist(parts.indexOf('END') + 1).toList();
          _completedAssignments = _notActiveAssignments.length;
        });
      } else {
        Navigator.pushReplacementNamed(context, '/login');
      }
    });
  }

  Future<void> fetchData() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? username = prefs.getString('username');
    String? password = prefs.getString('password');

    if (username != null && password != null) {
      try {
        _socket = await Socket.connect('127.0.0.1', 12345);
        _socket.write('LOGIN $username $password\n');

        _socket.listen((List<int> event) async {
          final response = String.fromCharCodes(event).trim();
          if (response.startsWith('LOGIN_SUCCESS')) {
            _socket.write('GET_HOMEPAGE_DATA $username\n');
          } else if (response.startsWith('HOMEPAGE_DATA')) {
            final parts = response.split(' ');
            setState(() {
              _username = username;
              _firstName = parts[1];
              _lastName = parts[2];
              _numberOfAssignments = int.parse(parts[3]);
              _numberOfExams = int.parse(parts[4]);
              _highestGrade = double.parse(parts[5]);
              _lowestGrade = double.parse(parts[6]);
              _activeAssignments =
                  parts.sublist(7, parts.indexOf('END')).toList();
              _notActiveAssignments =
                  parts.sublist(parts.indexOf('END') + 1).toList();
              _completedAssignments = _notActiveAssignments.length;
            });
          } else {
            Navigator.pushReplacementNamed(context, '/login');
          }
        });
      } catch (e) {
      }
    }
  }

  void _incrementCompletedAssignments() {
    setState(() {
      _completedAssignments++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_pageTitles[_selectedIndex]),
        leading: IconButton(
          icon: Icon(CupertinoIcons.person_circle,
              size: 40, color: Colors.blueAccent),
          onPressed: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => UserInfoPage(
                  firstName: _firstName,
                  lastName: _lastName,
                  username: _username,
                ),
              ),
            );
          },
        ),
        actions: [
          IconButton(
            icon: Icon(Icons.logout, color: Colors.blueAccent),
            onPressed: _logout,
          ),
        ],
      ),
      body: _buildPageContent(),
      bottomNavigationBar: BottomNavigationBar(
        items: [
          for (int i = 0; i < _pageTitles.length; i++)
            BottomNavigationBarItem(
              icon: Icon(_pageIcons[i]),
              label: _pageTitles[i],
            ),
        ],
        currentIndex: _selectedIndex,
        selectedItemColor: Colors.white,
        unselectedItemColor: Colors.grey,
        onTap: _onItemTapped,
        backgroundColor: Colors.deepPurple,
        type: BottomNavigationBarType.fixed,
      ),
    );
  }

  Widget _buildPageContent() {
    switch (_selectedIndex) {
      case 1:
        return TodoScreen(username: _username);
      case 2:
        return CoursesPage(username: _username);
      case 3:
        return NewsPage();
      case 4:
        return AssignmentPage(
          username: _username,
          onAssignmentCompleted: _incrementCompletedAssignments,
        );

      default:
        return SingleChildScrollView(
          child: SafeArea(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(height: 20),
                _buildSectionTitle('خلاصه'),
                SizedBox(height: 16),
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: GridView.count(
                    crossAxisCount: 2,
                    shrinkWrap: true,
                    mainAxisSpacing: 16,
                    crossAxisSpacing: 16,
                    physics: NeverScrollableScrollPhysics(),
                    children: [
                      _buildSummaryCard(
                        'امتحان داری',
                        toPersian(_numberOfExams.toString()),
                        CupertinoIcons.pencil_outline,
                        Colors.deepPurple,
                      ),
                      _buildSummaryCard(
                        'تمرین داری',
                        toPersian((_numberOfAssignments - _completedAssignments)
                            .toString()),
                        CupertinoIcons.rectangle_paperclip,
                        Colors.blueAccent,
                      ),
                      _buildSummaryCard(
                        'بدترین نمره',
                        toPersian(_lowestGrade.toString()),
                        CupertinoIcons.battery_25,
                        Colors.red,
                      ),
                      _buildSummaryCard(
                        'بهترین نمره',
                        toPersian(_highestGrade.toString()),
                        CupertinoIcons.chart_bar_fill,
                        Colors.green,
                      ),
                    ],
                  ),
                ),
                SizedBox(height: 30),
                _buildSectionTitle('تسک های جاری'),
                SizedBox(height: 10),
                isStringListEmpty(_activeAssignments)
                    ? Center(child: Text('تسکی وجود نداره'))
                    : Column(
                  children: _activeAssignments
                      .map((assignment) =>
                      _buildTaskCard(assignment, isActive: true))
                      .toList(),
                ),
                SizedBox(height: 40),
                _buildSectionTitle('کارهای انجام شده'),
                SizedBox(height: 10),
                _notActiveAssignments.isEmpty
                    ? Center(child: Text('تسکی وجود نداره'))
                    : Column(
                  children: _notActiveAssignments
                      .map((assignment) =>
                      _buildTaskCard(assignment, isActive: false))
                      .toList(),
                ),
                SizedBox(height: 200),
              ],
            ),
          ),
        );
    }
  }

  Widget _buildSectionTitle(String title) {
    return Center(
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
        decoration: BoxDecoration(
          border: Border.all(color: Colors.grey),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Text(
          title,
          style: TextStyle(
            fontFamily: "IRANSansX",
            fontSize: 24,
            color: Colors.deepPurple,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }

  Widget _buildSummaryCard(
      String title, String value, IconData icon, Color color) {
    return Card(
      elevation: 4,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 40, color: color),
            SizedBox(height: 8),
            Text(value,
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
            Text(title, style: TextStyle(fontSize: 16)),
          ],
        ),
      ),
    );
  }

  Widget _buildTaskCard(String task, {bool isActive = true}) {
    return Card(
      child: ListTile(
        leading: isActive
            ? Icon(Icons.check_box_outline_blank, color: Colors.green)
            : Icon(Icons.check_box, color: Colors.red),
        title: Text(task),
        trailing: isActive
            ? IconButton(
          icon: Icon(CupertinoIcons.check_mark_circled,
              color: Colors.blueAccent),
          onPressed: () {
            _completeTask(task);
          },
        )
            : IconButton(
          icon: Icon(CupertinoIcons.clear_circled, color: Colors.red),
          onPressed: () {
            _revertTask(task);
          },
        ),
      ),
    );
  }

  void _completeTask(String task) {
    setState(() {
      _activeAssignments.remove(task);
      _notActiveAssignments.add(task);
    });
  }

  void _revertTask(String task) {
    setState(() {
      _notActiveAssignments.remove(task);
      _activeAssignments.add(task);
    });
  }

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  void _logout() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.remove('username');
    await prefs.remove('password');
    Navigator.pushReplacementNamed(context, '/login');
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

  bool isStringListEmpty(List<String> list) {
    if (list.isEmpty) return true;
    if (list[0].length == 0) return true;
    return false;
  }
}