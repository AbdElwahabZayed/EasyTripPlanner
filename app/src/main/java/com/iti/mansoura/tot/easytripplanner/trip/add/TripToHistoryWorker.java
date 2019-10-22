package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TripToHistoryWorker extends Worker {

    public TripToHistoryWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String tripUID = getInputData().getString("tripUID");
        addToHistory(tripUID);
        return Result.success();
    }

    private void addToHistory(String tripUID)
    {
        //TODO process cancel trip from firebase & local
    }
}
