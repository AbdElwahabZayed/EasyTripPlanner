package com.iti.mansoura.tot.easytripplanner.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {


    private String password;
    @NonNull
    @PrimaryKey
    private String uuid;

    @ColumnInfo
    private String userName ;

    @ColumnInfo
    private String email;

    public User(String email,String userName , String password) {
        this.email = email;
        this.password = password;
        this.userName = userName;
    }
    @Ignore
    public User(String email,String userName , String password , String uuid) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.uuid = uuid;
    }
    @Ignore
    public User(){

    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
