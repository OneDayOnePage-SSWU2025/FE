package com.example.onedayonepaper.data.api;

import retrofit2.Retrofit;

public class AladinApiClient {

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.aladin.co.kr/ttb/api/")
                    .build();
        }
        return retrofit;
    }
}
