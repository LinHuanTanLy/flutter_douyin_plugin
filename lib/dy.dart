import 'package:dy/callback/dy_callback.dart';

import 'dy_platform_interface.dart';

class Dy {
  Future<String?> initKey(String clientKey, String clientSecret) {
    return DyPlatform.instance.initKey(clientKey, clientSecret);
  }

  Future<String?> getPlatformVersion() {
    return DyPlatform.instance.getPlatformVersion();
  }

  Future<String?> initSdk(String clientKey) {
    return DyPlatform.instance.initSdk(clientKey);
  }

  Future<String?> loginInWithDouyin() {
    return DyPlatform.instance.loginInWithDouyin();
  }

  void addDyCallbackListener(DyCallBackListener listener) {
    DyPlatform.instance.addDyCallbackListener(listener);
  }
}
