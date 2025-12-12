package com.example.onedayonepaper.data.api;

import com.example.onedayonepaper.data.dto.response.HomeResponse;
import com.example.onedayonepaper.data.dto.request.LoginRequest;
import com.example.onedayonepaper.data.dto.response.LoginResponse;
import com.example.onedayonepaper.data.dto.response.ReportResponse;
import com.example.onedayonepaper.data.dto.response.SignUpResponse;
import com.example.onedayonepaper.data.dto.request.UpdateNicknameRequest;
import com.example.onedayonepaper.data.dto.response.UserInfoResponse;
import com.example.onedayonepaper.data.dto.response.UserUpdateResponse;

import com.example.onedayonepaper.data.dto.request.CreateGroupRequest;
import com.example.onedayonepaper.data.dto.request.MemoRequest;
import com.example.onedayonepaper.data.dto.response.BasicResponse;
import com.example.onedayonepaper.data.dto.response.BookTotalPageResponse;
import com.example.onedayonepaper.data.dto.response.GroupEditResponse;
import com.example.onedayonepaper.data.dto.response.GroupsResponse;
import com.example.onedayonepaper.data.dto.response.MemoListResponse;
import com.example.onedayonepaper.data.dto.response.MyMemoResponse;
import com.example.onedayonepaper.data.dto.response.OwnerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @Multipart
    @POST("/users/signup")
    Call<SignUpResponse> signup(
            @Part("id") RequestBody id,
            @Part("password") RequestBody password,
            @Part("nickName") RequestBody nickName,
            @Part MultipartBody.Part img
    );

    @GET("/groups/detail")
    Call<GroupsResponse> getGroupDetail();

    @POST("/groups/join")
    Call<Void> joinGroup(
            @Query("groupName") String groupName,
            @Query("code") int code
    );

    @POST("/groups")
    Call<Void> createGroup(@Body CreateGroupRequest request);

    @GET("/books/total_page")
    Call<BookTotalPageResponse> getTotalPage(@Query("bookId") int bookId);
    @POST("/books/memo")
    Call<Void> postMemo(@Body MemoRequest request);

    @GET("/books/memo_by_page")
    Call<MemoListResponse> getMemoByPage(
            @Query("bookId") int bookId,
            @Query("page") int page
    );
    @GET("/books/my_memo")
    Call<MyMemoResponse> getMyMemo(@Query("bookId") int bookId);

    @DELETE("/books/{memoId}")
    Call<BasicResponse> deleteMemo(
            @Path("memoId") int memoId
    );

    @GET("/groups/isOwner")
    Call<OwnerResponse> isGroupOwner(@Query("groupId") int groupId);

    @POST("/books")
    Call<Void> changeBook(
            @Query("groupId") int groupId,
            @Body ChangeBookRequest request
    );

    @GET("/groups/forEdit")
    Call<GroupEditResponse> getGroupForEdit(@Query("groupId") int groupId);

}