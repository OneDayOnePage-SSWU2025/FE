package com.example.onedayonepaper.data.api;

import com.example.onedayonepaper.LoginActivity;
import com.example.onedayonepaper.data.dto.HomeResponse;
import com.example.onedayonepaper.data.dto.LoginRequest;
import com.example.onedayonepaper.data.dto.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/groups")
    Call<HomeResponse> getHomeGroups();
    @POST("/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}