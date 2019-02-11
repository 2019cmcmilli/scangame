/*package com.example.a1412023.scangametest1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import com.google.zxing.Result;
import com.google.zxing.BarcodeFormat;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanningActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private ViewGroup mContentHolderView;
    private View mManualView;
    private FloatingActionButton mModeToggle;
    private String TAG = "Activity_Scanning";
    private final int CAMERA_REQUEST_CODE = 1;
    private int viewMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ScanningActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
        mScannerView = new ZXingScannerView(this);
        mManualView = inflateManualView();
        mContentHolderView = (ViewGroup) findViewById(R.id.view_content_holder);
        mModeToggle = (FloatingActionButton) findViewById(R.id.fab_mode_toggle);
        mModeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "View mode set to " + (viewMode == 0 ? 1 : 0) + " from " + viewMode + " by FAB");
                setMode(viewMode == 0 ? 1 : 0);
            }
        });
        Log.v(TAG, "Scanning Activity created!");
    }
    @Override
    public void onResume() {
        super.onResume();
        if(viewMode == 0) {
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();          // Start camera on resume
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(viewMode == 0) {
            mScannerView.stopCamera();           // Stop camera on pause
        }
    }
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, "Code: " + rawResult.getText()); // Prints scan results
        Log.v(TAG, "Format: " + rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        //Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
        // If you would like to resume scanning, call this method below:
        if(rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13){
            Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
            Intent result = new Intent();
            result.putExtra("CODE", rawResult.getText());
            setResult(RESULT_OK, result);
            finish();
        }else{
            mScannerView.resumeCameraPreview(this);
            Toast.makeText(this, "Are you scanning the right kind of code?", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // If this is our permission request result.
        if(requestCode==CAMERA_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                setMode(0);
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                setMode(1);
            }
        }
    }

    private void setMode(int modeCode){ // This is one method only so I could say "mode code"
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
    }

    private View inflateManualView(){
        return View.inflate(this, R.layout.activity_scanning_manual_content, mContentHolderView);
    }
}
*/