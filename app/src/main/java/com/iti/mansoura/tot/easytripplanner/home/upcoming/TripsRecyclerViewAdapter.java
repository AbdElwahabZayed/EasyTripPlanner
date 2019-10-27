package com.iti.mansoura.tot.easytripplanner.home.upcoming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.util.ArrayList;

public class TripsRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_EMPTY = 2;

    private ArrayList<Trip> dataSet=new ArrayList<>();
    private Callback mCallback;
    private ViewGroup viewGroup;
    private String [] mStatusArray,mTypeArray;
    private  Trip mTrip;

    public interface Callback {
        void onEmptyViewRetryClick();

        void onItemRowClick(int position, Trip trip);

        void onStartClick(int position ,Trip trip);
    }

    public TripsRecyclerViewAdapter(Callback mCallback ,String [] mStatusArray , String [] mTypeArray)
    {
        this.mCallback = mCallback;
        this.mStatusArray = mStatusArray;
        this.mTypeArray = mTypeArray;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataSet != null && dataSet.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewGroup=parent;
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new NormalViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item_card_layout, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_row_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if(dataSet != null && dataSet.size() > 0)
            return dataSet.size();
        else
            return 1;
    }

    public void setDataSource(ArrayList<Trip> trips) {
        //this.dataSet.clear();
//        ArrayList<Trip> ts=new ArrayList<>();
//        for(Trip t:trips) {
//            switch (t.getStatus()){
//                case 0:
//                    ts.add(t);
//                    break;
//            }
//        }
        this.dataSet=trips;
        notifyDataSetChanged();
    }

    public class NormalViewHolder extends BaseViewHolder {

        CardView container;
        AppCompatTextView mTripTitle , mTripSource , mTripDestination , mTripDate, mTripStatus ;
        AppCompatButton mStart;
        ConstraintLayout collapse;

        NormalViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            mTripTitle = itemView.findViewById(R.id.trip_title);
            mTripSource = itemView.findViewById(R.id.source);
            mTripDestination = itemView.findViewById(R.id.destination);
            mTripStatus = itemView.findViewById(R.id.status);
            mTripDate = itemView.findViewById(R.id.date);
            mStart = itemView.findViewById(R.id.start);
            collapse=itemView.findViewById(R.id.collapsePart);
        }

        protected void clear() {
            mTripTitle.setText("");
            mTripStatus.setText("");
            mTripDate.setText("");
            mTripSource.setText("");
            mTripDestination.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            mTrip = dataSet.get(position);

            if (mTrip.getTripTitle() != null) {
                mTripTitle.setText(mTrip.getTripTitle());
            }

            if (mTrip.getTripSource() != null) {
                mTripSource.setText(mTrip.getTripSource());
            }

            if (mTrip.getTripDestination() != null) {
                mTripDestination.setText(mTrip.getTripDestination());
            }

            if (mTrip.getTripDate() != null) {
                mTripDate.setText(mTrip.getTripDate());
            }

            switch (mTrip.getStatus())
            {
                case 0:
                    mTripStatus.setText(mStatusArray[0]);
                    break;
                case 1:
                    mTripStatus.setText(mStatusArray[1]);
                    break;
            }
            // set Click Listeners here

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onItemRowClick(getCurrentPosition(),mTrip);
                }
            });

            mStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onStartClick(getCurrentPosition(),mTrip);
                }
            });
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        AppCompatTextView empty;
        AppCompatButton retry;

        EmptyViewHolder(View itemView) {
            super(itemView);

            empty = itemView.findViewById(R.id.empty);
            retry = itemView.findViewById(R.id.retry);

            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onEmptyViewRetryClick();
                }
            });
        }

        @Override
        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
        }
    }
}
