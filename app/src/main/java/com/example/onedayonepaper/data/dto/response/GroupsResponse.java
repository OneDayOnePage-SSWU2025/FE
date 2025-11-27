package com.example.onedayonepaper.data.dto.response;

import com.example.onedayonepaper.data.item.GroupItem;

import java.util.List;

public class GroupsResponse {
    private boolean success;
    private String message;
    private int totalCount;
    private List<GroupItem> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getTotalCount() { return totalCount; }
    public List<GroupItem> getData() { return data; }
}