package com.iti.mansoura.tot.easytripplanner.models;

public class User {

    private String email , password , userName ,uuid;

    public User(String email,String userName , String password) {
        this.email = email;
        this.password = password;
        this.userName = userName;
    }

    public User(String email,String userName , String password , String uuid) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.uuid = uuid;
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
