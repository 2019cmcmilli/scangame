package com.example.a1412023.scangametest1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class ResultsFragment extends Fragment {

    private String code;

    private String TAG = "Fragment_Results";

    public ResultsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        code = getArguments().getString("CODE");
        Log.v(TAG, code);
        URL url = NetworkUtils.buildUrl(code);
        new OpenLibraryQueryTask().execute(url);
    }

    private class OpenLibraryQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mJsonText.setText("L o a d i n g\nl O a d i n g\nl o A d i n g\nl o a D i n g\nl o a d I n g\nl o a d i N g\nl o a d i n G\n");
            mCoverView.setVisibility(View.INVISIBLE);
            mCoverView.setImageResource(0);
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
                    new DownloadImageTask((ImageView) findViewById(R.id.iv_cover_photo)).execute(json.getString("coverurl"));
                }catch (JSONException e){
                    mJsonText.setText("oops");
                }
            } else {
                // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
                mJsonText.setText("oop");
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

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
    }
}
