package com.iti.mansoura.tot.easytripplanner.home.upcoming;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.home.FloatingWidgetService;
import com.iti.mansoura.tot.easytripplanner.home.viewmodel.TripViewModel;
import com.iti.mansoura.tot.easytripplanner.models.Trip;
import com.iti.mansoura.tot.easytripplanner.trip.show.ShowTripActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends Fragment implements TripsRecyclerViewAdapter.Callback {

    private RecyclerView mRecyclerView;
    private ArrayList<Trip> dataSet;
    private FirebaseAuth mAuth;
    private View myView;
    private Context context;
    private TripViewModel tripViewModel;
    private TripsRecyclerViewAdapter recyclerViewAdapter;
    private static boolean firstTime=true;

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
        myView=view;
        tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
        tripViewModel.setContext(this.getContext());
        if(firstTime) {
            tripViewModel.uploadTOfirebaseThenputInroom();
            firstTime=false;
        }
        mRecyclerView = view.findViewById(R.id.trips);
        dataSet = new ArrayList<>();
        setUpRecyclerView(view);

    }

    private void setUpRecyclerView(View view)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void prepareData() {
        mAuth = FirebaseAuth.getInstance();
        String [] mStatusArray = getResources().getStringArray(R.array.status_array);
        String [] mTypeArray = getResources().getStringArray(R.array.type_array);
        recyclerViewAdapter = new TripsRecyclerViewAdapter(UpComingFragment.this,mStatusArray,mTypeArray);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        tripViewModel.getAllUpComingTrips(mAuth.getCurrentUser().getUid()).observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                Collections.reverse(trips);
                recyclerViewAdapter.setDataSource((ArrayList<Trip>) trips);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareData();
        recyclerViewAdapter.notifyDataSetChanged();
        //recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        //recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEmptyViewRetryClick() {
        prepareData();
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
              // not working correctly
    //           Uri gmmIntentUri = Uri.parse("geo:"+mypresenter.getSourceLat_presenter()+","+mypresenter.getSourceLong_presenter()+"?z=10&q="+mypresenter.getDestinationLat_presenter()+","+mypresenter.getDestinationLong_presenter()+"&avoid=tb&mode=driving");
              // navigation intent
              Uri gmmIntentUri = Uri.parse("google.navigation:"+trip.getSourceLat()+","+trip.getSourceLong()+"?z=10&q="+trip.getDestinationLat()+","+trip.getDestinationLong()+"&avoid=tb");
              // working correctly but no start
    //          Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?z=10&saddr="+mypresenter.getSourceLat_presenter()+","+mypresenter.getSourceLong_presenter()+"&daddr="+mypresenter.getDestinationLat_presenter()+","+mypresenter.getDestinationLong_presenter()+"&avoid=tb&mode=driving");
    //          Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps/dir/?api=1&z=10&origin="+mypresenter.getSourceLat_presenter()+","+mypresenter.getSourceLong_presenter()+"&destination="+mypresenter.getDestinationLat_presenter()+","+mypresenter.getDestinationLong_presenter()+"&avoid=tb&mode=driving");
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

          if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(getContext())) {
                getContext().startService(new Intent(getContext(), FloatingWidgetService.class).putExtra("activity_background", true).putExtra("notes",trip.getNotes()));
          }

          getActivity().finishAffinity();
    }
}
