package com.iti.mansoura.tot.easytripplanner.home.viewmodel;

import android.content.Context;

import com.iti.mansoura.tot.easytripplanner.db.TripDB.TripRepository;
import com.iti.mansoura.tot.easytripplanner.db_user.UserRepo;
import com.iti.mansoura.tot.easytripplanner.models.Trip;
import com.iti.mansoura.tot.easytripplanner.models.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TripViewModel extends ViewModel {
    TripRepository tripRepository;
    UserRepo userRepo=new UserRepo();

    public TripViewModel() {

    }
    public void setContext(Context context) {
        tripRepository=new TripRepository(context);

    }
    public LiveData<List<Trip>> getAllUpComingTrips(String id){
        System.out.println("ViewModel getAllUpComingTrips "+id);
        return tripRepository.getAllUpComingTrips(id);
    }
    public  LiveData<List<Trip>> getAllHistoryTrips(String id){
        return tripRepository.getAllHistoryTrips(id);
    }
    public  LiveData<List<Trip>> getAllDeletedTrips(String id){
        return tripRepository.getAllDeletedTrips(id);
    }
    public void addTripWithReminder(Trip trip){
        tripRepository.addTripWithReminder(trip);
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
    public void addUser(User user){
        userRepo.addUser(user);
    }
    public User getUser(String uid){
        return userRepo.getUser(uid);
    }
    public LiveData<User> getUserLiveData(String uid){
        return userRepo.getUserLiveData(uid);
    }

}
