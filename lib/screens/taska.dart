import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Taska extends StatefulWidget {
  const Taska({Key? key}) : super(key: key);

  @override
  _TaskaState createState() => _TaskaState();
}

class _TaskaState extends State<Taska> {
  final List<Map<String, dynamic>> _tasks = [
    {'title': 'آز ریز - تمرین 1', 'completed': false},
    {'title': 'تمرین الگوریتم', 'completed': false},
    {'title': 'انتخاب واحد', 'completed': false},
    {'title': 'آز ریز - تمرین 2', 'completed': false},
  ];

  final List<Map<String, dynamic>> _completedTasks = [
    {'title': 'آز ریز - تمرین 0', 'completed': true},
    {'title': 'بررسی فایل‌ها تمرین', 'completed': true},
  ];

  void _addTask(String title, DateTime deadline) {
    setState(() {
      _tasks.add({'title': title, 'completed': false});
    });
  }

  void _showAddTaskDialog() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (context) {
        return Padding(
          padding:
              EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
          child: AddTaskDialog(
            onAdd: (title, deadline) {
              _addTask(title, deadline);
              Navigator.of(context).pop();
            },
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('تسک‌ها'),
        actions: [
          IconButton(
            icon: Icon(CupertinoIcons.person_circle),
            onPressed: () {
              // Navigate to user profile page
            },
          )
        ],
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildCurrentTasksSection(),
              SizedBox(height: 20),
              _buildCompletedTasksSection(),
            ],
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _showAddTaskDialog,
        child: Icon(Icons.add),
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: 2, // Highlight the Tasks tab
        items: [
          BottomNavigationBarItem(
            icon: Icon(Icons.home),
            label: 'سرا',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.campaign),
            label: 'خبرا',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.task),
            label: 'تسکا',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.school),
            label: 'کلاسا',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.edit),
            label: 'کارا',
          ),
        ],
        onTap: (index) {
          // Handle bottom navigation tap
        },
      ),
    );
  }

  Widget _buildCurrentTasksSection() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'تسک‌ها',
          style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
        ),
        SizedBox(height: 10),
        ListView(
          shrinkWrap: true,
          physics: NeverScrollableScrollPhysics(),
          children: _tasks.map((task) {
            return _buildTaskItem(task['title'], task['completed']);
          }).toList(),
        ),
      ],
    );
  }

  Widget _buildCompletedTasksSection() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'تسک‌های انجام شده',
          style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
        ),
        SizedBox(height: 10),
        ListView(
          shrinkWrap: true,
          physics: NeverScrollableScrollPhysics(),
          children: _completedTasks.map((task) {
            return _buildTaskItem(task['title'], task['completed']);
          }).toList(),
        ),
      ],
    );
  }

  Widget _buildTaskItem(String text, bool completed) {
    return ListTile(
      leading: Icon(completed ? Icons.check_circle : Icons.cancel,
          color: completed ? Colors.green : Colors.red),
      title: Text(text),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(Icons.cancel, color: Colors.red),
          Icon(Icons.check_circle, color: Colors.green),
        ],
      ),
    );
  }
}

class AddTaskDialog extends StatefulWidget {
  final Function(String, DateTime) onAdd;

  const AddTaskDialog({Key? key, required this.onAdd}) : super(key: key);

  @override
  _AddTaskDialogState createState() => _AddTaskDialogState();
}

class _AddTaskDialogState extends State<AddTaskDialog> {
  final TextEditingController _taskController = TextEditingController();
  DateTime _selectedDate = DateTime.now();

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text('افزودن کار', style: TextStyle(fontSize: 18)),
              TextButton(
                onPressed: () => Navigator.of(context).pop(),
                child: Text('انصراف', style: TextStyle(color: Colors.red)),
              ),
            ],
          ),
          TextField(
            controller: _taskController,
            decoration: InputDecoration(
              labelText: 'زمان ددلاین',
              prefixIcon: Icon(Icons.edit, color: Colors.orange),
            ),
          ),
          SizedBox(height: 10),
          InkWell(
            onTap: _pickDeadline,
            child: InputDecorator(
              decoration: InputDecoration(
                labelText: 'زمان ددلاین',
                prefixIcon: Icon(Icons.calendar_today, color: Colors.purple),
              ),
              child: Text(
                _selectedDate == null
                    ? 'انتخاب تاریخ'
                    : _selectedDate.toLocal().toString().split(' ')[0],
              ),
            ),
          ),
          SizedBox(height: 20),
          Center(
            child: ElevatedButton(
              onPressed: _submit,
              child: Text('ثبت', style: TextStyle(color: Colors.white)),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.red,
                minimumSize: Size(double.infinity, 56),
              ),
            ),
          ),
        ],
      ),
    );
  }

  void _pickDeadline() async {
    DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _selectedDate,
      firstDate: DateTime(2020),
      lastDate: DateTime(2100),
    );

    if (picked != null && picked != _selectedDate) {
      setState(() {
        _selectedDate = picked;
      });
    }
  }

  void _submit() {
    if (_taskController.text.isNotEmpty) {
      widget.onAdd(_taskController.text, _selectedDate);
    }
  }
}
