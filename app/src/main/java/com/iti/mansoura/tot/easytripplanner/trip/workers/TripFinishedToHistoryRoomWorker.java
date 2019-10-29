package com.iti.mansoura.tot.easytripplanner.trip.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.iti.mansoura.tot.easytripplanner.db.TripDB.TripDataBase;
import com.iti.mansoura.tot.easytripplanner.db.TripDB.TripRepository;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

public class TripFinishedToHistoryRoomWorker extends Worker {

    private TripRepository tripRepository;

    public TripFinishedToHistoryRoomWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        TripDataBase.dbcontext = context;
        tripRepository = new TripRepository(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        String userUID = getInputData().getString("userUID");
        String tripUID = getInputData().getString("tripUID");
        addToHistory(userUID,tripUID);
        return Result.success();
    }

    private void addToHistory(String userUID,String tripUID)
    {
        // update local
        Trip mTrip = tripRepository.getUpComingTrip(userUID, tripUID);
        if(mTrip != null) {
            mTrip.setStatus(2);
            tripRepository.updateTrip(mTrip);
        }
        else{
            Log.e("history trip alert", "null");
        }
    }
}