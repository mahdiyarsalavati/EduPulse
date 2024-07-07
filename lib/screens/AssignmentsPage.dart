import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:file_picker/file_picker.dart';

class AssignmentPage extends StatefulWidget {
  final String username;
  final Function onAssignmentCompleted;

  const AssignmentPage(
      {Key? key, required this.username, required this.onAssignmentCompleted})
      : super(key: key);

  @override
  _AssignmentPageState createState() => _AssignmentPageState();
}

class _AssignmentPageState extends State<AssignmentPage> {
  List<Assignment> _assignments = [];
  late Socket _socket;
  Map<String, bool> _isCompleted = {};
  Map<String, int> _estimatedTimes = {};

  @override
  void initState() {
    super.initState();
    _connectSocket();
  }

  void _connectSocket() async {
    _socket = await Socket.connect('127.0.0.1', 8280);
    _fetchAssignments();
  }

  void _fetchAssignments() {
    _socket.write('GET_ASSIGNMENTS ${widget.username}\n');
    _socket.flush();
    _socket.listen((data) {
      String response = String.fromCharCodes(data).trim();
      if (response.isNotEmpty) {
        List<String> assignmentDetails = response.split(' ASSIGNMENT ');
        setState(() {
          _assignments = assignmentDetails
              .where((detail) => detail.isNotEmpty)
              .map((detail) => Assignment.fromString(detail))
              .toList();
        });
        _loadCompletionStatus();
        _loadEstimatedTimes();
      }
    });
  }

  Future<void> _saveCompletionStatus(String name, bool isCompleted) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setBool(name, isCompleted);
    setState(() {
      _isCompleted[name] = isCompleted;
    });
    if (isCompleted) {
      widget.onAssignmentCompleted();
    }
  }

  Future<void> _loadCompletionStatus() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    for (var assignment in _assignments) {
      bool? status = prefs.getBool(assignment.name);
      if (status != null) {
        setState(() {
          _isCompleted[assignment.name] = status;
        });
      }
    }
  }

  Future<void> _saveEstimatedTime(String name, int estimatedTime) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setInt(name + "_estimated", estimatedTime);
    setState(() {
      _estimatedTimes[name] = estimatedTime;
    });
  }

  Future<void> _loadEstimatedTimes() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    for (var assignment in _assignments) {
      int? time = prefs.getInt(assignment.name + "_estimated");
      if (time != null) {
        setState(() {
          _estimatedTimes[assignment.name] = time;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('تمرین‌ها'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: ListView(
          children: _assignments.map((assignment) {
            bool isCompleted = _isCompleted[assignment.name] ?? false;
            int estimatedTime =
                _estimatedTimes[assignment.name] ?? assignment.estimatedTime;
            return GestureDetector(
              onTap: () =>
                  _showAssignmentDialog(assignment, isCompleted, estimatedTime),
              child: Stack(
                children: [
                  Card(
                    color: isCompleted ? Colors.grey : Colors.deepPurple,
                    child: Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        textDirection: TextDirection.rtl,
                        children: [
                          Text(
                            assignment.name,
                            style: TextStyle(
                              color: Colors.white,
                              fontSize: 20,
                              fontWeight: FontWeight.bold,
                            ),
                            textAlign: TextAlign.right,
                            textDirection: TextDirection.rtl,
                          ),
                          Divider(color: Colors.white, thickness: 2),
                          Text(
                            'مهلت: ${assignment.deadline}',
                            style: TextStyle(color: Colors.white, fontSize: 14),
                            textAlign: TextAlign.right,
                            textDirection: TextDirection.rtl,
                          ),
                          Text(
                            'زمان تخمینی: $estimatedTime ساعت',
                            style: TextStyle(color: Colors.white, fontSize: 14),
                            textAlign: TextAlign.right,
                            textDirection: TextDirection.rtl,
                          ),
                        ],
                      ),
                    ),
                  ),
                  if (isCompleted)
                    Positioned(
                      top: 8,
                      left: 8,
                      child: Icon(
                        CupertinoIcons.check_mark_circled_solid,
                        size: 40,
                        color: Colors.green,
                      ),
                    ),
                ],
              ),
            );
          }).toList(),
        ),
      ),
    );
  }

  void _showAssignmentDialog(
      Assignment assignment, bool isCompleted, int estimatedTime) {
    TextEditingController _estimatedTimeController =
        TextEditingController(text: estimatedTime.toString());
    TextEditingController _descriptionController = TextEditingController();
    File? selectedFile;

    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text('جزئیات تمرین', textAlign: TextAlign.center),
          content: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              textDirection: TextDirection.rtl,
              children: [
                Text("عنوان:"),
                Text(' ${assignment.name}'),
                SizedBox(height: 8),
                Text("مهلت:"),
                Text(' ${assignment.deadline}'),
                SizedBox(height: 8),
                Text("زمان تخمینی باقی‌مانده:"),
                TextField(
                  controller: _estimatedTimeController,
                  keyboardType: TextInputType.number,
                  textAlign: TextAlign.right,
                  textDirection: TextDirection.rtl,
                ),
                SizedBox(height: 8),
                Text('توضیحات:'),
                TextField(
                  controller: _descriptionController,
                  textAlign: TextAlign.right,
                  textDirection: TextDirection.rtl,
                ),
                SizedBox(height: 8),
                Text('نمره: ثبت نشده'),
                SizedBox(height: 8),
                ElevatedButton(
                  onPressed: () async {
                    FilePickerResult? result =
                        await FilePicker.platform.pickFiles(
                      type: FileType.custom,
                      allowedExtensions: ['pdf'],
                    );
                    if (result != null) {
                      selectedFile = File(result.files.single.path!);
                    }
                  },
                  child: Text('بارگذاری تمرین (PDF)'),
                ),
              ],
            ),
          ),
          actions: [
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
                if (selectedFile != null) {
                  _saveCompletionStatus(assignment.name, true);
                }
                _saveEstimatedTime(assignment.name,
                        int.parse(_estimatedTimeController.text))
                    .then((_) {
                  setState(() {
                    _estimatedTimes[assignment.name] =
                        int.parse(_estimatedTimeController.text);
                  });
                });
              },
              child: Text('ثبت'),
              style: ElevatedButton.styleFrom(
                foregroundColor: Colors.white,
                backgroundColor: Colors.deepPurple,
              ),
            ),
          ],
        );
      },
    );
  }
}

class Assignment {
  final String name;
  final String deadline;
  final int estimatedTime;

  Assignment(this.name, this.deadline, this.estimatedTime);

  factory Assignment.fromString(String str) {
    List<String> parts = str.trim().split(' ');
    return Assignment(parts[0], parts[1], int.parse(parts[2]));
  }
}
