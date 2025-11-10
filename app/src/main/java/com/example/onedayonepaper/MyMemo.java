package com.example.onedayonepaper;

public class MyMemo {
    private final String profileUrl;
    private final String content;
    private final String point;

    public MyMemo(String profileUrl, String content, String point) {
        this.profileUrl = profileUrl;
        this.content = content;
        this.point = point;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getContent() {
        return content;
    }

    public String getPoint() {
        return point;
    }
}
