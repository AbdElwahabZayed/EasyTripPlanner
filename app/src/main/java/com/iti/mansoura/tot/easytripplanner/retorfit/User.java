package com.iti.mansoura.tot.easytripplanner.retorfit;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String mId ;
    @SerializedName("name")
    private String mName;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("profile_imge")
    private String mImage;

    public User()
    {

    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mMobile) {
        this.mMobile = mMobile;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }
}
