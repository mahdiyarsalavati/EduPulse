import 'package:flutter/material.dart';

class NewsPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('رویدادها و اخبار'),
        centerTitle: true,
      ),
      body: ListView(
        padding: EdgeInsets.all(16.0),
        children: [
          _buildNewsCard(
              context,
              'مسابقه نیوبیز',
              '/Users/mahdiyarsalavati/StudioProjects/EduPulse/frontend/assets/Backgrounds/newbies.png',
              '۱۲ تیر',
              "مسابقات برنامه‌نویسی NEWBIES 2024 در دانشکده مهندسی و علوم کامپیوتر، دانشگاه شهید بهشتی که با هدف ارتقای مهارت‌های برنامه‌نویسی برگزار شد."),
          _buildNewsCard(
              context,
              'اطلاعیه آموزشی',
              '/Users/mahdiyarsalavati/StudioProjects/EduPulse/frontend/assets/Backgrounds/newsPic.png',
              '15 اردیبهشت',
              "قابل توجه دانشجویان دکترا ورودی ۹۸ امکان حذف یک نیمسال بدون احتساب در سنوات..."),
        ],
      ),
    );
  }

  Widget _buildNewsCard(BuildContext context, String title, String imagePath,
      String date, String description) {
    return Card(
      color: Colors.deepPurple,
      margin: EdgeInsets.symmetric(vertical: 8.0),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(15.0),
      ),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Row(
          textDirection: TextDirection.rtl,
          children: [
            ClipRRect(
              borderRadius: BorderRadius.circular(15.0),
              child: Image.asset(
                imagePath,
                width: 100,
                height: 100,
                fit: BoxFit.cover,
              ),
            ),
            SizedBox(width: 8.0),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                textDirection: TextDirection.rtl,
                children: [
                  Text(
                    title,
                    style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                        color: Colors.white),
                    textAlign: TextAlign.center,
                  ),
                  SizedBox(height: 8.0),
                  Text(
                    description,
                    style: TextStyle(fontSize: 14, color: Colors.white),
                    textAlign: TextAlign.center,
                  ),
                  SizedBox(height: 8.0),
                  Text(
                    date,
                    style: TextStyle(fontSize: 12, color: Colors.grey),
                    textAlign: TextAlign.center,
                  ),
                  Align(
                    alignment: Alignment.center,
                    child: Text(
                      '...مطالعه بیشتر',
                      style: TextStyle(fontSize: 14, color: Colors.grey),
                      textAlign: TextAlign.center,
                    ),
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