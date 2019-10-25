package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TripSchedulingWorker extends Worker {

    private Context context;

    public TripSchedulingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        String tripUID = getInputData().getString("tripUID");
        String userUID = getInputData().getString("userUID");
        showDialogFragment(tripUID , userUID);
        return Result.success();
    }

    private void showDialogFragment(String tripUID , String userUID){
        context.startActivity(new Intent(context, TripAlertFragmentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("tripUID",tripUID).putExtra("userUID",userUID));
    }
}
