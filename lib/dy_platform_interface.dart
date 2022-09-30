import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'callback/dy_callback.dart';
import 'dy_method_channel.dart';

abstract class DyPlatform extends PlatformInterface {
  /// Constructs a DyPlatform.
  DyPlatform() : super(token: _token);

  static final Object _token = Object();

  static DyPlatform _instance = MethodChannelDy();

  /// The default instance of [DyPlatform] to use.
  ///
  /// Defaults to [MethodChannelDy].
  static DyPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [DyPlatform] when
  /// they register themselves.
  static set instance(DyPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  ///登录
  Future<String?> loginInWithDouyin() {
    throw UnimplementedError('loginInWithDouyin() has not been implemented.');
  }

  ///初始化key
  Future<String?> initKey(String clientKey, String clientSecret) {
    throw UnimplementedError('initKey() has not been implemented.');
  }

  ///刷新refreshToken
  Future<String?> reNewRefreshToken(String refreshToken) {
    throw UnimplementedError('renewRefreshToken() has not been implemented.');
  }

  /// 生成 client_token
  Future<String?> getClientToken() {
    throw UnimplementedError('getClientToken() has not been implemented.');
  }

  ///增加回调
  void addDyCallbackListener(DyCallBackListener callBackListener) {
    throw UnimplementedError(
        'addDyCallbackListener() has not been implemented.');
  }

  ///刷新AccessToken
  Future<String?> reNewAccessToken(String refreshToken) {
    throw UnimplementedError('reNewAccessToken() has not been implemented.');
  }

  ///分享去编辑页面
  Future<String?> shareToEditPage(List<String> imgPathList, List<String> videoPathList,bool shareToPublish) {
    throw UnimplementedError('shareToEditPage() has not been implemented.');
  }
}
