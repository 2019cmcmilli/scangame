package com.example.a1412023.InterStellarBookNights;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1412023.InterStellarBookNights.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class ResultsFragment extends Fragment {

    private String code;
    private Book latestBook;

    private View mRootView;

    private ProgressBar mLoadingIndicator;
    private ImageView mCoverImage;
    private TextView mTitleText;
    private TextView mSubjectText;
    private TextView mPagesText;
    private TextView mPubdateText;
    private Button mShelfButton;
    private Button mRequestButton;
    private Button mDiscardButton;

    private String TAG = "Fragment_Results";

    private MainActivity mParentActivity;

    public ResultsFragment(){}

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof MainActivity){
            mParentActivity = (MainActivity) context;
        }else{
            Log.w(TAG, "No parent activity found for results fragment!");
        }
    }

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

        mCoverImage  = mRootView.findViewById(R.id.iv_result_cover);
        mTitleText   = mRootView.findViewById(R.id.tv_title);
        mSubjectText = mRootView.findViewById(R.id.tv_subject);
        mPubdateText = mRootView.findViewById(R.id.tv_pubdate);
        mPagesText   = mRootView.findViewById(R.id.tv_pages);


        mLoadingIndicator = mRootView.findViewById(R.id.pb_loading_indicator);
        mShelfButton = mRootView.findViewById(R.id.shelf_button);
        mShelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mParentActivity.shelf.addBook(latestBook)){
                    mParentActivity.hideResultFragment();
                }else{
                    Toast.makeText(mParentActivity, "Failed to add book", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRequestButton = mRootView.findViewById(R.id.request_quick_fulfill_button);
        //TODO: Request quick fulfill routine
        mDiscardButton = mRootView.findViewById(R.id.quick_discard_button);
        mDiscardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Confirmation on quick discard
                mParentActivity.hideResultFragment();
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
        protected void onPostExecute(String response) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (response != null && !response.equals("")) {
                try {
                    JsonParser parser = new JsonParser();
                    JsonObject obj = parser.parse(response).getAsJsonObject();
                    String coverurl = obj.get("coverurl").getAsString();
                    new DownloadImageTask(mCoverImage).execute(coverurl);
                    mTitleText.setText("Title: "+obj.get("title").getAsString());
                    JsonArray subjs = obj.get("subjs").getAsJsonArray();
                    String subjstr = "";
                    for(int i = 0; i < subjs.size(); ++i){
                        subjstr += subjs.get(i).getAsInt() + " ";
                    }
                    mSubjectText.setText("Subjects: "+subjstr);
                    mPagesText.setText("Pages: "+obj.get("pages").getAsInt()+"");
                    mPubdateText.setText("Published: "+obj.get("pubdate").getAsString());
                }catch (NullPointerException e){
                    //mJsonText.setText("oops");
                    e.printStackTrace();
                }
            } else {
                // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
                //mJsonText.setText("oop");
                Log.i(TAG, "No results");
            }
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
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
            Log.v(TAG, "Cover visible");
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mShelfButton.setVisibility(View.VISIBLE);
            if(!mParentActivity.shelf.hasRoom()){
                mDiscardButton.setVisibility(View.VISIBLE);
            }
            //TODO: Request applicability check
        }
    }
}
