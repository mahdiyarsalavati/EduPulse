import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'LoginPage.dart';
import 'SignupPage.dart';

class InitialPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var screenSize = MediaQuery.of(context).size;

    return Scaffold(
      resizeToAvoidBottomInset: true,
      body: Stack(
        children: [
          Container(
            decoration: BoxDecoration(
              image: DecorationImage(
                image: AssetImage("assets/Backgrounds/background.png"),
                fit: BoxFit.cover,
              ),
            ),
          ),
          Center(
            child: SafeArea(
              child: Column(
                children: [
                  SizedBox(height: screenSize.width / 10),
                  Text(
                    "ادیوپالس",
                    style: TextStyle(
                        fontFamily: "Badee", fontSize: 80, color: Colors.white),
                  ),
                  Text("برنامه مدیریت دانشگاهی",
                      style: TextStyle(color: Colors.white)),
                  SizedBox(height: screenSize.width / 2.6),
                  Image.asset("assets/Backgrounds/banner.png"),
                  SizedBox(
                    width: 300,
                    height: 60,
                    child: ElevatedButton.icon(
                      onPressed: () {
                        Navigator.of(context).push(
                          MaterialPageRoute(
                              builder: (context) => LoginPage()),
                        );
                      },
                      icon: Icon(CupertinoIcons.person_alt_circle),
                      label: Text("ورود",
                          style: TextStyle(fontFamily: "Abar", fontSize: 30)),
                    ),
                  ),
                  SizedBox(height: 20),
                  SizedBox(
                    width: 300,
                    height: 60,
                    child: ElevatedButton.icon(
                      onPressed: () {
                        Navigator.of(context).push(
                          MaterialPageRoute(
                              builder: (context) => SignupPage()),
                        );
                      },
                      icon: Icon(CupertinoIcons.person_add),
                      label: Text("ثبت نام",
                          style: TextStyle(fontFamily: "Abar", fontSize: 30)),
                    ),
                  )
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
