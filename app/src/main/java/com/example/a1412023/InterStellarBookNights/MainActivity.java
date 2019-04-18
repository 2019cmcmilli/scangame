package com.example.a1412023.InterStellarBookNights;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.a1412023.InterStellarBookNights.R;

public class MainActivity extends AppCompatActivity {
    //public static final String ACTION_SCAN_BARCODE = "com.example.a1412023.InterStellarBookNights.action_scan_barcode";
    public static final int SCAN_REQUEST = 1;
    private String TAG = "Activity_Main";

    private BottomNavigationView mBottomNav;

    private final Fragment fragmentInv = new InvFragment();
    private final Fragment fragmentHome = new HomeFragment();
    private final Fragment fragmentShelf = new ShelfFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment fragmentRes;
    private Fragment activeFragment = fragmentHome;

    private String resultPending = "";

    public Shelf shelf;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        mBottomNav = findViewById(R.id.navigation);
        mBottomNav.setSelectedItemId(R.id.navigation_home);
        mBottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, fragmentInv, "Ivn").hide(fragmentInv).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentHome, "Home").commit();
        fm.beginTransaction().add(R.id.main_container, fragmentShelf, "Shelf").hide(fragmentShelf).commit();

        shelf = new Shelf(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.v(TAG, "onStart");
        if(resultPending.length() > 0){
            Bundle result = new Bundle();
            result.putString("CODE", resultPending);
            fragmentRes = new ResultsFragment();
            fragmentRes.setArguments(result);
            fm.beginTransaction().add(R.id.main_container, fragmentRes, "Res").hide(activeFragment).commit();
            resultPending = "";
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.v(TAG, "onResume");
        if(resultPending.length() > 0){
            Bundle result = new Bundle();
            result.putString("CODE", resultPending);
            fragmentRes = new ResultsFragment();
            fragmentRes.setArguments(result);
            fm.beginTransaction().add(R.id.main_container, fragmentRes, "Res").hide(activeFragment).commit();
            resultPending = "";
        }
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

                case R.id.navigation_shelf:
                    fm.beginTransaction().hide(activeFragment).show(fragmentShelf).commit();
                    activeFragment = fragmentShelf;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        Log.v(TAG, "onSaveInstanceState");
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle){
        super.onRestoreInstanceState(bundle);
        Log.v(TAG, "onRestoreInstanceState");
    }

    public void hideResultFragment(){
        Log.v(TAG, "Hiding Results");
        fm.beginTransaction().remove(fragmentRes).show(activeFragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.w(TAG, "Result Cancelled");
        }else{
            if (requestCode == SCAN_REQUEST) {
                Log.v(TAG, data.getStringExtra("CODE"));
                String raw = data.getStringExtra("CODE");
                resultPending = raw;
            }
        }
    }
}
