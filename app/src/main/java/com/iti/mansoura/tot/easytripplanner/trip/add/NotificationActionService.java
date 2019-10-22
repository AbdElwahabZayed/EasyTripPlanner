package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

public class NotificationActionService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotificationActionService() {
        super(NotificationActionService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        String action = intent.getAction();
//        if (ACTION_1.equals(action)){
//        }
        int notificationID = intent.getExtras().getInt("notificationID");
        String tripUID = intent.getExtras().getString("tripUID");
//        Log.e("notification id: ","" + notificationID);
        NotificationManagerCompat.from(this).cancel(notificationID);
        startActivity(new Intent(this, TripAlertFragmentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("tripUID",tripUID));
    }
}
