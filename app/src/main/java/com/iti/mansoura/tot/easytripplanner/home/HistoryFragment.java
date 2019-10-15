package com.iti.mansoura.tot.easytripplanner.home;


import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
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

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        historyTripsDB_ref = reference.child("History_Trips").getRef();
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = rootView.findViewById(R.id.rv);
        setupRecyclerView();
        return rootView;
        
    }

    @Override
    public void onStart() {
        super.onStart();
        historyTripsDB_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Trip> trips = new ArrayList<>();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Trip trip = child.getValue(Trip.class);
                    trips.add(trip);
                }
                System.out.println(""+trips.size());
                adapter.setDataSource(trips);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setupRecyclerView() {
        LinearLayoutManager mngr = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mngr);
        adapter = new HistoryAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

}