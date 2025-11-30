package com.example.onedayonepaper.data.dto.response;

public class BasicResponse {
    private boolean success;
    private String message;
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
