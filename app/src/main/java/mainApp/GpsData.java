package mainApp;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Matteo on 04/12/2017.
 */
public class GpsData implements LocationListener {

    boolean isGPSEnabled = false; // flag for GPS satellite status
    boolean isNetworkEnabled = false; // flag for cellular network status
    boolean canGetLocation = false; // flag for either cellular or satellite status

    private static Context mContext;
    private Wifi wifi = null;

    public GpsData(Context context,Wifi wifi) {
        this.mContext = context;
        this.wifi = wifi;

    }

    @Override
    public void onLocationChanged(Location location) {
        this.updateCoordinates(location);
        wifi.updateValues();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        String text = "GPS enabled. Thank you!";
        Toast toast = Toast.makeText(mContext,text,Toast.LENGTH_LONG);
        toast.show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        String text = "Please, Enable your GPS!";
        Toast toast = Toast.makeText(mContext,text,Toast.LENGTH_LONG);
        toast.show();
    }


    public static void updateCoordinates(Location location) {

        Double lon = location.getLongitude();
        Double lat = location.getLatitude();
        Float accuracy = location.getAccuracy();

        TextView lat_tv = (TextView)((Activity)mContext).findViewById(R.id.latitude_value);
        lat_tv.setText(String.valueOf(lat));

        TextView lon_tv = (TextView)((Activity)mContext).findViewById(R.id.longitude_value);
        lon_tv.setText(String.valueOf(lon));

        TextView accuracy_tv = (TextView)((Activity)mContext).findViewById(R.id.accuracy_value);
        accuracy_tv.setText(String.valueOf(accuracy));

    }




}