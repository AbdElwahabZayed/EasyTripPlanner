package com.iti.mansoura.tot.easytripplanner.retorfit;

import com.iti.mansoura.tot.easytripplanner.retorfit.direction.Direction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionInterface {

    @GET("/maps/api/directions/json")
    Call<Direction> getDirection(@Query(value="origin")String origin, @Query(value="destination")String destination, @Query(value="mode")String mode, @Query(value="key")String key);
}
