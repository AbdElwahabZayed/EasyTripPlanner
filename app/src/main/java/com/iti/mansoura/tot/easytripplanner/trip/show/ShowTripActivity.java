package com.iti.mansoura.tot.easytripplanner.trip.show;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.db.TripDB.TripRepository;
import com.iti.mansoura.tot.easytripplanner.home.FloatingWidgetService;
import com.iti.mansoura.tot.easytripplanner.home.history.show_historyMap;
import com.iti.mansoura.tot.easytripplanner.models.Trip;
import com.iti.mansoura.tot.easytripplanner.trip.edit.EditTripActivity;
import com.iti.mansoura.tot.easytripplanner.trip.workers.TripToDeleteWorker;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class ShowTripActivity extends AppCompatActivity {

    private Presenter mypresenter;
    private TextView start_point,end_point,edittime,editdate,notes,status;
    private String  tstart,tend,ttime,tdate,tnote;
    private int tstatus;
    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton mMap, mEdit, mDelete;
    private Trip trip;
    private TripRepository tripRepository;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        trip = getIntent().getExtras().getParcelable("trip");

        mypresenter=new Presenter(trip);

        initComponent();
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
        toolbar = findViewById(R.id.toolbar);
        status =findViewById(R.id.trip_status);
        start_point=findViewById(R.id.start_point);
        end_point=findViewById(R.id.end_point);
        notes=findViewById(R.id.notes);
        edittime=findViewById(R.id.time);
        editdate=findViewById(R.id.date);
        materialDesignFAM = findViewById(R.id.material_design_android_floating_action_menu);
        mMap = findViewById(R.id.material_design_floating_action_menu_item1);
        mEdit = findViewById(R.id.material_design_floating_action_menu_item2);
        mDelete = findViewById(R.id.material_design_floating_action_menu_item3);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitleTextColor(Color.WHITE);

        tripRepository = new TripRepository(this);

        mMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(trip.getStatus()==0) {
                    showMap(trip.getNotes());
                    finishAffinity();
                }else if(trip.getStatus()==2){
                    String o = "";
                    o = o + "" + trip.getSourceLat() + "," + trip.getSourceLong();
                    String d = "";
                    d = d + "" + trip.getDestinationLat() + "," + trip.getDestinationLong();
                    startActivity(new Intent(ShowTripActivity.this, show_historyMap.class).putExtra("FromFAB",false).putExtra("Origin",o).putExtra("Destination",d));

                }
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ShowTripActivity.this , EditTripActivity.class)
                        .putExtra("tripStatus", trip.getStatus())
                        .putExtra("tripUID", trip.getTripUID())
                        .putExtra("firebaseUID", trip.getFirebaseUID()));

            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if(trip.getStatus()==0) {
                                    setTripToDeleted(trip.getTripUID(), trip.getUserUID());
                                }else{
                                    deleteHistoryTrip(trip);
                                }
                                onBackPressed();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog alertDialog=  new AlertDialog.Builder(ShowTripActivity.this)
                        .setTitle(getResources().getString(R.string.delete_trip))
                        .setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.no), dialogClickListener)
                        .create();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                }
                else {
                    alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
                }

                alertDialog.show();

            }
        });
    }

    private void deleteHistoryTrip(final Trip trip) {
        if(trip != null) {
            trip.setStatus(3);
            tripRepository.updateTrip(trip);
        }
        else {
            Log.e("delete trip fab", "null");
        }
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        if(trip!=null) {
            if(trip.getFirebaseUID().isEmpty() || trip.getFirebaseUID()==null){
                String tripFireBaseUID = UUID.randomUUID().toString();
                trip.setFirebaseUID(tripFireBaseUID);
            }
            reference.child("Trips").child(trip.getFirebaseUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    reference.child("Trips").child(trip.getFirebaseUID()).setValue(trip);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("delete trip fab", "onCancelled");
                }
            });
        }
    }

    private void showMap(String notes)
    {
        try {
            // avoid=tfb t >> tolls , f >> ferry , b >> bicycle
            // not working correctly
//                    Uri gmmIntentUri = Uri.parse("geo:"+mypresenter.getSourceLat_presenter()+","+mypresenter.getSourceLong_presenter()+"?z=10&q="+mypresenter.getDestinationLat_presenter()+","+mypresenter.getDestinationLong_presenter()+"&avoid=tb&mode=driving");
            // navigation intent
            Uri gmmIntentUri = Uri.parse("google.navigation:"+mypresenter.getSourceLat_presenter()+","+mypresenter.getSourceLong_presenter()+"?z=10&q="+mypresenter.getDestinationLat_presenter()+","+mypresenter.getDestinationLong_presenter()+"&avoid=tb");
            // working correctly but no start
//                    Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?z=10&saddr="+mypresenter.getSourceLat_presenter()+","+mypresenter.getSourceLong_presenter()+"&daddr="+mypresenter.getDestinationLat_presenter()+","+mypresenter.getDestinationLong_presenter()+"&avoid=tb&mode=driving");
//                    Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps/dir/?api=1&z=10&origin="+mypresenter.getSourceLat_presenter()+","+mypresenter.getSourceLong_presenter()+"&destination="+mypresenter.getDestinationLat_presenter()+","+mypresenter.getDestinationLong_presenter()+"&avoid=tb&mode=driving");
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            this.startService(new Intent(this, FloatingWidgetService.class).putExtra("activity_background", true).putExtra("notes",notes));
        }
    }

    private void setTripToDeleted(String tripUID , String userUID)
    {

        // update local
        Trip mTrip = tripRepository.getUpComingTrip(userUID, tripUID);
        if(mTrip != null) {
            mTrip.setStatus(3);
            tripRepository.updateTrip(mTrip);
        }
        else {
            Log.e("delete trip fab", "null");
        }

        // update firebase
        // passing trip UID to WorkRequest
        Data data = new Data.Builder().putString("firebaseUID",mTrip.getFirebaseUID() ).build();
        WorkManager mWorkManager = WorkManager.getInstance(this);

        Constraints mConstraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        OneTimeWorkRequest mWorkRequest = new OneTimeWorkRequest
                .Builder(TripToDeleteWorker.class)
                .setConstraints(mConstraints)
                .setInputData(data)
                .build();

        mWorkManager.enqueue(mWorkRequest);
    }
}
