import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'UserInfoPage.dart';

class HomePage extends StatefulWidget {
  final Socket socket;
  final String username;
  final String firstName;
  final String lastName;
  final int numberOfAssignments;
  final int numberOfExams;
  final double highestGrade;
  final double lowestGrade;
  final List<String> activeAssignments;
  final List<String> notActiveAssignments;

  const HomePage({
    Key? key,
    required this.socket,
    required this.username,
    required this.firstName,
    required this.lastName,
    required this.numberOfAssignments,
    required this.numberOfExams,
    required this.highestGrade,
    required this.lowestGrade,
    required this.activeAssignments,
    required this.notActiveAssignments,
  }) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int _selectedIndex = 0;

  static const List<String> _pageTitles = [
    'تمرینا',
    'خبرا',
    'کلاسا',
    'کارا',
    'سرا'
  ];

  static const List<IconData> _pageIcons = [
    CupertinoIcons.news,
    CupertinoIcons.book,
    CupertinoIcons.check_mark_circled,
    CupertinoIcons.settings,
    CupertinoIcons.home,
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_pageTitles[_selectedIndex]),
        leading: IconButton(
          icon: Icon(CupertinoIcons.person_circle),
          onPressed: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => UserInfoPage(
                  firstName: widget.firstName,
                  lastName: widget.lastName,
                  username: widget.username,
                ),
              ),
            );
          },
        ),
      ),
      body: SingleChildScrollView(
        child: SafeArea(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(height: 20),
              Center(
                child: Text(
                  'خلاصه',
                  style: TextStyle(
                    fontFamily: "Abar",
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              SizedBox(height: 16),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  _buildSummaryCard(
                    'بهترین نمره',
                    widget.highestGrade.toString(),
                    Icons.star,
                    Colors.yellow,
                  ),
                  _buildSummaryCard(
                    'تا امتحان داری',
                    widget.numberOfExams.toString(),
                    Icons.favorite,
                    Colors.red,
                  ),
                  _buildSummaryCard(
                    'تا تمرین داری',
                    widget.numberOfAssignments.toString(),
                    Icons.assignment,
                    Colors.green,
                  ),
                ],
              ),
              SizedBox(height: 30),
              Center(
                child: Text(
                  'تسک های جاری',
                  style: TextStyle(
                    fontFamily: "Abar",
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              ...widget.activeAssignments
                  .map((assignment) =>
                      _buildTaskCard(assignment, isActive: true))
                  .toList(),
              SizedBox(height: 50),
              Center(
                child: Text(
                  'کارهای انجام شده',
                  style: TextStyle(
                    fontFamily: "Abar",
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              ...widget.notActiveAssignments
                  .map((assignment) =>
                      _buildTaskCard(assignment, isActive: false))
                  .toList(),
            ],
          ),
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        items: [
          for (int i = 0; i < _pageTitles.length; i++)
            BottomNavigationBarItem(
              icon: Icon(_pageIcons[i]),
              label: _pageTitles[i],
            ),
        ],
        currentIndex: _selectedIndex,
        selectedItemColor: Colors.blueAccent,
        unselectedItemColor: Colors.grey,
        onTap: _onItemTapped,
        backgroundColor: Colors.white,
        type: BottomNavigationBarType.fixed,
      ),
    );
  }

  Widget _buildSummaryCard(
      String title, String value, IconData icon, Color color) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
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
      widget.activeAssignments.remove(task);
      widget.notActiveAssignments.add(task);
    });
  }

  void _revertTask(String task) {
    setState(() {
      widget.notActiveAssignments.remove(task);
      widget.activeAssignments.add(task);
    });
  }
}
