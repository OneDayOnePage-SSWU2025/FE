package com.example.onedayonepaper.data.dto.request;

public class CreateGroupRequest {

    private String groupName;
    private int code;
    private String theme;
    private Book book;

    public static class Book {
        private String imgUrl;
        private String bookTitle;
        private String author;
        private int totalPage;

        public Book(String imgUrl, String bookTitle, String author, int totalPage) {
            this.imgUrl = imgUrl;
            this.bookTitle = bookTitle;
            this.author = author;
            this.totalPage = totalPage;
        }

    }

    public CreateGroupRequest(String groupName,
                              int code,
                              String theme,
                              String imgUrl,
                              String bookTitle,
                              String author,
                              int totalPage) {
        this.groupName = groupName;
        this.code = code;
        this.theme = theme;
        this.book = new Book(imgUrl, bookTitle, author, totalPage);
    }

}