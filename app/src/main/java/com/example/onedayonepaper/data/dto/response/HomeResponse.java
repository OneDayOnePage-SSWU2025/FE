package com.example.onedayonepaper.data.dto.response;

import com.example.onedayonepaper.data.item.HomeGroupItem;

import java.util.List;

public class HomeResponse {
    private boolean success;
    private String message;
    private int totalCount;
    private List<HomeGroupItem> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getTotalCount() { return totalCount; }
    public List<HomeGroupItem> getData() { return data; }
}
