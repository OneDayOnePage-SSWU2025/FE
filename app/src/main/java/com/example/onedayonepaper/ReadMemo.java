package com.example.onedayonepaper;

public class ReadMemo {
    private final String profileUrl;
    private final String userName;
    private final String content;
    private final int page;

    public ReadMemo(String profileUrl, String userName, String content, int page) {
        this.profileUrl = profileUrl;
        this.userName = userName;
        this.content = content;
        this.page = page;
    }

    public String getProfileUrl() { return profileUrl; }
    public String getUserName() { return userName; }
    public String getContent() { return content; }
    public int getPage() { return page; }
}
