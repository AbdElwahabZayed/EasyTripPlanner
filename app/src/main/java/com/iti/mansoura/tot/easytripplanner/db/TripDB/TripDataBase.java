package com.iti.mansoura.tot.easytripplanner.db.TripDB;

import android.content.Context;

import com.iti.mansoura.tot.easytripplanner.models.Trip;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Trip.class},version = 1,exportSchema = false)//will add user.class
public abstract class TripDataBase extends RoomDatabase {
public static Context dbcontext;
    public static TripDataBase getDataBaseInstance(){

        Builder<TripDataBase> tripDataBaseBuilder= Room.databaseBuilder(dbcontext, TripDataBase.class,"Trip.db");
        return tripDataBaseBuilder.build();
    }

    public abstract TripDao getDaoInstance();
}
