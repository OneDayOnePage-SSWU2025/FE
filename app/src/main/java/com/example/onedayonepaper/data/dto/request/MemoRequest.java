package com.example.onedayonepaper.data.dto.request;


public class MemoRequest {
    private int bookId;
    private int page;
    private String memo;

    public MemoRequest(int bookId, int page, String memo) {
        this.bookId = bookId;
        this.page = page;
        this.memo = memo;
    }
}
