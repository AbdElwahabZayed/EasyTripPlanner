package com.iti.mansoura.tot.easytripplanner.home.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.home.viewmodel.TripViewModel;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    FirebaseAuth mAuth;
    private HistoryAdapter adapter;
    private DatabaseReference historyTripsDB_ref;
    private RecyclerView recyclerView;
    private TripViewModel tripViewModel;

    public HistoryFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tripViewModel= ViewModelProviders.of(this).get(TripViewModel.class);
        tripViewModel.setContext(getActivity());
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        historyTripsDB_ref = reference.child("History_Trips").getRef();
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = rootView.findViewById(R.id.rv);
        setupRecyclerView();
        return rootView;

    }

    private void getHistoryTrips(String id) {

        tripViewModel.getAllHistoryTrips(id).observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                System.out.println("getAllHistoryTrips(id).observe(getActivity(),");
                adapter.setDataSource(trips);

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
       /* historyTripsDB_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Trip> trips = new ArrayList<>();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Trip trip = child.getValue(Trip.class);
                    if(trip.getUserUID().equals(mAuth.getCurrentUser().getUid()))
                        trips.add(trip);
                }
                System.out.println(""+trips.size());
                adapter.setDataSource(trips);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //adapter.notifyDataSetChanged();
        getHistoryTrips(mAuth.getUid());
    }

    @Override
    public void onResume() {
        super.onResume();
        //adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        LinearLayoutManager mngr = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mngr);
        adapter = new HistoryAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

}