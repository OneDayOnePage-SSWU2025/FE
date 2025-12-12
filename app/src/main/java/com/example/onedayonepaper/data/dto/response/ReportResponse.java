package com.example.onedayonepaper.data.dto.response;

import com.example.onedayonepaper.data.item.ReportItem;

public class ReportResponse {

    private boolean success;
    private String message;
    private ReportItem data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }

    public ReportItem getData(){
        return data;
    }
}
