import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
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
  }

  void _initializeNotifications() async {
    const DarwinInitializationSettings initializationSettingsIOS =
    DarwinInitializationSettings();

    final InitializationSettings initializationSettings = InitializationSettings(
      iOS: initializationSettingsIOS,
    );

    await flutterLocalNotificationsPlugin.initialize(initializationSettings);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('کارهای امروز'),
        automaticallyImplyLeading: false,
      ),
      body: ListView.builder(
        itemCount: _tasks.length,
        itemBuilder: (context, index) {
          return Card(
            color: Colors.deepPurple,
            child: ListTile(
              title: Text(
                _tasks[index]['title']!,
                style: TextStyle(
                  fontSize: 30,
                  color: Colors.white,
                  decoration: _tasks[index]['done']
                      ? TextDecoration.lineThrough
                      : TextDecoration.none,
                ),
              ),
              subtitle: Text(
                _tasks[index]['time']!,
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
                  setState(() {
                    _tasks[index]['done'] = !_tasks[index]['done'];
                  });
                },
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

  void _showAddTaskDialog(BuildContext context) {
    final _titleController = TextEditingController();
    DateTime _selectedDate = DateTime.now();
    TimeOfDay _selectedTime = TimeOfDay(hour: 7, minute: 0);

    showDialog(
      context: context,
      builder: (ctx) {
        return AlertDialog(
          title: Text('افزودن کار جدید'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: _titleController,
                decoration: InputDecoration(labelText: 'عنوان'),
              ),
              SizedBox(height: 20),
              Row(
                children: [
                  Text("تاریخ:"),
                  Text('${DateFormat('yyyy-MM-dd').format(_selectedDate)}'),
                  IconButton(
                    icon: Icon(Icons.calendar_today),
                    onPressed: () async {
                      final pickedDate = await showDatePicker(
                        context: context,
                        initialDate: _selectedDate,
                        firstDate: DateTime(2000),
                        lastDate: DateTime(2101),
                      );
                      if (pickedDate != null) {
                        setState(() => _selectedDate = pickedDate);
                        (ctx as Element).markNeedsBuild();
                      }
                    },
                  ),
                ],
              ),
              Row(
                children: [
                  Text("زمان:"),
                  Text('${_selectedTime.format(context)}'),
                  IconButton(
                    icon: Icon(Icons.access_time),
                    onPressed: () async {
                      final pickedTime = await showTimePicker(
                        context: context,
                        initialTime: _selectedTime,
                      );
                      if (pickedTime != null) {
                        setState(() => _selectedTime = pickedTime);
                        (ctx as Element).markNeedsBuild();
                      }
                    },
                  ),
                ],
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(),
              child: Text('لغو'),
            ),
            ElevatedButton(
              onPressed: () {
                final title = _titleController.text;
                final dateTime = DateTime(
                  _selectedDate.year,
                  _selectedDate.month,
                  _selectedDate.day,
                  _selectedTime.hour,
                  _selectedTime.minute,
                );
                final formattedDateTime =
                DateFormat('yyyy-MM-dd – kk:mm').format(dateTime);
                if (title.isNotEmpty) {
                  _addTask(title, formattedDateTime);
                  _scheduleNotification(title, dateTime);
                  Navigator.of(ctx).pop();
                }
              },
              child: Text('افزودن'),
            ),
          ],
        );
      },
    );
  }

  void _addTask(String title, String dateTime) {
    setState(() {
      _tasks.add({'title': title, 'time': dateTime, 'done': false});
    });
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
      androidAllowWhileIdle: true,
      uiLocalNotificationDateInterpretation:
      UILocalNotificationDateInterpretation.absoluteTime,
    );
  }
}
