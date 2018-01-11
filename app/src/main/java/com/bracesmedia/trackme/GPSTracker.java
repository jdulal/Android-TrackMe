package com.bracesmedia.trackme;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by jagadish on 1/10/2018.
 */

public class GPSTracker extends Service implements LocationListener {
   private final Context mContext;

   boolean isGPSEnabled = false;
   boolean isNetworkEnabled = false;
   boolean canGetLocation =  false;

   Location location;
   double latitude;
   double longitude;

   private static final long MIN_DISTANCE_FOR_UPDATES=10;
   private static final long MIN_TIME_BW_UPDATES = 100 * 60 * 1;

   protected LocationManager locationManager;

   public GPSTracker (Context context){
       this.mContext=context;
       getLocation();
   }

    public Location getLocation() {
       try{
           locationManager=(LocationManager) mContext.getSystemService(LOCATION_SERVICE);
           isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
           isNetworkEnabled =locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

           if(!isGPSEnabled && !isNetworkEnabled){

           }else{
               this.canGetLocation=true;
               if(isNetworkEnabled){
                   if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                       return null;
                   }
                   locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATES,this);
                   if(locationManager!=null){
                       location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                       if(location!=null){
                           latitude=location.getLatitude();
                           longitude=location.getLongitude();
                       }
                   }
               }

               if(isGPSEnabled){
                   if(location==null){
                       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATES,this);
                       if(locationManager!=null){
                           location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                           if(location!=null){
                               latitude=location.getLatitude();
                               longitude=location.getLongitude();
                           }
                       }
                   }
               }
           }

       }catch (Exception e){
           e.printStackTrace();
       }
        return location;
    }
    public void stopUsingGPS(){
       if(locationManager!=null){
           if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
               return;
           }
           locationManager.removeUpdates(GPSTracker.this);

           }
    }

    public double getLatitude(){
        if(location!=null){
            latitude=location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location!=null){
            longitude=location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("Please enable GPS setting. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Settings.ACTION_LOCALE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
