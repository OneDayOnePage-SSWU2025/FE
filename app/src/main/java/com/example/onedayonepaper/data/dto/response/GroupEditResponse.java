package com.example.onedayonepaper.data.dto.response;

import java.util.List;

public class GroupEditResponse {
    private int groupId;
    private String groupName;
    private int code;
    private String theme;
    private List<String> bookNames;

    public int getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public int getCode() { return code; }
    public String getTheme() { return theme; }
    public List<String> getBookNames() { return bookNames; }
}
