package com.example.onedayonepaper.data.dto.response;

import com.example.onedayonepaper.data.item.UserInfo;

public class UserInfoResponse {

    private boolean success;
    private String message;
    private UserInfo data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public UserInfo getData() { return data; }
}
