package com.iti.mansoura.tot.easytripplanner.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable
{
    private String tripUID ,tripTitle , tripType , tripSource ,  tripDestination , tripTime , tripDate , notes , userUID;
    private double sourceLat , sourceLong , destinationLat , destinationLong;
    private int status; // 0 not-started (upcoming) , 1 started , 2 history(finished) , 3 soft deleted (hidden from user)

    public Trip() {
    }

    public Trip(Parcel in) {
        tripUID = in.readString();
        tripTitle = in.readString();
        tripType = in.readString();
        tripSource = in.readString();
        userUID = in.readString();
        tripDestination = in.readString();
        tripDate = in.readString();
        tripTime = in.readString();
        sourceLat = in.readDouble();
        sourceLong = in.readDouble();
        destinationLat = in.readDouble();
        destinationLong = in.readDouble();
        status = in.readInt();
        notes = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTripUID() {
        return tripUID;
    }

    public void setTripUID(String tripUID) {
        this.tripUID = tripUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getTripSource() {
        return tripSource;
    }

    public void setTripSource(String tripSource) {
        this.tripSource = tripSource;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public double getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(double sourceLat) {
        this.sourceLat = sourceLat;
    }

    public double getSourceLong() {
        return sourceLong;
    }

    public void setSourceLong(double sourceLong) {
        this.sourceLong = sourceLong;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLong() {
        return destinationLong;
    }

    public void setDestinationLong(double destinationLong) {
        this.destinationLong = destinationLong;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tripUID);
        dest.writeString(tripTitle);
        dest.writeString(tripType);
        dest.writeString(tripSource);
        dest.writeString(userUID);
        dest.writeString(tripDestination);
        dest.writeString(tripDate);
        dest.writeString(tripTime);
        dest.writeDouble(sourceLat);
        dest.writeDouble(sourceLong);
        dest.writeDouble(destinationLat);
        dest.writeDouble(destinationLong);
        dest.writeString(notes);
        dest.writeInt(status);
    }
}
