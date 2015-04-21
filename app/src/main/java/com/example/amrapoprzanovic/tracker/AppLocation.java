package com.example.amrapoprzanovic.tracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Created by amrapoprzanovic on 21/04/15.
 */
public class AppLocation {

    public static final String ACTION_TAG = "com.bitcamp.amra.location_update";

    private Context mAppContext;
    private LocationManager mLocationManager;

    private static AppLocation sAppLocation;


    private AppLocation(Context context) {
        mAppContext = context;
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);

    }

    public static AppLocation get(Context context){

        if( sAppLocation !=  null){
            sAppLocation = new AppLocation(context);
        }

        return  sAppLocation;
    }

    public void startsLocationTrack(){
        Intent broadcast =  new Intent(ACTION_TAG);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mAppContext,0, broadcast,0);
        //kada treba napravit flag = 0, kada be treba napravit flag =1;

        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                pendingIntent);



    }
}
