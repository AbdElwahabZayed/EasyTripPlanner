package com.example.zayed.easytripplanner.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.zayed.easytripplanner.FloatingWidgetService;
import com.example.zayed.easytripplanner.R;
import com.example.zayed.easytripplanner.add.AddTripActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;
    FloatingWidgetService g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponent();


    }

    private void initComponent() {
        fab =findViewById(R.id.fab_AddNewTrip);
        fab.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 123);
            startService(new Intent(Home.this, FloatingWidgetService.class).putExtra("activity_background", true));

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            startService(new Intent(Home.this, FloatingWidgetService.class).putExtra("activity_background", true));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_AddNewTrip:
                startActivity(new Intent(Home.this, AddTripActivity.class));
                break;
        }
    }
}
