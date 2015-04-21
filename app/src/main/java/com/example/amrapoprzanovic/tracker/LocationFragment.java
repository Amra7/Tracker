package com.example.amrapoprzanovic.tracker;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {


    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private TextView mAltitudeText;

    private ListView mAddressesList;

    private static final String TAG = "LocationReceiver";

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_location, container, false);

        mLatitudeText = (TextView) fragmentView.findViewById(R.id.text_view_latitude);
        mLongitudeText = (TextView) fragmentView.findViewById(R.id.text_view_longitude);
        mAltitudeText = (TextView) fragmentView.findViewById(R.id.text_view_altitude);

        mAddressesList = (ListView) fragmentView.findViewById(R.id.list_view_addresses);
        //TODO
        //add adapter and data

        //nasa aplikacija moze osluskivati promjene(na browseru, ako je nestalo baterije..)
        return fragmentView;
    }

    public void showList(Location location){

        String url = String.format(
                getActivity().getString(R.string.google_geocoder),
                location.getLatitude(),
                location.getLongitude()
        );

        ServiceRequest.request(
                url,
                new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            JSONArray resultArray = result.getJSONArray("results");

                            for (int i =0; i<resultArray.length(); i++){
                                JSONObject adderss = resultArray.getJSONObject(i);
                                Log.d("LocationApp", address.get("formatted_address"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private BroadcastReceiver mLocationReceiver = new LocationReceiver(){

        @Override
        public void locationReceived(Context context, Location location){
            //Log.d(TAG , "Ovo je iz one privatne");

            mLatitudeText.setText(
                    String.format("%.3f", location.getLatitude())
            );

            mLongitudeText.setText(
                    String.format("%.3f", location.getLongitude())
            );
            mAltitudeText.setText(
                    String.format("%.3f", location.getAltitude())
            );

            showList(location);
        }

        @Override
        public void locationNull(Context context, boolean gpsEnabled){
            //Log.d(TAG , "Ovo je iz one privatne");
            int messageId = gpsEnabled == true ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
        }
    };

    @Override
    public void onStart(){
        super.onStart();
        getActivity().registerReceiver(
                mLocationReceiver,
                new IntentFilter(AppLocation.ACTION_TAG)
        );
    }


}
