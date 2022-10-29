package com.ly.dy.model;


import java.util.HashMap;
import java.util.Map;

public class ResultModel {
    private int code;
    private Object result;
    private String errorMessage;

    public ResultModel() {
    }

    public ResultModel(int code, Object result, String errorMessage) {
        this.code = code;
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("result", result);
        map.put("errorMessage", errorMessage);
        return map;
    }

    public  String toJson(){
        return "{\n" +
                "   \"code\":\""+code+"\",\n" +
                "   \""+errorMessage+"\":\"\",\n" +
                "   \""+result+"\":\"\"\n" +
                "}";
    }
    @Override
    public String toString() {
        return "{" +
                "code:" + code +
                ", result:" + result +
                ", errorMessage:'" + errorMessage + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
