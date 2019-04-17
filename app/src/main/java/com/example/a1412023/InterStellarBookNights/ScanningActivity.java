package com.example.a1412023.InterStellarBookNights;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import com.example.a1412023.InterStellarBookNights.R;
import com.google.zxing.Result;
import com.google.zxing.BarcodeFormat;


import org.jetbrains.annotations.NotNull;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanningActivity extends AppCompatActivity{
    //private static ZXingScannerView mScannerView;
    private FloatingActionButton mModeToggle;
    private ToggleFragmentAdapter mFragmentAdapter;
    private ViewPager mViewPager;
    private static String TAG = "Activity_Scanning";
    private final int CAMERA_REQUEST_CODE = 1;
    private int viewMode = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ScanningActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
        mFragmentAdapter = new ToggleFragmentAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.vp_content_holder);
        mViewPager.setAdapter(mFragmentAdapter);
        mModeToggle = (FloatingActionButton) findViewById(R.id.fab_mode_toggle);
        mModeToggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "View mode set to " + (viewMode == 0 ? 1 : 0) + " from " + viewMode + " by FAB");
                mFragmentAdapter.setMode(viewMode == 0 ? 1 : 0);

            }
        });
        mFragmentAdapter.setMode(viewMode);
        Log.v(TAG, "Scanning Activity created!");
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // If this is our permission request result.
        if(requestCode==CAMERA_REQUEST_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                mFragmentAdapter.setMode(0);
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                mFragmentAdapter.setMode(1);
            }
        }
    }

    /*private void setMode(int modeCode){ // This is one method only so I could say "mode code"
        Log.d(TAG, "settingMode with code " + modeCode);
        viewMode = modeCode;
        switch(modeCode){
            case 0:
                mContentHolderView.removeAllViews();
                mContentHolderView.addView(mScannerView);
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
                mModeToggle.setImageResource(R.drawable.ic_dialpad);
                break;
            case 1:
                mContentHolderView.removeAllViews();
                mContentHolderView.addView(mManualView);
                mScannerView.stopCamera();
                mModeToggle.setImageResource(R.drawable.ic_camera);
                break;
        }
    }*/

    /*private View inflateManualView(){
        return View.inflate(this, R.layout.activity_scanning_manual_content, mContentHolderView);
    }*/


    private static boolean validateCode(String code){
        if(code == null){
            Log.v(TAG, "code is null");
            return false;
        }
        if(!code.substring(0, 3).equals("978") && !code.substring(0, 3).equals("979")){
            Log.v(TAG, "code not in Bookland, begins with " + code.substring(0, 3));
            return false;
        }
        if(code.length() != 13){
            Log.v(TAG, "code is length " + code.length());
            return false;
        }
        //checksum
        int check = 0;
        for(int i = 0; i < code.length() - 1; ++i){
            int digit = Integer.parseInt(code.substring(i, i+1));
            if(i % 2 == 0){
                check += digit;
                Log.v(TAG, ""+digit);
            }else{
                check += 3 * digit;
                Log.v(TAG, "3*"+digit);
            }
            Log.v(TAG, "\t"+check);
        }
        check = ((-check % 10) + 10) % 10; // WHY DOESN'T MOD WORK LIKE IT SHOULD IN SO MANY LANGUAGES AHHHHHHHHHHHHHHHHH
        if(check != Integer.parseInt(code.substring(12))){
            Log.v(TAG, "check should be " + check + ", was instead " + code.substring(12));
            return false;
        }
        Log.v(TAG, "code is good!");
        return true;
    }

    public static class ScanningViewFragment extends Fragment implements ZXingScannerView.ResultHandler{
        private ZXingScannerView mScannerView;
        public ScanningViewFragment(){}

        public static ScanningViewFragment newInstance(){
            Bundle args = new Bundle();
            ScanningViewFragment fragment = new ScanningViewFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            mScannerView = new ZXingScannerView(getActivity());
            return mScannerView;
        }

        @Override
        public void handleResult(Result rawResult) {
            // Do something with the result here
            Log.v(TAG, "Code: " + rawResult.getText()); // Prints scan results
            Log.v(TAG, "Format: " + rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
            //Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
            // If you would like to resume scanning, call this method below:
            if(rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13){
                if(validateCode(rawResult.getText())){
                    Toast.makeText(getActivity(), rawResult.getText(), Toast.LENGTH_SHORT).show();
                    Intent result = new Intent();
                    result.putExtra("CODE", rawResult.getText());
                    getActivity().setResult(RESULT_OK, result);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "Code invalid/Scanned incorrectly", Toast.LENGTH_SHORT).show();
                    mScannerView.resumeCameraPreview(this);
                }
            }else{
                mScannerView.resumeCameraPreview(this);
                Toast.makeText(getActivity(), "Are you scanning the right kind of code?", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onResume(){
            super.onResume();
            Log.v(TAG, "ScanningViewFragment resumed");
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();
        }
        @Override
        public void onPause(){
            super.onPause();
            Log.v(TAG, "ScanningViewFragment paused");
            mScannerView.stopCamera();
            Log.d(TAG, "onPause: camera stopped");
        }
    }

    public static class ManualEntryFragment extends Fragment {

        private EditText mManualEntryCode;
        private EditText mManualEntryTitle;
        private Button mManualEntrySubmit;

        public ManualEntryFragment() {
        }

        public static ManualEntryFragment newInstance() {
            Bundle args = new Bundle();

            ManualEntryFragment fragment = new ManualEntryFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.v(TAG, "Created manual entry fragment");
            return inflater.inflate(R.layout.activity_scanning_manual_content, container, false);
        }

        @Override
        public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
            mManualEntryCode   = (EditText) view.findViewById(R.id.et_manual_entry);
            mManualEntryTitle  = (EditText) view.findViewById(R.id.et_manual_title);
            mManualEntrySubmit = (Button)   view.findViewById(R.id.bt_manual_submit);

            mManualEntrySubmit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(mManualEntryCode.getText().length() < 13){
                        Toast.makeText(getActivity(), "Please enter the entire code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mManualEntryCode.getText().length() > 13){
                        Toast.makeText(getActivity(), "Code too long", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!validateCode(mManualEntryCode.getText().toString())){
                        Toast.makeText(getActivity(), "Please enter a valid ISBN-13 code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mManualEntryTitle.getText().length() == 0){
                        Toast.makeText(getActivity(), "Please enter a word from the title", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent result = new Intent();
                    result.putExtra("CODE", mManualEntryCode.getText().toString());
                    result.putExtra("TITLE", mManualEntryTitle.getText().toString());
                    getActivity().setResult(RESULT_OK, result);
                    getActivity().finish();
                }
            });
        }
    }

    public class ToggleFragmentAdapter extends FragmentPagerAdapter {

        public ToggleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                return ScanningViewFragment.newInstance(); //SCANNING FRAGMENT
            }
            if(position == 1) {
                return ManualEntryFragment.newInstance();
            }
            Log.e(TAG, "GOT BAD ITEM WITH POSITION "+position);
            return null;
        }

        public void setMode(int modeCode){
            Log.d(TAG, "setMode called with code "+modeCode);
            viewMode = modeCode;
            switch(modeCode){
                case 0:
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED){
                        // Permission is not granted
                        ActivityCompat.requestPermissions(ScanningActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                    }else{
                        mViewPager.setCurrentItem(0, false);
                        mModeToggle.setImageResource(R.drawable.ic_dialpad);
                        /*if(getItem(0).getView() != null) {
                            ((ZXingScannerView)getItem(0).getView()).startCamera();
                        }*/
                    }
                    break;
                case 1:
                    mViewPager.setCurrentItem(1, false);
                    mModeToggle.setImageResource(R.drawable.ic_camera);
                    /*if(getItem(0).getView() != null) {
                        ((ZXingScannerView)getItem(0).getView()).stopCamera();
                        Log.d(TAG, "setMode: camera stopped");
                    }else{
                        Log.d(TAG, "scanner is null");
                    }*/
                    break;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
