import 'package:flutter/material.dart';
import 'package:socket_io_client/socket_io_client.dart' as IO;

class TodoScreen extends StatefulWidget {
  final String username;

  TodoScreen({required this.username});

  @override
  _TodoScreenState createState() => _TodoScreenState();
}

class _TodoScreenState extends State<TodoScreen> {
  List<Task> tasks = [];

  @override
  void initState() {
    super.initState();
  }



  void addTask(String title, String time) {
    String formattedTask = "${widget.username}+$title+$time";
    setState(() {
      tasks.add(Task(title: title, time: time));
    });
  }

  void toggleTaskStatus(int index) {
    setState(() {
      tasks[index].isDone = !tasks[index].isDone;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('کارهای امروز'),
        automaticallyImplyLeading: false,
      ),
      body: ListView.builder(
        itemCount: tasks.length,
        itemBuilder: (context, index) {
          final task = tasks[index];
          return ListTile(
            title: Text(task.title),
            subtitle: Text(task.time),
            trailing: Icon(
              task.isDone ? Icons.check_circle : Icons.radio_button_unchecked,
              color: task.isDone ? Colors.green : null,
            ),
            onTap: () => toggleTaskStatus(index),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showAddTaskDialog(context),
        child: Icon(Icons.add),
      ),
    );
  }

  void _showAddTaskDialog(BuildContext context) {
    final _titleController = TextEditingController();
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
                  Text('زمان: ${_selectedTime.format(context)}'),
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
                final time = _selectedTime.format(context);
                if (title.isNotEmpty) {
                  addTask(title, time);
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
}

class Task {
  String title;
  String time;
  bool isDone = false;

  Task({required this.title, required this.time});
}
