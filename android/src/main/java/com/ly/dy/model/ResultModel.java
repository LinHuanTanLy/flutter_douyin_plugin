package com.ly.dy.model;



public class ResultModel {
    private boolean ifSuccess;
    private String message;

    public ResultModel(boolean ifSuccess, String message) {
        this.ifSuccess = ifSuccess;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "ifSuccess:" + ifSuccess +
                ", message:'" + message + '\'' +
                '}';
    }
}
