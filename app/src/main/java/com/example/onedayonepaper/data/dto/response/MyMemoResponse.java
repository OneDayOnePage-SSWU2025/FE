package com.example.onedayonepaper.data.dto.response;

import com.example.onedayonepaper.data.item.MemoItem;

import java.util.List;

public class MyMemoResponse {
    private boolean success;
    private String message;
    private int totalCount;
    private List<MemoItem> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getTotalCount() { return totalCount; }
    public List<MemoItem> getData() { return data; }
}
