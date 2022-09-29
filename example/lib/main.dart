import 'package:flutter/material.dart';
import 'package:dy/dy.dart';

const clientKey = 'awydmcyyh53uwo1v';
const clientSecret = 'ac1a816c62a84475bd27356f9af52fe1';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _dyPlugin = Dy();
  String? initKeyResult = "";
  String? loginInResult = "";

  final BoxDecoration _decoration = BoxDecoration(
    boxShadow: [BoxShadow(color: Colors.black.withAlpha(180))],
    borderRadius: BorderRadius.circular(8),
    color: Colors.white,
  );

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    Widget _buildItemWidget(
      String method,
      VoidCallback? onPressed,
      String result,
    ) =>
        Container(
            padding: const EdgeInsets.all(8),
            margin: const EdgeInsets.all(8),
            width: double.infinity,
            decoration: _decoration,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                TextButton(onPressed: onPressed, child: Text(method)),
                Text(result)
              ],
            ));

    return MaterialApp(
      home: Scaffold(
          backgroundColor: const Color(0xfff4f4f4),
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: Column(
            children: [
              _buildItemWidget("initKey", () async {
                initKeyResult =
                    await _dyPlugin.initKey(clientKey, clientSecret);
                debugPrint("the initKeyResult is $initKeyResult");
                setState(() {});
              }, "initKeyResult is $initKeyResult"),
              _buildItemWidget("loginInWithDy", () async {
                if (initKeyResult != "true") {
                  setState(() {
                    loginInResult = "initKey first";
                  });
                }
                loginInResult = await _dyPlugin.loginInWithDouyin();
              }, '$loginInResult')
            ],
          )),
    );
  }
}
