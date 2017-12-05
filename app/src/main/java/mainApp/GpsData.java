package mainApp;

import android.Manifest;
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

/**
 * Created by Matteo on 04/12/2017.
 */
public class GpsData extends Service implements LocationListener {

    boolean isGPSEnabled = false; // flag for GPS satellite status
    boolean isNetworkEnabled = false; // flag for cellular network status
    boolean canGetLocation = false; // flag for either cellular or satellite status

    private GpsStatus mGpsStatus;
    private final Context mContext;
    protected LocationManager locationManager;
    protected GpsListener gpsListener = new GpsListener();

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

    public GpsData(Context context) {
        this.mContext = context;
        getLocation();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class GpsListener implements GpsStatus.Listener{
        @Override
        public void onGpsStatusChanged(int event) {
        }
    }

    public Location getLocation() {

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            System.out.println("Permission denied GPS");

            } else {

            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);// getting GPS satellite status
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);// getting cellular network status
                if (!isGPSEnabled && !isNetworkEnabled) {
                } else {
                    this.canGetLocation = true;
                    if (isNetworkEnabled) {//GPS is enabled, getting lat/long via cellular towers
                        locationManager.addGpsStatusListener(gpsListener);//inserted new
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Cell tower", "Cell tower");
                        if (locationManager != null) {
                            locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (locationNetwork != null) {
                                szAltitude = " NA (using cell towers)";
                                szSatellitesInView = " NA (using cell towers)";
                                szSatellitesInUse = " NA (using cell towers)";
                            }
                        }
                    }
                    if (isGPSEnabled) {//GPS is enabled, gettoing lat/long via satellite
                        if (locationGPS == null) {
                            locationManager.addGpsStatusListener(gpsListener);//inserted new
                            locationManager.getGpsStatus(null);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (locationGPS != null) {
                                    dAltitude = locationGPS.getAltitude();
                                    szAltitude = String.valueOf(dAltitude);
                                    /**************************************************************
                                     * Provides a count of satellites in view, and satellites in use
                                     **************************************************************/
                                    mGpsStatus = locationManager.getGpsStatus(mGpsStatus);
                                    Iterable<GpsSatellite> satellites = mGpsStatus.getSatellites();
                                    int iTempCountInView = 0;
                                    int iTempCountInUse = 0;
                                    if (satellites != null) {
                                        for (GpsSatellite gpsSatellite : satellites) {
                                            iTempCountInView++;
                                            if (gpsSatellite.usedInFix()) {
                                                iTempCountInUse++;
                                            }
                                        }
                                    }
                                    szSatellitesInView = String.valueOf(iTempCountInView);
                                    szSatellitesInUse = String.valueOf(iTempCountInUse);
                                }
                            }
                        }
                    }


                    if (locationGPS != null && locationNetwork != null)
                    {
                        if (locationGPS.getAccuracy() < locationNetwork.getAccuracy())
                        {
                            return locationGPS;
                        }
                        else
                        {
                            return locationNetwork;
                        }


                    }

                    else if (locationGPS != null || locationNetwork!=null)
                    {
                        if (locationGPS!=null)
                        {
                            return locationGPS;
                        }

                        else
                        {
                            return locationNetwork;
                        }


                    }

                    else
                    {
                      return null;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }



            }
            return null;
        }

    public int getSatInView() {

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            System.out.println("Permission denied GPS");

        } else {

            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);// getting GPS satellite status
                    if (isGPSEnabled) {//GPS is enabled, gettoing lat/long via satellite
                        if (locationGPS == null) {
                            locationManager.addGpsStatusListener(gpsListener);//inserted new
                            locationManager.getGpsStatus(null);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (locationGPS != null) {
                                    dAltitude = locationGPS.getAltitude();
                                    szAltitude = String.valueOf(dAltitude);
                                    /**************************************************************
                                     * Provides a count of satellites in view, and satellites in use
                                     **************************************************************/
                                    mGpsStatus = locationManager.getGpsStatus(mGpsStatus);
                                    Iterable<GpsSatellite> satellites = mGpsStatus.getSatellites();
                                    int iTempCountInView = 0;
                                    int iTempCountInUse = 0;
                                    if (satellites != null) {
                                        for (GpsSatellite gpsSatellite : satellites) {
                                            iTempCountInView++;
                                            if (gpsSatellite.usedInFix()) {
                                                iTempCountInUse++;
                                            }
                                        }
                                    }
                                    szSatellitesInView = String.valueOf(iTempCountInView);
                                    szSatellitesInUse = String.valueOf(iTempCountInUse);
                                    return iTempCountInView;
                                }
                            }
                        }
                    }


            } catch (Exception e) {
                e.printStackTrace();
            }



        }
        return -1;
    }

    public int getSatInUse() {

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            System.out.println("Permission denied GPS");

        } else {

            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);// getting GPS satellite status
                if (isGPSEnabled) {//GPS is enabled, gettoing lat/long via satellite
                    if (locationGPS == null) {
                        locationManager.addGpsStatusListener(gpsListener);//inserted new
                        locationManager.getGpsStatus(null);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (locationGPS != null) {
                                dAltitude = locationGPS.getAltitude();
                                szAltitude = String.valueOf(dAltitude);
                                /**************************************************************
                                 * Provides a count of satellites in view, and satellites in use
                                 **************************************************************/
                                mGpsStatus = locationManager.getGpsStatus(mGpsStatus);
                                Iterable<GpsSatellite> satellites = mGpsStatus.getSatellites();
                                int iTempCountInView = 0;
                                int iTempCountInUse = 0;
                                if (satellites != null) {
                                    for (GpsSatellite gpsSatellite : satellites) {
                                        iTempCountInView++;
                                        if (gpsSatellite.usedInFix()) {
                                            iTempCountInUse++;
                                        }
                                    }
                                }
                                szSatellitesInView = String.valueOf(iTempCountInView);
                                szSatellitesInUse = String.valueOf(iTempCountInUse);
                                return iTempCountInUse;
                            }
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }



        }
        return -1;
    }



}