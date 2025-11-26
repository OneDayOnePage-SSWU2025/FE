package com.example.onedayonepaper.data.api;

import com.example.onedayonepaper.data.dto.HomeResponse;
import com.example.onedayonepaper.data.dto.LoginRequest;
import com.example.onedayonepaper.data.dto.LoginResponse;
import com.example.onedayonepaper.data.dto.ReportResponse;
import com.example.onedayonepaper.data.dto.UpdateNicknameRequest;
import com.example.onedayonepaper.data.dto.UserInfoResponse;
import com.example.onedayonepaper.data.dto.UserUpdateResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @GET("/groups")
    Call<HomeResponse> getHomeGroups();
    @POST("/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @GET("/users/report")
    Call<ReportResponse> getReport();
    @GET("/users/my")
    Call<UserInfoResponse> getMyInfo();
    @Multipart
    @PATCH("/users/image")
    Call<UserUpdateResponse> updateProfileImage(
            @Part MultipartBody.Part image
    );
    @PATCH("/users/nickname")
    Call<UserUpdateResponse> updateNickname(@Body UpdateNicknameRequest request);


}