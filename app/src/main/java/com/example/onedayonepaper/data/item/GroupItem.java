package com.example.onedayonepaper.data.item;

public class GroupItem {
    private int groupId;
    private String groupName;
    private int latestMemoPage;
    private BookItem book;

    public int getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public int getLatestMemoPage() { return latestMemoPage; }
    public BookItem getBook() { return book; }
}
