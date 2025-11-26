package com.example.onedayonepaper.data.dto;

import java.util.List;

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
