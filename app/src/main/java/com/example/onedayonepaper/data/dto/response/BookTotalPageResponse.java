package com.example.onedayonepaper.data.dto.response;

public class BookTotalPageResponse {

    private boolean success;
    private String message;
    private int data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getData() { return data; }
}
