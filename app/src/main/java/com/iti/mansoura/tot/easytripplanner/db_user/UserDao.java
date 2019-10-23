package com.iti.mansoura.tot.easytripplanner.db_user;

import com.iti.mansoura.tot.easytripplanner.models.User;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void addUser(User user);

    @Query("select * from User where uuid = :uid")
    User getUser(String uid);
}
