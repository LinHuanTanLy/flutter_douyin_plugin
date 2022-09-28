import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:dy/dy_method_channel.dart';

void main() {
  MethodChannelDy platform = MethodChannelDy();
  const MethodChannel channel = MethodChannel('dy');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
