package com.example.onedayonepaper.data.dto;

public class UserInfoResponse {

    private boolean success;
    private String message;
    private UserInfo data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public UserInfo getData() { return data; }
}
