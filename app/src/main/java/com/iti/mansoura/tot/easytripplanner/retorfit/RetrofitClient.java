package com.iti.mansoura.tot.easytripplanner.retorfit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{
    private static final String BASE_URL = "https://maps.googleapis.com/";
    private static RetrofitClient retrofitClient;
    private Retrofit retrofitInstance;

    private RetrofitClient()
    {
    }

    public static RetrofitClient getRetrofitClientInstance()
    {
        if(retrofitClient == null)
            retrofitClient = new RetrofitClient();

        return retrofitClient;
    }

    public Retrofit getRetrofit()
    {
        if(retrofitInstance == null)
        {
            retrofitInstance = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitInstance;
    }
}
