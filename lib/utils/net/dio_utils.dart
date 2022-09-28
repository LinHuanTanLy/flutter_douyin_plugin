import 'package:dio/dio.dart';

enum REQ {
  POST,
  GET,
  PUT,
  DELETE,
}

class DioUtils {
  factory DioUtils() => _getInstance ??= DioUtils._();

  DioUtils._();

  static DioUtils? _getInstance;

  Dio? _dio;

  Dio get dio {
    _dio ??= Dio();
    return _dio!;
  }

  Future<Response>? request({
    REQ reqType = REQ.POST,
    required String url,
    required Map<String, dynamic> params,
    Function? onError,
  }) {
    if (reqType == REQ.POST) {
      return dio
          .post(url, queryParameters: params)
          .catchError(onError ?? () {});
    } else if (reqType == REQ.GET) {
      return dio.get(url, queryParameters: params).catchError(onError ?? () {});
    } else if (reqType == REQ.DELETE) {
      return dio
          .delete(url, queryParameters: params)
          .catchError(onError ?? () {});
    } else if (reqType == REQ.PUT) {
      return dio.put(url, queryParameters: params).catchError(onError ?? () {});
    } else {
      return dio
          .post(url, queryParameters: params)
          .catchError(onError ?? () {});
    }
  }
}
