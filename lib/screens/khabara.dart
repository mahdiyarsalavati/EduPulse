import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Khabara extends StatefulWidget {
  const Khabara({Key? key}) : super(key: key);

  @override
  _KhabaraState createState() => _KhabaraState();
}

class _KhabaraState extends State<Khabara> {
  final List<Map<String, dynamic>> _news = [
    {
      'date': 'امروز (16 اردیبهشت)',
      'title': 'اطلاعیه آموزشی',
      'description': 'قابل توجه دانشجویان دکترا...',
      'image': 'assets/university.jpg'
    },
    {
      'date': 'دیروز (15 اردیبهشت)',
      'title': 'اطلاعیه آموزشی',
      'description': 'قابل توجه دانشجویان دکترا...',
      'image': 'assets/university.jpg'
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('رویدادها و اخبار'),
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
              _buildSearchBar(),
              SizedBox(height: 20),
              _buildCategoryButtons(),
              SizedBox(height: 20),
              _buildNewsList(),
            ],
          ),
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: 1, // Highlight the News tab
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

  Widget _buildSearchBar() {
    return TextField(
      decoration: InputDecoration(
        prefixIcon: Icon(Icons.search),
        hintText: 'جستجو',
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
        ),
      ),
    );
  }

  Widget _buildCategoryButtons() {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Row(
        children: [
          _buildCategoryButton('اخبار'),
          _buildCategoryButton('رویدادها'),
          _buildCategoryButton('یادآوری‌ها'),
          _buildCategoryButton('تولدهای امروز'),
          _buildCategoryButton('تمدیدها'),
        ],
      ),
    );
  }

  Widget _buildCategoryButton(String title) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 4.0),
      child: ElevatedButton(
        onPressed: () {
          // Handle category button press
        },
        child: Text(title),
        style: ElevatedButton.styleFrom(
          foregroundColor: title == 'اخبار' ? Colors.white : Colors.black, backgroundColor: title == 'اخبار' ? Colors.blue : Colors.grey.shade200,
        ),
      ),
    );
  }

  Widget _buildNewsList() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: _news.map((newsItem) {
        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(newsItem['date'], style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            SizedBox(height: 10),
            _buildNewsItem(newsItem),
            SizedBox(height: 20),
          ],
        );
      }).toList(),
    );
  }

  Widget _buildNewsItem(Map<String, dynamic> newsItem) {
    return Card(
      color: Colors.pink.shade200,
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Image.asset(newsItem['image'], width: 80, height: 80, fit: BoxFit.cover),
            SizedBox(width: 10),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    newsItem['title'],
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  SizedBox(height: 8),
                  Text(
                    newsItem['description'],
                    style: TextStyle(fontSize: 14),
                  ),
                  SizedBox(height: 8),
                  Text(
                    'مطالعه بیشتر...',
                    style: TextStyle(fontSize: 14, color: Colors.blue),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
