import 'package:dy/callback/dy_callback.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:dy/dy.dart';
import 'package:dy/dy_platform_interface.dart';
import 'package:dy/dy_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockDyPlatform with MockPlatformInterfaceMixin implements DyPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');



  @override
  Future<String?> loginInWithDouyin() {
    throw UnimplementedError();
  }

  @override
  Future<String?> initKey(String clientKey, String clientSecret) {
    throw UnimplementedError();
  }

  @override
  void addDyCallbackListener(DyCallBackListener callBackListener) {}

  @override
  Future<String?> reNewRefreshToken(String refreshToken) {
    throw UnimplementedError();
  }
}

void main() {
  final DyPlatform initialPlatform = DyPlatform.instance;

  test('$MethodChannelDy is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelDy>());
  });

  test('getPlatformVersion', () async {
    Dy dyPlugin = Dy();
    MockDyPlatform fakePlatform = MockDyPlatform();
    DyPlatform.instance = fakePlatform;

    expect(await dyPlugin.getPlatformVersion(), '42');
  });
}
