package com.example.onedayonepaper.data.api;

import com.example.onedayonepaper.data.dto.HomeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {
    @GET("/groups")
    Call<HomeResponse> getHomeGroups(@Header("Authorization") String token);
}