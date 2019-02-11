package com.example.a1412023.scangametest1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import com.example.a1412023.scangametest1.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String TAG = "Activity_Main";
    public static final String ACTION_SCAN_BARCODE = "com.example.a1412023.scangametest1.action_scan_barcode";
    public static final int SCAN_REQUEST = 1;
    private TextView mTextView;
    private ProgressBar mLoadingIndicator;
    private TextView mJsonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView);
        mTextView.setText("Scan a code!");

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mJsonText = findViewById(R.id.tv_json);

        final Context context = this;
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, ScanningActivity.class);
                intent.setAction(ACTION_SCAN_BARCODE);
                startActivityForResult(intent, SCAN_REQUEST);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.w(TAG, "Result Cancelled");
        }
        else if (requestCode == SCAN_REQUEST) {
            Log.v(TAG, data.getStringExtra("CODE"));
            String raw = data.getStringExtra("CODE");
            //String formatted = raw.substring(3,4) + "-" + raw.substring(4,7) + "-" + raw.substring(7,12) + "-" + raw.substring(12,13);//NOT ALWAYS THIS
            mTextView.setText(raw);
            URL searchUrl = NetworkUtils.buildUrl(raw);
            new OpenLibraryQueryTask().execute(searchUrl);
        }
    }

    @Override
    public void onStop(){
        Log.v(TAG, "onStop");
        super.onStop();
    }
    @Override
    public void onStart(){
        Log.v(TAG, "onStart");
        super.onStart();
    }
    @Override
    public void onDestroy(){
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }

    public class OpenLibraryQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mJsonText.setText("L o a d i n g\nl O a d i n g\nl o A d i n g\nl o a D i n g\nl o a d I n g\nl o a d i N g\nl o a d i n G\n");
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            // COMPLETED (27) As soon as the loading is complete, hide the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (searchResults != null && !searchResults.equals("")) {
                // COMPLETED (17) Call showJsonDataView if we have valid, non-null results
                try {
                    JSONObject json = new JSONObject(searchResults);
                    mJsonText.setText(json.toString(4));
                }catch (JSONException e){
                    mJsonText.setText("oops");
                }
            } else {
                // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
                mJsonText.setText("oop");
            }
        }
    }
}
