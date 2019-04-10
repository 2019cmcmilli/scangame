package com.example.a1412023.InterStellarBookNights;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.a1412023.InterStellarBookNights.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;


public class ResultsFragment extends Fragment {

    private String code;

    private View mRootView;
    private Button mBackButton;
    private ProgressBar mLoadingIndicator;

    private String TAG = "Fragment_Results";

    public ResultsFragment(){}

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        mRootView = view;
        return view;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState){
        Log.v(TAG, "viewCreated");
        code = getArguments().getString("CODE");
        mLoadingIndicator = mRootView.findViewById(R.id.pb_loading_indicator);
        mBackButton = mRootView.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideResultFragment();

            }
        });
        Log.v(TAG, code);
        URL url = NetworkUtils.buildUrl(code);
        new OpenLibraryQueryTask().execute(url);
    }

    private class OpenLibraryQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*mLoadingIndicator.setVisibility(View.VISIBLE);
            mJsonText.setText("L o a d i n g\nl O a d i n g\nl o A d i n g\nl o a D i n g\nl o a d I n g\nl o a d i N g\nl o a d i n G\n");
            mCoverView.setVisibility(View.INVISIBLE);
            mCoverView.setImageResource(0);*/
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
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (searchResults != null && !searchResults.equals("")) {
                try {
                    JSONObject json = new JSONObject(searchResults);
                    //mJsonText.setText(json.toString(4));
                    //new DownloadImageTask((ImageView) mRootView.findViewById(R.id.iv_cover_photo)).execute(json.getString("coverurl"));
                    JSONArray jsarr = (JSONArray)json.get("subjs");
                    if (jsarr == null) { /*...*/ }
                    int[] subjs = new int[jsarr.length()];
                    for (int i = 0; i < jsarr.length(); ++i) {
                        subjs[i] = jsarr.optInt(i);
                    }
                    int sum = 1;
                    for(int i = 0; i < subjs.length; i++){
                        sum += subjs[i];
                    }
                    double[][] colors = {{221, 51, 51},{229, 141, 27},{247, 247, 46},{60, 242, 43},{42, 119, 241},{188, 35, 234}};
                    double[] color = {0, 0, 0};
                    for(int i = 0; i < subjs.length; i++){
                        for(int j = 0; j < 3; j++){
                            color[j] += colors[i][j] * ((double)subjs[i])/sum;
                        }
                    }
                    for(int j = 0; j < 3; ++j){
                        color[j] = Math.min((int)color[j], 255);
                    }
                    mRootView.setBackgroundColor(Color.rgb((int)color[0],(int)color[1],(int)color[2]));
                }catch (JSONException e){
                    //mJsonText.setText("oops");
                    e.printStackTrace();
                }
            } else {
                // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
                //mJsonText.setText("oop");
                Log.i(TAG, "No results");
            }
            mBackButton.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    /*
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            bmImage.setVisibility(View.INVISIBLE);
            bmImage.setImageResource(0);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmImage.requestLayout();
            bmImage.setVisibility(View.VISIBLE);
            Log.v(TAG, "Cover visible");
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }*/
}
