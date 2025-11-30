package com.example.onedayonepaper.data.dto.response;

import com.example.onedayonepaper.ReadMemo;
import com.example.onedayonepaper.data.item.MemoItem;

import java.util.List;

public class MemoListResponse {
    private boolean success;
    private String message;
    private int totalCount;
    private List<ReadMemo> data;

    public List<ReadMemo> getData() {
        return data;
    }

    public int getTotalCount() { return totalCount; }
}
