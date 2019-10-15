package com.iti.mansoura.tot.easytripplanner.home.upcoming;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

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
        mRecyclerView.setAdapter(new TripsRecyclerViewAdapter(dataSet,this));
    }

    @Override
    public void onEmptyViewRetryClick() {
        //TODO Load from local
    }

    @Override
    public void onItemRowClick(int position) {
        //TODO show details Activity
    }

    @Override
    public void onStartClick(int position, Trip trip) {
        //TODO start trip and track it on google maps
    }
}
