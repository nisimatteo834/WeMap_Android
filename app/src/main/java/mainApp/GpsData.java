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

/**
 * Created by Matteo on 04/12/2017.
 */
public class GpsData implements LocationListener {

    boolean isGPSEnabled = false; // flag for GPS satellite status
    boolean isNetworkEnabled = false; // flag for cellular network status
    boolean canGetLocation = false; // flag for either cellular or satellite status

    private GpsStatus mGpsStatus;
    private static Context mContext;
    protected LocationManager locationManager;

    Location locationNetwork=null;
    Location locationGPS = null;// location
    double dLatitude, dAltitude, dLongitude, dAccuracy, dSpeed, dSats;
    float fAccuracy, fSpeed;
    long lSatTime;     // satellite time
    String szSignalSource, szAltitude, szAccuracy, szSpeed;

    public String szSatellitesInUse, szSatellitesInView;
    public static String szSatTime;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    private static final long MIN_TIME_BW_UPDATES = 1000; //1 second

    public GpsData(Context context) {
        this.mContext = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.updateCoordinates(location);
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

//        TextView siv = (TextView) findViewById(R.id.siv_value);
//        siv.setText(String.valueOf(this.getSatsInView(getApplicationContext())));
//
//        TextView siu = (TextView) findViewById(R.id.siu_value);
//        siu.setText(String.valueOf(this.getSatsInUse(getApplicationContext())));


    }




}