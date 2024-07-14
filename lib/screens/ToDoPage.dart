import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:timezone/timezone.dart' as tz;

class TodoScreen extends StatefulWidget {
  final String username;

  const TodoScreen({Key? key, required this.username}) : super(key: key);

  @override
  _TodoScreenState createState() => _TodoScreenState();
}

class _TodoScreenState extends State<TodoScreen> {
  final List<Map<String, dynamic>> _tasks = [];
  final FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin =
      FlutterLocalNotificationsPlugin();

  @override
  void initState() {
    super.initState();
    _initializeNotifications();
    _loadTasks();
  }

  void _initializeNotifications() async {
    const DarwinInitializationSettings initializationSettingsIOS =
        DarwinInitializationSettings();

    final InitializationSettings initializationSettings =
        InitializationSettings(
      iOS: initializationSettingsIOS,
    );

    await flutterLocalNotificationsPlugin.initialize(initializationSettings);
  }

  Future<void> _loadTasks() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? tasksString = prefs.getString('tasks_' + widget.username);
    if (tasksString != null) {
      List<dynamic> tasksJson = jsonDecode(tasksString);
      setState(() {
        _tasks.clear();
        for (var taskJson in tasksJson) {
          _tasks.add(Map<String, dynamic>.from(taskJson));
        }
      });
    }
  }

  Future<void> _saveTasks() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String tasksString = jsonEncode(_tasks);
    await prefs.setString('tasks_'+widget.username, tasksString);
  }

  Future<void> _deleteTasks() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.remove('tasks_'+widget.username);
    setState(() {
      _tasks.clear();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('کارهای امروز'),
        automaticallyImplyLeading: false,
        actions: [
          IconButton(
            icon: Icon(Icons.delete),
            onPressed: _confirmDeleteTasks,
          ),
        ],
      ),
      body: ListView.builder(
        itemCount: _tasks.length,
        itemBuilder: (context, index) {
          return Padding(
            padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
            child: Container(
              decoration: BoxDecoration(
                  gradient: LinearGradient(colors: [
                    Colors.deepPurple,
                    Colors.deepPurple.withOpacity(0.6)
                  ], begin: Alignment.topLeft, end: Alignment.bottomRight),
                  borderRadius: BorderRadius.all(Radius.circular(20))),
              child: Padding(
                padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                child: ListTile(
                  title: Text(
                    _tasks[index]['title'],
                    style: TextStyle(
                      fontSize: 30,
                      color: Colors.white,
                      decoration: _tasks[index]['done']
                          ? TextDecoration.lineThrough
                          : TextDecoration.none,
                    ),
                  ),
                  subtitle: Text(
                    _tasks[index]['time'],
                    style: TextStyle(
                      fontSize: 14,
                      color: Colors.white70,
                    ),
                  ),
                  trailing: IconButton(
                    icon: Icon(
                      _tasks[index]['done']
                          ? Icons.check_box
                          : Icons.check_box_outline_blank,
                      color: Colors.white,
                    ),
                    onPressed: () {
                      toggleDoneIcon(index);
                    },
                  ),
                ),
              ),
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showAddTaskDialog(context),
        child: Icon(Icons.add),
        backgroundColor: Colors.deepPurple,
      ),
    );
  }

  void toggleDoneIcon(int index) {
    setState(() {
      _tasks[index]['done'] = !_tasks[index]['done'];
    });
    _saveTasks();
  }

  void _showAddTaskDialog(BuildContext context) {
    final TextEditingController _title = TextEditingController();
    DateTime _selectedDate = DateTime.now();
    TimeOfDay _selectedTime =
        TimeOfDay(hour: DateTime.now().hour + 1, minute: DateTime.now().minute);

    showDialog(
      context: context,
      builder: (context1) {
        return StatefulBuilder(
          builder: (context1, setState) {
            return SimpleDialog(
              title: Text('افزودن کار جدید'),
              children: [
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      TextField(
                        controller: _title,
                        decoration: InputDecoration(labelText: 'عنوان'),
                      ),
                      SizedBox(height: 20),
                      Row(
                        children: [
                          Text("تاریخ: "),
                          Text(DateFormat('d MMMM').format(_selectedDate).toString()),
                          IconButton(
                            icon: Icon(Icons.calendar_today),
                            onPressed: () async {
                              final pickedDate = await showDatePicker(
                                context: context,
                                initialDate: _selectedDate,
                                firstDate: DateTime(2024),
                                lastDate: DateTime(2100),
                              );
                              setState(() => _selectedDate = pickedDate!);
                            },
                          ),
                        ],
                      ),
                      Row(
                        children: [
                          Text("زمان: "),
                          Text(_selectedTime.format(context)),
                          IconButton(
                            icon: Icon(Icons.access_time),
                            onPressed: () async {
                              final pickedTime = await showTimePicker(
                                context: context,
                                initialTime: _selectedTime,
                              );
                              if (pickedTime != null) {
                                setState(() => _selectedTime = pickedTime);
                              }
                            },
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    TextButton(
                      onPressed: () => Navigator.of(context1).pop(),
                      child: Text('لغو'),
                    ),
                    ElevatedButton(
                      onPressed: () {
                        final title = _title.text;
                        final dateTime = DateTime(
                          _selectedDate.year,
                          _selectedDate.month,
                          _selectedDate.day,
                          _selectedTime.hour,
                          _selectedTime.minute,
                        );
                        final formattedDateTime =
                            DateFormat('d MMMM : h:mm a').format(dateTime);
                        if (title.isNotEmpty) {
                          _addTask(title, formattedDateTime);
                          _scheduleNotification(title, dateTime);
                          Navigator.of(context1).pop();
                        }
                      },
                      child: Text('افزودن'),
                    ),
                  ],
                ),
              ],
            );
          },
        );
      },
    );
  }

  void _addTask(String title, String dateTime) {
    setState(() {
      _tasks.add({'title': title, 'time': dateTime, 'done': false});
    });
    _saveTasks();
  }

  void _scheduleNotification(String title, DateTime scheduledTime) async {
    final int notificationId = _tasks.length;
    final tz.TZDateTime scheduledTZDateTime =
        tz.TZDateTime.from(scheduledTime, tz.local);

    await flutterLocalNotificationsPlugin.zonedSchedule(
      notificationId,
      'کارهای امروز',
      title,
      scheduledTZDateTime,
      const NotificationDetails(
        iOS: DarwinNotificationDetails(),
      ),
      uiLocalNotificationDateInterpretation:
          UILocalNotificationDateInterpretation.absoluteTime,
    );
  }

  void _confirmDeleteTasks() {
    showDialog(
      context: context,
      builder: (context) {
        return SimpleDialog(
          title: Center(child: Text('حذف تمام کارها')),
          children: [
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    'آیا مطمئن هستید که می‌خواهید تمام کارهای خود را حذف کنید؟',
                    textAlign: TextAlign.right,
                  ),
                  SizedBox(height: 20),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      TextButton(
                        onPressed: () => Navigator.of(context).pop(),
                        child: Text('لغو'),
                      ),
                      ElevatedButton(
                        onPressed: () async {
                          await _deleteTasks();
                          Navigator.of(context).pop();
                          ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                            content: Text('تمام کارها حذف شدند'),
                            backgroundColor: Colors.red,
                          ));
                        },
                        child: Text('حذف'),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ],
        );
      },
    );
  }
}
