package com.iti.mansoura.tot.easytripplanner.home.upcoming;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.models.Trip;
import com.iti.mansoura.tot.easytripplanner.trip.show.ShowTripActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends Fragment implements TripsRecyclerViewAdapter.Callback {

    private RecyclerView mRecyclerView;
    private ArrayList<Trip> dataSet;
    private FirebaseAuth mAuth;

    public UpComingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_up_coming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.trips);
        dataSet = new ArrayList<>();
        setUpRecyclerView();
    }

    private void setUpRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        prepareData();
    }

    private void prepareData() {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Trips").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        Trip mTrip = childDataSnapshot.getValue(Trip.class);
                        if(mTrip.getStatus() == 0)
                            dataSet.add(mTrip);
                    }

                    TripsRecyclerViewAdapter recyclerViewAdapter = new TripsRecyclerViewAdapter(UpComingFragment.this);
                    mRecyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.setDataSource(dataSet);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("upComming " , "error"+databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onEmptyViewRetryClick() {
        //TODO Load from local
    }

    @Override
    public void onItemRowClick(int position, Trip trip) {
        Intent showActivity = new Intent(getActivity(), ShowTripActivity.class);
        showActivity.putExtra("trip",trip);
        startActivity(showActivity);
    }

    @Override
    public void onStartClick(int position, Trip trip) {
        try {
            // avoid=tfb t >> tolls , f >> ferry , b >> bicycle
            Uri gmmIntentUri = Uri.parse("geo:"+trip.getSourceLat()+","+trip.getSourceLong()+"?z=10&q="+trip.getDestinationLat()+","+trip.getDestinationLong()+"&avoid=tb&mode=driving");
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
}
