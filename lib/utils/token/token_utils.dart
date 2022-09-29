import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:dy/conf/dyConf.dart';
import 'package:dy/utils/net/dio_utils.dart';
import 'package:flutter/foundation.dart';

class TokenUtils {
  factory TokenUtils() => _getInstance ??= TokenUtils._();

  TokenUtils._();

  static TokenUtils? _getInstance;

  Future<String> getAccessToken(String authCode) async {
    String resultStr = "";
    Response? resp = await DioUtils().request(
        url: "https://open.douyin.com/oauth/access_token/",
        params: {
          "client_secret": DyConf.clientSecret,
          "code": authCode,
          "grant_type": "authorization_code",
          "client_key": DyConf.clientKey,
        },
        onError: (error) {
          debugPrint("ly=> request AccessToken err is $error");
        });
    if (resp?.statusCode == 200) {
      dynamic data = resp?.data;
      resultStr = json.encode(data);
    }
    debugPrint("ly=> getAccessToken result is $resp");
    return Future.value(resultStr);
  }

  Future<String> reNewRefreshToken(String refreshToken) async {
    String newRefreshToken = "";
    Response? resp = await DioUtils().request(
        url: "https://open.douyin.com/oauth/renew_refresh_token/",
        params: {
          "refresh_token": refreshToken,
          "client_key": DyConf.clientKey,
        },
        contentType: "multipart/form-data",
        onError: (error) {
          debugPrint("ly=> request reNewRefreshToken err is $error");
        });
    if (resp?.statusCode == 200) {
      dynamic data = resp?.data;
      newRefreshToken = json.encode(data);
    }
    debugPrint("ly=> reNewRefreshToken result is $resp");
    return Future.value(newRefreshToken);
  }

  Future<String?> getClientToken() async {
    String clientToken = "";
    Response? resp = await DioUtils().request(
        url: "https://open.douyin.com/oauth/client_token/",
        params: {
          "grant_type": "client_credential",
          "client_key": DyConf.clientKey,
          "client_secret": DyConf.clientSecret,
        },
        contentType: "multipart/form-data",
        onError: (error) {
          debugPrint("ly=> request getClientToken err is $error");
        });
    if (resp?.statusCode == 200) {
      dynamic data = resp?.data;
      clientToken = json.encode(data);
    }
    debugPrint("ly=> getClientToken result is $resp");
    return Future.value(clientToken);
  }

  Future<String> reNewAccessToken(String refreshToken) async {
    String newRefreshToken = "";
    Response? resp = await DioUtils().request(
        url: "https://open.douyin.com/oauth/refresh_token/",
        params: {
          "refresh_token": refreshToken,
          "grant_type": "refresh_token",
          "client_key": DyConf.clientKey,
        },
        contentType: "application/x-www-form-urlencoded",
        onError: (error) {
          debugPrint("ly=> request reNewAccessToken err is $error");
        });
    if (resp?.statusCode == 200) {
      dynamic data = resp?.data;
      newRefreshToken = json.encode(data);
    }
    debugPrint("ly=> reNewAccessToken result is $resp");
    return Future.value(newRefreshToken);
  }
}
