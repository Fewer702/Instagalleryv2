package com.example.parseinstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import com.example.parseinstagram.fragments.AccountFragment;
import com.example.parseinstagram.fragments.CameraFragment;
import com.example.parseinstagram.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseObject firstObject = new  ParseObject("FirstClass");
        firstObject.put("message","Hey ! First message from android. Parse is now connected");
        firstObject.saveInBackground(e -> {
            if (e != null){
                Log.e(TAG, e.getLocalizedMessage());
            }else{
                Log.d(TAG,"Object saved.");
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        // define fragments here
        final Fragment home = new HomeFragment();
        final Fragment camera = new CameraFragment();
        final Fragment account = new AccountFragment();


        // Bottom navigation icon selected
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = home;
                        break;

                    case R.id.addPic:
                        fragment = camera;
                        break;

                    case R.id.account:
                    default:
                        fragment = account;
//                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.home);

    }
}