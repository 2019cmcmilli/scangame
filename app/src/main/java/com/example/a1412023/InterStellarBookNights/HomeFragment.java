package com.example.a1412023.InterStellarBookNights;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.a1412023.InterStellarBookNights.R;

public class HomeFragment extends Fragment {

    public static final String ACTION_SCAN_BARCODE = "com.example.a1412023.scangametest1.action_scan_barcode";
    public static final int SCAN_REQUEST = 1;
    private static final String TAG = "Fragment_Home";

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        final Context context = getContext();
        final Button scan_button = view.findViewById(R.id.scan_button);
        scan_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, ScanningActivity.class);
                intent.setAction(ACTION_SCAN_BARCODE);
                getActivity().startActivityForResult(intent, SCAN_REQUEST);
            }
        });
        final Button game_button = view.findViewById(R.id.game_button);
        game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Not available on this patch :(", Toast.LENGTH_SHORT).show();
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
            //mTextView.setText(raw);
            //URL searchUrl = NetworkUtils.buildUrl(raw);
            //new OpenLibraryQueryTask().execute(searchUrl);
        }
    }

}
