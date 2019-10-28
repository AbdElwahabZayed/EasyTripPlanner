package com.iti.mansoura.tot.easytripplanner.db.TripDB;

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
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.db_user.UserDataBase;
import com.iti.mansoura.tot.easytripplanner.models.Trip;
import com.iti.mansoura.tot.easytripplanner.retorfit.Direction;
import com.iti.mansoura.tot.easytripplanner.retorfit.DirectionInterface;
import com.iti.mansoura.tot.easytripplanner.retorfit.Leg;
import com.iti.mansoura.tot.easytripplanner.retorfit.RetrofitClient;
import com.iti.mansoura.tot.easytripplanner.retorfit.Route;
import com.iti.mansoura.tot.easytripplanner.retorfit.Duration;
import com.iti.mansoura.tot.easytripplanner.trip.add.TripSchedulingWorker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripRepository {

    private LiveData<List<Trip>> mutableLiveDataHistoryTrips;
    private LiveData<List<Trip>> mutableLiveDataUpComingTrips;
    private TripDataBase tripDataBase;
    private TripDao tripDao;
    private FirebaseAuth mAuth;
    private List<Trip> dataSet= new ArrayList<>();
    //private List<Trip> tempList= new ArrayList<>();
    private WorkManager mWorkManager;
    private OneTimeWorkRequest mWorkRequest;
    private int durationValue;

    //Fragment myFragment;
    Context myContext;
    public TripRepository(Context context){
        System.out.println("TripRepository");
        myContext=context;
        UserDataBase.dbcontext=context;
        tripDataBase=TripDataBase.getDataBaseInstance();
        tripDao= tripDataBase.getDaoInstance();

    }

    public LiveData<List<Trip>> getAllUpComingTrips(String id){
        mutableLiveDataUpComingTrips=tripDao.getUpComingTripsByUserUID(id);
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
        mutableLiveDataHistoryTrips=tripDao.getHistoryTripsByUserUID(id);
        return mutableLiveDataHistoryTrips;

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
            return null;
        }
    }

    public void addTripWithReminder(Trip trip){
        new AddTripWithReminder().execute(trip);
    }
    private class AddTripWithReminder extends AsyncTask<Trip,Void,Void> {
        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.addTrip(trips[0]);
            // set alarm
            setTripReminder(trips[0].getTripTime(),trips[0].getTripDate(),trips[0].getTripUID(),trips[0].getUserUID());
            // calculate duration
            int duration = getTripDuration(new String[]{String.valueOf(trips[0].getSourceLat()),String.valueOf(trips[0].getSourceLong())} ,
                    new String []{String.valueOf(trips[0].getDestinationLat()) , String.valueOf(trips[0].getDestinationLong())});
            Log.e("duration" , ""+duration);
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
        if(isNetworkConnected()) {
            new getAllTrips().execute();
        }
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
                if(t.getFirebaseUID()==null){
                    String tripFireBaseUID = UUID.randomUUID().toString();
                    t.setFirebaseUID(tripFireBaseUID);
                }
                System.out.println("dsfg  :"+t.getTripUID());
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Trips").child(t.getFirebaseUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            reference.child("Trips").child(t.getFirebaseUID()).setValue(t);
                        }else {
                            reference.child("Trips").child(t.getFirebaseUID()).setValue(t);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            new GetAllDataFromFireBase().execute();
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

    public Trip getUpComingTrip(String userID , String tripUID){
        try {
            return new getUpComingTrip().execute(userID,tripUID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getUpComingTrip extends AsyncTask<String,Void,Trip> {

        @Override
        protected Trip doInBackground(String... strings) {
            return tripDao.getUpComingTrip(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(Trip trip) {
            super.onPostExecute(trip);
        }
    }

    public Trip getUpComingTripToEdit(String userID , String tripUID){
        try {
            return new getUpComingTripToEdit().execute(userID,tripUID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getUpComingTripToEdit extends AsyncTask<String,Void,Trip> {

        @Override
        protected Trip doInBackground(String... strings) {
            return tripDao.getUpComingTripToEdit(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(Trip trip) {
            super.onPostExecute(trip);
        }
    }


    public Trip getHistoryTrip(String userID , String tripUID){
        try {
            return new getHistoryTrip().execute(userID,tripUID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getHistoryTrip extends AsyncTask<String,Void,Trip> {

        @Override
        protected Trip doInBackground(String... strings) {
            return tripDao.getHistoryTrip(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(Trip trip) {
            super.onPostExecute(trip);
        }
    }

    public Trip getHistoryTripToEdit(String userID , String tripUID){
        try {
            return new getHistoryTripToEdit().execute(userID,tripUID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getHistoryTripToEdit extends AsyncTask<String,Void,Trip> {

        @Override
        protected Trip doInBackground(String... strings) {
            return tripDao.getHistoryTripToEdit(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(Trip trip) {
            super.onPostExecute(trip);
        }
    }

    /**
     *
     * @param tripTime trip start time
     * @param tripDate trip date
     * @param tripUID trip unique identifier (id)
     */
    private void setTripReminder(String tripTime, String tripDate, String tripUID , String userUID)
    {
        // passing trip UID to WorkRequest
        Data data = new Data.Builder().putString("tripUID",tripUID ).putString("userUID",userUID ).build();
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
                java.time.Duration duration = java.time.Duration.between(start, end);

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

    /**
     *
     * @param source source city , lat and long
     * @param destination destination city , lat and long
     * @return the trip duration
     */
    private int getTripDuration(String [] source , String [] destination)
    {
        DirectionInterface directionService = RetrofitClient.getRetrofitClientInstance()
                .getRetrofit().create(DirectionInterface.class);

        Call<Direction> fetchUsers = directionService.
                getDirection("origin="+source[0]+","+source[1]+"&destination="+destination[0]+","+destination[1]+"&mode=driving&key="+myContext.getResources().getString(R.string.google_api_key));

        fetchUsers.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                Direction direction = response.body();
                if(direction != null)
                {
                    List<Route> routes = direction.getRoutes();
                    for (Route route: routes) {
                        List<Leg> legs = route.getLegs();
                        for (Leg leg: legs) {
                             Duration duration =leg.getDuration();
                             durationValue += duration.getValue();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {
                Log.e("Failed " , t.getMessage());
            }
        });
        return durationValue;
    }


}
