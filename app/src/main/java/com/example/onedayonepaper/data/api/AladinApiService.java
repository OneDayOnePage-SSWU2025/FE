package com.example.onedayonepaper.data.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AladinApiService {

    @GET("ItemSearch.aspx")
    Call<ResponseBody> searchBooks(
            @Query("ttbkey") String ttbKey,
            @Query("Query") String query,
            @Query("QueryType") String queryType,
            @Query("SearchTarget") String target,
            @Query("start") int start,
            @Query("MaxResults") int maxResults,
            @Query("output") String output,
            @Query("Version") String version
    );

    @GET("ItemLookUp.aspx")
    Call<ResponseBody> lookupBook(
            @Query("ttbkey") String ttbKey,
            @Query("itemIdType") String itemIdType,
            @Query("ItemId") String itemId,
            @Query("output") String output,
            @Query("Version") String version,
            @Query("OptResult") String optResult
    );
}
