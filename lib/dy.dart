import 'package:dy/callback/dy_callback.dart';

import 'dy_platform_interface.dart';

class Dy {
  Future<String?> initKey(String clientKey, String clientSecret) {
    return DyPlatform.instance.initKey(clientKey, clientSecret);
  }

  Future<String?> getPlatformVersion() {
    return DyPlatform.instance.getPlatformVersion();
  }

  Future<String?> loginInWithDouyin(String scope) {
    return DyPlatform.instance.loginInWithDouyin(scope);
  }

  Future<String?> reNewRefreshToken(String refreshToken) {
    return DyPlatform.instance.reNewRefreshToken(refreshToken);
  }

  Future<String?> getClientToken() {
    return DyPlatform.instance.getClientToken();
  }

  void addDyCallbackListener(DyCallBackListener listener) {
    DyPlatform.instance.addDyCallbackListener(listener);
  }

  Future<String?> reNewAccessToken(String refreshToken) {
    return DyPlatform.instance.reNewAccessToken(refreshToken);
  }

  Future<dynamic> shareToEditPage(
      List<String> imgPathList,
      List<String> videoPathList,
      List<String> mHashTagList,
      bool shareToPublish,
      String mState,
      String appId,
      String appTitle,
      String description,
      String appUrl) {
    return DyPlatform.instance.shareToEditPage(
        imgPathList,
        videoPathList,
        mHashTagList,
        shareToPublish,
        mState,
        appId,
        appTitle,
        description,
        appUrl);
  }
}
