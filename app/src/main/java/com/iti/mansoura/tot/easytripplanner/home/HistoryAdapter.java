package com.iti.mansoura.tot.easytripplanner.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {


    private final Context context;
    private List<Trip> friends;

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.C, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.nameText.setText(friend.getName());
        holder.phoneText.setText(friend.getPhone());
        holder.emailText.setText(friend.getEmail());
    }

    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }

    public void setDataSource(List<Friend> friends) {
        this.friends = friends;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView nameText, phoneText, emailText;

       public MyHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        MediaPlayer player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource("https://firebasestorage.googleapis.com/v0/b/fir-b9532.appspot.com/o/songs%2Fsong1.mp3?alt=media&token=a4424b28-93c3-4a0c-a6b9-9136dcf63335");
                        player.prepare();
                        player.start();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            });

        }
    }
}