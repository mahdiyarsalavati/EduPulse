import 'dart:ui';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

import '../../Component/AnimatedButton.dart';
import '../../Component/SignInForm.dart';
import '../../Component/SignUpForm.dart';

class InitialPage extends StatefulWidget {
  const InitialPage({super.key});

  @override
  State<InitialPage> createState() => _InitialPageState();
}

class _InitialPageState extends State<InitialPage> {
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
          SafeArea(
            child: LayoutBuilder(
              builder: (context, constraints) {
                return SingleChildScrollView(
                  child: ConstrainedBox(
                    constraints:
                    BoxConstraints(minHeight: constraints.maxHeight),
                    child: IntrinsicHeight(
                      child: Padding(
                        padding: const EdgeInsets.all(50),
                        child: Center(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              Text(
                                "ادیوپالس",
                                style: TextStyle(
                                    fontFamily: "Badee",
                                    fontSize: 80,
                                    color: Colors.white),
                              ),
                              Text("برنامه مدیریت دانشگاهی",
                                  style: TextStyle(color: Colors.white)),
                              SizedBox(height: screenSize.width / 1.6),
                              Image.asset("assets/Backgrounds/banner.png"),
                              AnimatedButton(
                                press: () {
                                  showGeneralDialog(
                                    barrierDismissible: true,
                                    barrierLabel: "",
                                    context: context,
                                    pageBuilder: (context, _, __) => Center(
                                      child: Container(
                                        height: 750,
                                        margin: EdgeInsets.symmetric(
                                            horizontal: 16),
                                        padding: EdgeInsets.symmetric(
                                            vertical: 32, horizontal: 24),
                                        decoration: BoxDecoration(
                                            color:
                                            Colors.white.withOpacity(0.96),
                                            borderRadius: BorderRadius.all(
                                                Radius.circular(40))),
                                        child: Scaffold(
                                          backgroundColor: Colors.transparent,
                                          body: SingleChildScrollView(
                                            child: SizedBox(
                                              height: MediaQuery.of(context)
                                                  .size
                                                  .height -
                                                  200,
                                              child: Center(
                                                child: Column(
                                                  mainAxisSize:
                                                  MainAxisSize.min,
                                                  children: [
                                                    Text("ورود",
                                                        style: TextStyle(
                                                            fontFamily: "Badee",
                                                            fontSize: 50)),
                                                    SizedBox(height: 20),
                                                    Text(
                                                        "درصورتی که قبلا ثبت نام کرده اید، می توانید وارد شوید",
                                                        style: TextStyle(
                                                            fontSize: 15),
                                                        textAlign:
                                                        TextAlign.center),
                                                    SignIn(),
                                                    SizedBox(height: 20),
                                                    Row(
                                                      children: [
                                                        Expanded(
                                                            child: Divider()),
                                                        Text("یا",
                                                            style: TextStyle(
                                                                color: Colors
                                                                    .black45),
                                                            textAlign: TextAlign
                                                                .center),
                                                        Expanded(
                                                            child: Divider())
                                                      ],
                                                    ),
                                                    SizedBox(height: 10),
                                                    ElevatedButton.icon(
                                                      style: ElevatedButton
                                                          .styleFrom(
                                                          backgroundColor:
                                                          Colors
                                                              .blueAccent,
                                                          minimumSize: Size(
                                                              double
                                                                  .infinity,
                                                              56)),
                                                      onPressed: () {
                                                        showGeneralDialog(
                                                          barrierDismissible:
                                                          true,
                                                          barrierLabel: "",
                                                          context: context,
                                                          pageBuilder: (context,
                                                              _, __) =>
                                                              Center(
                                                                child: Container(
                                                                  height: 870,
                                                                  margin: EdgeInsets
                                                                      .symmetric(
                                                                      horizontal:
                                                                      16),
                                                                  padding: EdgeInsets
                                                                      .symmetric(
                                                                      vertical:
                                                                      32,
                                                                      horizontal:
                                                                      24),
                                                                  decoration: BoxDecoration(
                                                                      color: Colors
                                                                          .white
                                                                          .withOpacity(
                                                                          0.96),
                                                                      borderRadius:
                                                                      BorderRadius.all(
                                                                          Radius.circular(
                                                                              40))),
                                                                  child: Scaffold(
                                                                    backgroundColor:
                                                                    Colors
                                                                        .transparent,
                                                                    body: Center(
                                                                      child: Column(
                                                                        mainAxisSize:
                                                                        MainAxisSize
                                                                            .min,
                                                                        children: [
                                                                          Text(
                                                                              "ثبت نام",
                                                                              style: TextStyle(
                                                                                  fontFamily: "Badee",
                                                                                  fontSize: 50)),
                                                                          SizedBox(
                                                                              height:
                                                                              20),
                                                                          SignUp(),
                                                                        ],
                                                                      ),
                                                                    ),
                                                                  ),
                                                                ),
                                                              ),
                                                        );
                                                      },
                                                      icon: Icon(CupertinoIcons
                                                          .person_add),
                                                      label: Text("ثبت نام",
                                                          style: TextStyle(
                                                              color: Colors
                                                                  .white)),
                                                    )
                                                  ],
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),
                                  );
                                },
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

