package com.example.a1412023.scangametest1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String ACTION_SCAN_BARCODE = "com.example.a1412023.scangametest1.action_scan_barcode";
    public static final int SCAN_REQUEST = 1;
    private String TAG = "Activity_Main";

    private BottomNavigationView mBottomNav;

    final Fragment fragmentInv = new InvFragment();
    final Fragment fragmentHome = new HomeFragment();
    final Fragment fragmentCraft = new CraftFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment activeFragment = fragmentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNav = findViewById(R.id.navigation);
        mBottomNav.setSelectedItemId(R.id.navigation_home);
        mBottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, fragmentInv, "Ivn").hide(fragmentInv).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentHome, "Home").commit();
        fm.beginTransaction().add(R.id.main_container, fragmentCraft, "Craft").hide(fragmentCraft).commit();

        final Context context = this;
        final Button button = findViewById(R.id.scan_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, ScanningActivity.class);
                intent.setAction(ACTION_SCAN_BARCODE);
                startActivityForResult(intent, SCAN_REQUEST);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_inv:
                    fm.beginTransaction().hide(activeFragment).show(fragmentInv).commit();
                    activeFragment = fragmentInv;
                    return true;

                case R.id.navigation_home:
                    fm.beginTransaction().hide(activeFragment).show(fragmentHome).commit();
                    activeFragment = fragmentHome;
                    return true;

                case R.id.navigation_craft:
                    fm.beginTransaction().hide(activeFragment).show(fragmentCraft).commit();
                    activeFragment = fragmentCraft;
                    return true;
            }
            return false;
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.w(TAG, "Result Cancelled");
        }
        else if (requestCode == SCAN_REQUEST) {
            Log.v(TAG, data.getStringExtra("CODE"));
            String raw = data.getStringExtra("CODE");
            // Start a results fragment here
            URL searchUrl = NetworkUtils.buildUrl(raw);
            new OpenLibraryQueryTask().execute(searchUrl);
        }
    }
}
