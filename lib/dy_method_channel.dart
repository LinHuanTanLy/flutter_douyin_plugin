import 'package:dy/utils/token/token_utils.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'callback/dy_callback.dart';
import 'conf/dyConf.dart';
import 'dy_platform_interface.dart';

/// An implementation of [DyPlatform] that uses method channels.
class MethodChannelDy extends DyPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('dy');

  DyCallBackListener? _callBackListener;

  MethodChannelDy() {
    methodChannel.setMethodCallHandler((call) async {
      debugPrint("${call.method}---${call.arguments}");
      String method = call.method;
      switch (method) {
        case "getAccessToken":
          String authCode = call.arguments['authCode'];
          _callBackListener?.call("getAuthCode", authCode);
          String result = await TokenUtils().getAccessToken(authCode);
          _callBackListener?.call("getAccessToken", result);
          return Future.value(result);
        default:
          return Future.value(true);
      }
    });
  }

  @override
  void addDyCallbackListener(DyCallBackListener callBackListener) {
    _callBackListener = callBackListener;
  }

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String?> loginInWithDouyin() async {
    final result =
        await methodChannel.invokeMethod<String>('loginInWithDouyin');
    return result;
  }

  @override
  Future<String?> initKey(String clientKey, String clientSecret) async {
    DyConf.clientKey = clientKey;
    DyConf.clientSecret = clientSecret;
    final result = await methodChannel
        .invokeMethod<String>('initSdk', {"clientKey": clientKey});
    return result;
  }

  @override
  Future<String?> reNewRefreshToken(String refreshToken) {
    return TokenUtils().reNewRefreshToken(refreshToken);
  }

  @override
  Future<String?> getClientToken() {
    return TokenUtils().getClientToken();
  }

  @override
  Future<String?> reNewAccessToken(String refreshToken) {
    return TokenUtils().reNewAccessToken(refreshToken);
  }

  @override
  Future<String?> shareToEditPage(
      List<String> imgPathList,
      List<String> videoPathList,
      List<String> mHashTagList,
      bool shareToPublish,
      String mState,
      String appId,
      String appTitle,
      String description,
      String appUrl) async {
    final result = await methodChannel.invokeMethod<String>(
        shareToPublish ? 'shareToPublishPage' : 'shareToEditPage', {
      "imgPathList": imgPathList,
      "videoPathList": videoPathList,
      "mHashTagList": mHashTagList,
      "mState": mState,
      "appId": appId,
      "appTitle": appTitle,
      "description": description,
      "appUrl": appUrl,
    });
    return result;
  }
}
