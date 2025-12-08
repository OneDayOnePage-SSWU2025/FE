package com.example.onedayonepaper.data.dto.request;

public class ChangeBookRequest {
    private String imgUrl;
    private String bookTitle;
    private String author;
    private int totalPage;

    public ChangeBookRequest(String imgUrl, String bookTitle, String author, int totalPage) {
        this.imgUrl = imgUrl;
        this.bookTitle = bookTitle;
        this.author = author;
        this.totalPage = totalPage;
    }
}
