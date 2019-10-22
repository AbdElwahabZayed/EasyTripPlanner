package com.iti.mansoura.tot.easytripplanner.home.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.iti.mansoura.tot.easytripplanner.db.TripRepository;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.util.List;

public class TripViewModel extends ViewModel {
    TripRepository tripRepository;

    public TripViewModel() {

    }
    public void setContext(Context context) {
        tripRepository=new TripRepository(context);
    }
    public LiveData<List<Trip>> getAllUpCommingTrips(String id){
        System.out.println("ViewModel getAllUpCommingTrips "+id);
        return tripRepository.getAllUpComingTrips(id);
    }
    public  LiveData<List<Trip>> getAllHistoryTrips(String id){
        return tripRepository.getAllHistoryTrips(id);
    }
    public  LiveData<List<Trip>> getAllDeletedTrips(String id){
        return tripRepository.getAllDeletedTrips(id);
    }
    public void addTrip(Trip trip){
        tripRepository.addTrip(trip);
    }
    public void updateTrip(Trip trip){
        tripRepository.updateTrip(trip);
    }
    public void deleteTrip_From_DB(Trip trip){
        tripRepository.deleteTrip(trip);
    }
    public void uploadTOfirebaseThenputInroom(){tripRepository.uploadTOfirebaseThenputInroom();}

}
