package com.example.onedayonepaper.data.dto;

public class UpdateNicknameRequest {
    private String nickName;

    public UpdateNicknameRequest(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName(){
        return nickName;
    }
}
