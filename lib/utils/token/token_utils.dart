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
    Response? resp = await DioUtils()
        .request(url: "https://open.douyin.com/oauth/access_token/", params: {
      "client_secret": DyConf.clientSecret,
      "code": authCode,
      "grant_type": "authorization_code",
      "client_key": DyConf.clientKey,
    },onError: (error){
          debugPrint("ly=> request AccessToken err is $error");
    });
    if (resp?.statusCode == 200) {
      dynamic data = resp?.data;
      resultStr = json.encode(data);
    }
    debugPrint("ly=> getAccessToken result is $resp");
    return Future.value(resultStr);
  }
}
