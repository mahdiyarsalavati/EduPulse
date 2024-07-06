import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Kara extends StatefulWidget {
  const Kara({Key? key}) : super(key: key);

  @override
  _KaraState createState() => _KaraState();
}

class _KaraState extends State<Kara> {
  final List<Map<String, dynamic>> _assignments = [
    {'title': 'تمرین مینی‌پروژه AP', 'time': '4:00 عصر', 'completed': false},
    {'title': 'تمرین مدار منطقی 1', 'time': '6:00 عصر', 'completed': true},
    {'title': 'تمرین ریاضی 2', 'time': '12:00 ظهر', 'completed': true},
    {'title': 'تمرین معادلات دیفرانسیل 2', 'time': '9:00 صبح', 'completed': true},
    {'title': 'تمرین معماری کامپیوتر', 'time': '9:00 صبح', 'completed': false},
  ];

  void _showAssignmentDetails(Map<String, dynamic> assignment) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => AssignmentDetailsPage(assignment: assignment),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('تمرین‌ها'),
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16.0),
        itemCount: _assignments.length,
        itemBuilder: (context, index) {
          final assignment = _assignments[index];
          return Card(
            color: Colors.pink.shade200,
            child: ListTile(
              title: Text(assignment['title']),
              subtitle: Text(assignment['time']),
              trailing: Icon(
                assignment['completed'] ? Icons.check_circle : Icons.radio_button_unchecked,
                color: assignment['completed'] ? Colors.white : Colors.white,
              ),
              onTap: () => _showAssignmentDetails(assignment),
            ),
          );
        },
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: 4, // Highlight the Assignments tab
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
            icon: Icon(Icons.school),
            label: 'کلاسا',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.task),
            label: 'تسکا',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.edit),
            label: 'تمرینا',
          ),
        ],
        onTap: (index) {
          // Handle bottom navigation tap
        },
      ),
    );
  }
}

class AssignmentDetailsPage extends StatelessWidget {
  final Map<String, dynamic> assignment;

  const AssignmentDetailsPage({Key? key, required this.assignment}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('جزئیات تمرین'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildAssignmentHeader(),
            SizedBox(height: 20),
            _buildAssignmentDetails(),
          ],
        ),
      ),
    );
  }

  Widget _buildAssignmentHeader() {
    return Card(
      color: Colors.pink.shade200,
      child: ListTile(
        title: Text(assignment['title']),
        subtitle: Text('دوشنبه, 24 فروردین 1403\n${assignment['time']}'),
        trailing: Icon(
          assignment['completed'] ? Icons.check_circle : Icons.radio_button_unchecked,
          color: assignment['completed'] ? Colors.white : Colors.white,
        ),
      ),
    );
  }

  Widget _buildAssignmentDetails() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'جزئیات تمرین',
          style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
        SizedBox(height: 10),
        Row(
          children: [
            Icon(Icons.notifications, color: Colors.red),
            SizedBox(width: 10),
            Text('ددلاین: 3.5 روز دیگر'),
          ],
        ),
        SizedBox(height: 10),
        Text('زمان تخمینی باقی‌مانده: 5 ساعت'),
        SizedBox(height: 10),
        Text('توضیحات:'),
        SizedBox(height: 5),
        TextField(
          decoration: InputDecoration(
            border: OutlineInputBorder(),
            hintText: 'آشنایی با verilog و مدارهای آسنکرون',
          ),
          maxLines: 3,
        ),
        SizedBox(height: 20),
        Text('نمره: ثبت نشده!'),
        SizedBox(height: 20),
        ElevatedButton.icon(
          onPressed: () {
            // Handle file upload
          },
          icon: Icon(Icons.upload_file),
          label: Text('بارگذاری تمرین'),
          style: ElevatedButton.styleFrom(backgroundColor: Colors.pink),
        ),
        SizedBox(height: 20),
        Center(
          child: ElevatedButton(
            onPressed: () {
              // Handle submit
            },
            child: Text('ثبت', style: TextStyle(color: Colors.white)),
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.purple,
              minimumSize: Size(double.infinity, 56),
            ),
          ),
        ),
      ],
    );
  }
}
