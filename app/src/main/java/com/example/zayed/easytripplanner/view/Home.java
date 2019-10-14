package com.example.zayed.easytripplanner.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.zayed.easytripplanner.FloatingWidgetService;
import com.example.zayed.easytripplanner.R;
import com.example.zayed.easytripplanner.add.AddTripActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class Home extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;
    FloatingWidgetService g;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textViewImg;
    TextView textViewEmail;
    FrameLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponent();
    }

    private void initComponent() {
        fab =findViewById(R.id.fab_AddNewTrip);
        fab.setOnClickListener(this);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navi);
        header=findViewById(R.id.header);
        toolbar=findViewById(R.id.toolbarHome);
        textViewImg=navigationView.getHeaderView(0).findViewById(R.id.txtViewImag);
        textViewEmail=navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
        textViewImg.setText("AZ");
        textViewEmail.setText("AbdElWhab_Zayed@yahoo.com");
        setSupportActionBar(toolbar);
        setMenu();
        setListeners();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 123);
            startService(new Intent(Home.this, FloatingWidgetService.class).putExtra("activity_background", true));

        }
    }

    private void setMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_foreground);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void setListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.sync:
                    drawerLayout.closeDrawers();
                    break;
                }


                return true;
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                //supportFinishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            startService(new Intent(Home.this, FloatingWidgetService.class).putExtra("activity_background", true));
            finish();
        }*/
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
