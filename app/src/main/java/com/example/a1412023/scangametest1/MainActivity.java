package com.example.a1412023.scangametest1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
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
}
