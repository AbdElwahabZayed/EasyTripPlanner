package com.iti.mansoura.tot.easytripplanner.retorfit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionInterface {

    @GET("maps/api/directions/json?")
    Call<Direction> getDirection(@Query("")String query);
}
