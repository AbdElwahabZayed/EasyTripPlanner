package com.iti.mansoura.tot.easytripplanner.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.util.List;

@Dao
public interface TripDao {
    @Insert
    void addTrip(Trip trip);

    @Query("select * from Trip")
    List<Trip> getAllTrips();

    @Query("select * from Trip")
    LiveData<List<Trip>> getUpCommingTripsByUserUID();

    @Query("select * from Trip")
    LiveData<List<Trip>> getHistoryTripsByUserUID();

    @Query("select * from Trip where userUID=:id and status=3 ")
    LiveData<List<Trip>> getDeletedTripsByUserUID(String id);

    @Update//pass the whole object and room will update the changed feild else if it was not found room will do nothing
    void updateTrip(Trip trip);

    @Delete
    void deleteTrip_FromDataBase(Trip trip);

    @Query("delete from Trip")
    void deleteAllUsers();

    @Query("select tripUID from Trip")
    List<String> getAllIDs();
}
