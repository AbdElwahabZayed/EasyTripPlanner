package com.iti.mansoura.tot.easytripplanner.retorfit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DurationInterface {

    @GET("maps/api/directions/json?")
    Call<List<User>> getDuration(@Query("")String query);
}
