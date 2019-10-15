package com.iti.mansoura.tot.easytripplanner.trip.show;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

public class ShowTripActivity extends AppCompatActivity {

    Presenter mypresenter;
    TextView start_point,end_point,edittime,editdate,notes,status;
    String  tstart,tend,ttime,tdate;
    int tstatus;
    String tnote;

    private AppCompatImageButton mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        initComponent();
        Trip trip = getIntent().getExtras().getParcelable("trip");
        mypresenter=new Presenter(trip);
        getTripDetails();
        fillActivity();
    }

    private void fillActivity() {
         start_point.setText(tstart);
         end_point.setText(tend);
         editdate.setText(tdate);
         edittime.setText(ttime);
         notes.setText(tnote);
         status.setText(String.valueOf(tstatus));
    }

    private void getTripDetails() {
        tstart= mypresenter.getTripSource_presenter();
        tdate=mypresenter.getTripDate_presenter();
        tend=mypresenter.getTripDestination_presenter();
        tnote=mypresenter.getNotes_presenter();
        ttime=mypresenter.getTriptime_presenter();
        tstatus=mypresenter.getTripstatus_presenter();
    }

    private void initComponent() {
        status =findViewById(R.id.trip_status);
        start_point=findViewById(R.id.start_point);
        end_point=findViewById(R.id.end_point);
        notes=findViewById(R.id.notes);
        edittime=findViewById(R.id.time);
        editdate=findViewById(R.id.date);
        mMap = findViewById(R.id.map);

        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // avoid=tfb t >> tolls , f >> ferry , b >> bicycle
                    Uri gmmIntentUri = Uri.parse("geo:"+mypresenter.getSourceLat_presenter()+","+mypresenter.getSourceLong_presenter()+"?z=10&q="+mypresenter.getDestinationLat_presenter()+","+mypresenter.getDestinationLong_presenter()+"&avoid=tb&mode=driving");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
                catch (ActivityNotFoundException e) {
                    try {
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.google.android.apps.maps")));
                    }
                    catch (ActivityNotFoundException a) {
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                    }

                    e.printStackTrace();
                }
            }
        });
    }
}
