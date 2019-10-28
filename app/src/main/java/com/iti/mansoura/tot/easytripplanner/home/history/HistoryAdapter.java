package com.iti.mansoura.tot.easytripplanner.home.history;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {


    private final Context context;
    private List<Trip> historytrips=new ArrayList<>();
    private ViewGroup viewGroup;
    public HistoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewGroup=parent;
        MyHolder myHolder=new MyHolder(LayoutInflater.from(context).inflate(R.layout.empty_row_item, parent, false));
        ;
        switch (viewType){
            case 2:
                myHolder=new MyHolder(LayoutInflater.from(context).inflate(R.layout.trip_card_layout, parent, false));
                break;
        }
        return myHolder;


    }

    @Override
    public int getItemViewType(int position) {
        int type=0;
        if (historytrips.get(position).getStatus()==2){
            type=2;
        }
        return type;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Trip trip=historytrips.get(position);
        if (trip.getStatus()==2 && holder.tripName!=null) {
            holder.tripName.setText(trip.getTripTitle().toUpperCase());
            holder.tripDate.setText(trip.getTripDate());
            holder.tripStatus.setText(trip.getTripType());
            holder.tripSource.setText(trip.getTripSource());
            holder.tripDes.setText(trip.getTripDestination());
        }
    }

    @Override
    public int getItemCount() {
        if (historytrips!=null)
            System.out.println(""+historytrips.size());
        return historytrips != null ? historytrips.size() : 0;
    }

    public void setDataSource(List<Trip> historytrips) {
        this.historytrips.clear();
        for(Trip t:historytrips) {
            if(t.getStatus()==2 && !this.historytrips.contains(t)) {
                System.out.println(""+t.getStatus());
                this.historytrips.add(t);
            }
        }
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView tripName, tripDate, tripStatus,tripDes,tripSource;
        Button btn_StartTrip;
        ConstraintLayout collapse;
        public MyHolder(final View itemView) {
            super(itemView);
            tripName=itemView.findViewById(R.id.textView_TripName);
            tripDes=itemView.findViewById(R.id.textView_Destination);
            tripDate=itemView.findViewById(R.id.textView_Date);
            tripSource=itemView.findViewById(R.id.textView_Source);
            tripStatus=itemView.findViewById(R.id.textView_ٍStatus);
            collapse=itemView.findViewById(R.id.collapsePart);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }
    }
}