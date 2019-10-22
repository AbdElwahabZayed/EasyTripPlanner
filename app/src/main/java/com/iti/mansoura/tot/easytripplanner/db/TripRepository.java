package com.iti.mansoura.tot.easytripplanner.db;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mansoura.tot.easytripplanner.models.Trip;
import com.iti.mansoura.tot.easytripplanner.trip.add.TripSchedulingWorker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TripRepository {
    //List<Trip> tempList;
    private LiveData<List<Trip>> mutableLiveDataHistoryTrips;
    private LiveData<List<Trip>> mutableLiveDataUpComingTrips;
    private TripDataBase tripDataBase;
    private TripDao tripDao;
    private FirebaseAuth mAuth;
    private List<Trip> dataSet= new ArrayList<>();
    private List<Trip> tempList= new ArrayList<>();

    private WorkManager mWorkManager;
    private OneTimeWorkRequest mWorkRequest;

    //Fragment myFragment;
    Context myContext;
    public TripRepository(Context context){
        System.out.println("TripRepository");
        myContext=context;
        tripDataBase=TripDataBase.getDataBaseInstance();
        tripDao= tripDataBase.getDaoInstance();

    }

    public LiveData<List<Trip>> getAllUpComingTrips(String id){
        mutableLiveDataUpComingTrips=tripDao.getUpCommingTripsByUserUID();
        if(isNetworkConnected()) {
            uploadTOfirebaseThenputInroom();
        }
        return mutableLiveDataUpComingTrips;
    }

    private void deleteAllUsers() {
        new delete().execute();
    }

    private List<Trip> getUpComingFromFireBase(final String id) {

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
        }
        return dataSet;
    }

    public LiveData<List<Trip>> getAllHistoryTrips(String id){
        return tripDao.getHistoryTripsByUserUID();
    }

    /*private List<Trip> getHistoryFromFireBase(final String id) {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Trips").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        /*final Trip mTrip = childDataSnapshot.getValue(Trip.class);
                        if(mTrip.getStatus() == 2 /*&& mTrip.getUserUID().equals(id))
                            dataSet.add(mTrip);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                deleteTrip(mTrip);
                                addTrip(mTrip);
                            }
                        }).start();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("upComing " , "error"+databaseError.getMessage());
                }
            });
        }
        return dataSet;
    }*/

    public LiveData<List<Trip>> getAllDeletedTrips(String id){
        return  tripDao.getDeletedTripsByUserUID(id);
    }
    public void addTrip(Trip trip){
        new AddTrips().execute(trip);
    }

    public void addListOfTrips(List<Trip> trips){
        System.out.println("trips size "+trips.size());
        for (Trip t:trips) {
            new AddTrips().execute(t);
        }
    }
    public class AddTrips extends AsyncTask<Trip,Void,Void> {
        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.addTrip(trips[0]);
            // set alarm
            setReminder(trips[0].getTripTime(),trips[0].getTripDate(),trips[0].getTripUID());
            // calculate duration
            String duration = getTripDuration(new String[]{String.valueOf(trips[0].getSourceLat()),String.valueOf(trips[0].getSourceLong())} ,
                    new String []{String.valueOf(trips[0].getDestinationLat()) , String.valueOf(trips[0].getDestinationLong())});
            Log.e("duration" , duration);
            //TODO setup a worker to set trip to history
            return null;
        }
    }
    public void updateTrip(Trip trip){
        new UpdateTrips().execute(trip);
    }

    public class UpdateTrips extends AsyncTask<Trip,Void,Void> {
        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.updateTrip(trips[0]);
            return null;
        }
    }
    public void deleteTrip(Trip trip){
        new deleteTrips().execute(trip);
    }
    public class deleteTrips extends AsyncTask<Trip,Void,Void> {
        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.deleteTrip_FromDataBase(trips[0]);
            return null;
        }
    }

    private class delete extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            tripDao.deleteAllUsers();
            return null;
        }
    }
    public void uploadTOfirebaseThenputInroom(){
        new getAllTrips().execute();


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private class getAllTrips extends AsyncTask<Void,Void,List<Trip>> {
        @Override
        protected List<Trip> doInBackground(Void... voids) {
            System.out.println("getAllTrips");
            List<Trip> list=tripDao.getAllTrips();
            return  list;
        }

        @Override
        protected void onPostExecute(List<Trip> trips) {
            for(final Trip t:trips){
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Trips").child(t.getTripUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            reference.child("Trips").child(t.getTripUID()).setValue(t);
                        }else {
                            reference.child("Trips").child(t.getTripUID()).setValue(t);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                new GetAllDataFromFireBase().execute();
            }
        }
    }

    private class GetAllDataFromFireBase extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("asdfghghfgfsfadaasds");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Trips").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        final Trip mTrip = childDataSnapshot.getValue(Trip.class);
                        System.out.println("Trip "+mTrip.getTripTitle());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                deleteTrip(mTrip);
                                addTrip(mTrip);
                            }
                        }).start();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("upComing " , "error"+databaseError.getMessage());
                }
            });
            return null;
        }
    }

    private void setReminder(String tripTime,String tripDate,String tripUID)
    {
        // passing trip UID to WorkRequest
        Data data = new Data.Builder().putString("tripUID",tripUID ).build();

        mWorkManager = WorkManager.getInstance(myContext);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            try {
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("MM/dd/yy HH:mm")
                                .withLocale( Locale.getDefault() )
                                .withZone( ZoneId.systemDefault() );
                String currentFormatted = formatter.format( Instant.now() );

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm",Locale.getDefault());
                Instant start = sdf.parse(currentFormatted).toInstant();
                Instant end = sdf.parse(tripDate+" "+tripTime).toInstant();
                Duration duration = Duration.between(start, end);

//                Log.e("reminder start ", formatter.format( start ));
//                Log.e("reminder end ", formatter.format( end ));
//                Log.e("reminder duration ", ""+duration.getSeconds());

                mWorkRequest = new OneTimeWorkRequest
                        .Builder(TripSchedulingWorker.class)
                        .setInputData(data)
                        .setInitialDelay(duration)
                        .build();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.getDefault());

                long start = System.currentTimeMillis();
                start = TimeUnit.MILLISECONDS.toSeconds(start);

                Date mDate = sdf.parse(tripDate+" "+tripTime);
                long end = mDate.getTime();
                end = TimeUnit.MILLISECONDS.toSeconds(end);
                long difference = end - start;

//                Log.e("reminder start ", ""+start );
//                Log.e("reminder end ", ""+end);

                mWorkRequest = new OneTimeWorkRequest
                        .Builder(TripSchedulingWorker.class)
                        .setInputData(data)
                        .setInitialDelay(difference, TimeUnit.SECONDS)
                        .build();

            }
            catch (ParseException e) {
                Log.e("SR ", e.getMessage());
            }
        }

        mWorkManager.enqueue(mWorkRequest);
    }

    private String getTripDuration(String [] source , String [] destination)
    {
        String duration = "";
        //TODO request it from google using retrofit
        return duration;
    }
}
