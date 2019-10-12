package com.example.zayed.easytripplanner.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zayed.easytripplanner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponent();


    }

    private void initComponent() {
        fab =findViewById(R.id.fab_AddNewTrip);
        fab.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_AddNewTrip:
                //startActivity(new Intent(Home.this,AddTrip.class));
                break;
        }
    }
}
