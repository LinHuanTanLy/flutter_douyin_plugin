// To parse this JSON data, do
//
//     final resultModel = resultModelFromJson(jsonString);

import 'dart:convert';

ResultModel resultModelFromJson(String str) =>
    ResultModel.fromJson(json.decode(str));

String resultModelToJson(ResultModel data) => json.encode(data.toJson());

class ResultModel {
  ResultModel({
    this.code,
    this.errorMessage,
    this.result,
  });

  String? code;
  String? errorMessage;
  dynamic result;

  factory ResultModel.fromJson(Map<String, dynamic> json) => ResultModel(
        code: json["code"],
        errorMessage: json["errorMessage"],
        result: json["result"],
      );

  Map<String, dynamic> toJson() => {
        "code": code,
        "errorMessage": errorMessage,
        "result": result,
      };
}
