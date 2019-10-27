package com.iti.mansoura.tot.easytripplanner.trip.show;

import com.iti.mansoura.tot.easytripplanner.models.Trip;

public class Presenter {
    Trip trip_obj;

    String title,type,source,destination,date,time ,triptime;
    int status;
    double source_Lat,source_Long,destination_Lat,destination_Long;
    String note;

    public Presenter(Trip trip_obj)
    {
        this.trip_obj = trip_obj;
    }

    public String getTripTitle_presenter() {
      title=trip_obj.getTripTitle();

        return title;
    }

    public void setTripTitle_presenter(String tripTitle) {
        trip_obj.setTripTitle(tripTitle);
    }

    public String getTripType_presenter() {
       type=trip_obj.getTripType();
        return type;
    }

    public void setTripType_presenter(String tripType) {
        trip_obj.setTripType(tripType);
    }

    public String getTripSource_presenter() {

        source=trip_obj.getTripSource();
        return source;
    }

    public void setTripSource_presenter(String tripSource) {
        trip_obj.setTripSource(tripSource);
    }

    public String getTripDestination_presenter() {
        destination=trip_obj.getTripDestination();
        return destination;
    }

    public void setTripDestination_presenter(String tripDestination) {
        trip_obj.setTripDestination(tripDestination);
    }

    public String getTripDate_presenter() {
        date=trip_obj.getTripDate();
        return date;
    }

    public void setTripDate_presenter(String tripDate) {
        trip_obj.setTripDate(tripDate);
    }

    public double getSourceLat_presenter() {
        source_Lat=trip_obj.getSourceLat();
        return source_Lat;
    }

    public void setSourceLat_presenter(double sourceLat) {
        trip_obj.setSourceLat(sourceLat);
    }

    public double getSourceLong_presenter() {
        source_Long=trip_obj.getSourceLong();
        return source_Long;
    }

    public void setSourceLong_presenter(double sourceLong) {
        trip_obj.setSourceLong(sourceLong);
    }

    public double getDestinationLat_presenter() {
        destination_Lat=trip_obj.getDestinationLat();
        return destination_Lat;
    }

    public void setDestinationLat_presenter(double destinationLat) {
        trip_obj.setDestinationLat(destinationLat);
    }

    public double getDestinationLong_presenter() {
        destination_Long=trip_obj.getDestinationLong();
        return destination_Long;
    }

    public void setDestinationLong_presenter(double destinationLong) {
        trip_obj.setDestinationLong(destinationLong);
    }

    public String getNotes_presenter() {
        note=trip_obj.getNotes();
        return note;
    }

    public void setNotes_presenter(String notes) {
        trip_obj.setNotes(notes);
    }

    public String getTriptime_presenter() {
        time=trip_obj.getTripTime();

        return time;

    }

    public void setTriptime_presenter(String triptime) {

        trip_obj.setTripTime(triptime);
    }

    public int getTripstatus_presenter() {
        status=trip_obj.getStatus();
        return status;
    }

    public void setTripstatus_presenter(int tripstatus) {
        trip_obj.setStatus(tripstatus);
    }
}
