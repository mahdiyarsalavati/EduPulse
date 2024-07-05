import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'ToDoPage.dart';
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
  List<String> tasks = [];

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
    CupertinoIcons.pencil_circle
  ];

  @override
  void initState() {
    super.initState();
    _fetchTasks();
  }

  Future<void> _fetchTasks() async {
    try {
      widget.socket.write('GET_TASKS ${widget.username}\n');
      widget.socket.listen((data) {
        String response = String.fromCharCodes(data).trim();
        if (response.startsWith("TASKS")) {
          setState(() {
            tasks = response.substring(6).split("\n");
          });
        }
      });
    } catch (e) {
      print('Error fetching tasks: $e');
    }
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
                  firstName: widget.firstName,
                  lastName: widget.lastName,
                  username: widget.username,
                ),
              ),
            );
          },
        ),
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
        return TodoScreen(
          username: widget.username,
        );
      default:
        return SingleChildScrollView(
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
                        toPersian(widget.numberOfExams.toString()),
                        CupertinoIcons.pencil_outline,
                        Colors.deepPurple,
                      ),
                      _buildSummaryCard(
                        'تمرین داری',
                        toPersian(widget.numberOfAssignments.toString()),
                        CupertinoIcons.rectangle_paperclip,
                        Colors.blueAccent,
                      ),
                      _buildSummaryCard(
                        'بدترین نمره',
                        toPersian(widget.lowestGrade.toString()),
                        CupertinoIcons.battery_25,
                        Colors.red,
                      ),
                      _buildSummaryCard(
                        'بهترین نمره',
                        toPersian(widget.highestGrade.toString()),
                        CupertinoIcons.chart_bar_fill,
                        Colors.green,
                      ),
                    ],
                  ),
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
                SizedBox(height: 200)
              ],
            ),
          ),
        );
    }
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

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  void _addTask(String task) {
    setState(() {
      tasks.add(task);
    });
  }

  void _updateTask(int index, String task) {
    setState(() {
      tasks[index] = task;
    });
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
}
