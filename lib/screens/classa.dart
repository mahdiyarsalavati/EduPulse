import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Classa extends StatefulWidget {
  const Classa({Key? key}) : super(key: key);

  @override
  _ClassaState createState() => _ClassaState();
}

class _ClassaState extends State<Classa> {
  final List<Map<String, dynamic>> _classes = [
    {
      'title': 'برنامه‌سازی پیشرفته',
      'instructor': 'دکتر وحیدی',
      'units': 3,
      'remainingAssignments': 4,
      'topStudent': 'علی علوی'
    },
    {
      'title': 'برنامه‌سازی پیشرفته',
      'instructor': 'دکتر وحیدی',
      'units': 3,
      'remainingAssignments': 4,
      'topStudent': 'علی علوی'
    },
  ];

  void _addClass(String classCode) {
    // Add class logic here
    setState(() {
      _classes.add({
        'title': 'کلاس جدید',
        'instructor': 'استاد جدید',
        'units': 3,
        'remainingAssignments': 4,
        'topStudent': 'دانشجو جدید'
      });
    });
  }

  void _showAddClassDialog() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (context) {
        return Padding(
          padding:
              EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
          child: AddClassDialog(
            onAdd: (classCode) {
              _addClass(classCode);
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
        title: Text('کلاسا'),
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
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    'ترم بهار 1403',
                    style: TextStyle(fontSize: 16),
                  ),
                  ElevatedButton.icon(
                    onPressed: _showAddClassDialog,
                    icon: Icon(Icons.add),
                    label: Text('افزودن کلاس'),
                    style:
                        ElevatedButton.styleFrom(backgroundColor: Colors.blue),
                  ),
                ],
              ),
              SizedBox(height: 20),
              _buildClassesList(),
            ],
          ),
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: 2, // Highlight the Classes tab
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

  Widget _buildClassesList() {
    return ListView.builder(
      shrinkWrap: true,
      physics: NeverScrollableScrollPhysics(),
      itemCount: _classes.length,
      itemBuilder: (context, index) {
        final classItem = _classes[index];
        return Card(
          color: Colors.pink.shade200,
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  classItem['title'],
                  style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
                SizedBox(height: 8),
                Text('استاد: ${classItem['instructor']}'),
                SizedBox(height: 4),
                Text('تعداد واحد: ${classItem['units']}'),
                SizedBox(height: 4),
                Text('تکالیف باقی‌مانده: ${classItem['remainingAssignments']}'),
                SizedBox(height: 4),
                Text('دانشجوی ممتاز: ${classItem['topStudent']}'),
              ],
            ),
          ),
        );
      },
    );
  }
}

class AddClassDialog extends StatefulWidget {
  final Function(String) onAdd;

  const AddClassDialog({Key? key, required this.onAdd}) : super(key: key);

  @override
  _AddClassDialogState createState() => _AddClassDialogState();
}

class _AddClassDialogState extends State<AddClassDialog> {
  final TextEditingController _classCodeController = TextEditingController();

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
              Text('افزودن کلاس جدید', style: TextStyle(fontSize: 18)),
              TextButton(
                onPressed: () => Navigator.of(context).pop(),
                child: Text('انصراف', style: TextStyle(color: Colors.red)),
              ),
            ],
          ),
          TextField(
            controller: _classCodeController,
            decoration: InputDecoration(
              labelText: 'کد درس',
              hintText: 'کد گلستان درس را وارد کنید...',
              prefixIcon: Icon(Icons.edit, color: Colors.orange),
            ),
          ),
          SizedBox(height: 20),
          Center(
            child: ElevatedButton(
              onPressed: _submit,
              child: Text('افزودن', style: TextStyle(color: Colors.white)),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.pink,
                minimumSize: Size(double.infinity, 56),
              ),
            ),
          ),
        ],
      ),
    );
  }

  void _submit() {
    if (_classCodeController.text.isNotEmpty) {
      widget.onAdd(_classCodeController.text);
    }
  }
}
