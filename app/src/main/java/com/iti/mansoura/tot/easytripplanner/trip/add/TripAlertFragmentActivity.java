package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.iti.mansoura.tot.easytripplanner.R;

public class TripAlertFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);

        this.setFinishOnTouchOutside(false);

        String tripUID = getIntent().getExtras().getString("tripUID");
        String userUID = getIntent().getExtras().getString("userUID");
        String firebaseUID = getIntent().getExtras().getString("firebaseUID");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        TripAlertFragment tripAlertFragment = TripAlertFragment.newInstance(getResources().getString(R.string.trip_alert),tripUID,userUID,firebaseUID);
        if(getSupportFragmentManager().findFragmentByTag("trip_alert_fragment") == null)
        {
            ft.add(R.id.container,tripAlertFragment,"trip_alert_fragment");
        }
        else
        {
            ft.replace(R.id.container,tripAlertFragment,"trip_alert_fragment");
        }
        ft.commit();
    }
}
