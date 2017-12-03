package mainApp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockApplication;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import mainApp.R;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,LocationListener {
    LocationManager locationManager = null;
    WifiManager wifi = null;
    String locationProvider = LocationManager.GPS_PROVIDER;
    public Location location;
    public double longitude;
    public double latitude;
    public boolean isGPSEnabled;
    public boolean isNetworkEnabled;
    public boolean locationServiceAvailable;
    int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 150;


    //boh
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;

    //private static LocationService instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        this.initLocationService(MainActivity.this);
        this.updateValues();
//        new SpeedTestTask().execute();
//        System.out.println("Prova" + SpeedTestTask.bitrate);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void onResume() {
        super.onResume();
        this.updateValues();
        this.initLocationService(getApplicationContext());

    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private void updateValues() {

        this.checkWifi();
        this.checkGPS();
//wifi values updating
        String ssid = wifi.getConnectionInfo().getSSID();
        String ip = Formatter.formatIpAddress(wifi.getConnectionInfo().getIpAddress());
        int speed = wifi.getConnectionInfo().getLinkSpeed();
        int rssi = wifi.getConnectionInfo().getRssi();
        String gw = Formatter.formatIpAddress(wifi.getDhcpInfo().gateway);


        TextView ssid_value = (TextView) findViewById(R.id.SSID_value);
        ssid_value.setText(ssid);

        TextView ip_value = (TextView) findViewById(R.id.ip_value);
        ip_value.setText(ip);

        TextView speed_value = (TextView) findViewById(R.id.speed_value);
        String s = String.valueOf(speed) + " Mbit/s";
        speed_value.setText(s);


        TextView mac_value = (TextView) findViewById(R.id.MAC_value);
        mac_value.setText(Wifi.getMacAddr());

        TextView mac_ap = (TextView) findViewById(R.id.apMAC_value);
        mac_ap.setText(wifi.getConnectionInfo().getBSSID());

        TextView rssi_tv = (TextView) findViewById(R.id.rssi_value);
        rssi_tv.setText(String.valueOf(rssi));

        TextView GW = (TextView) findViewById(R.id.gateway_value);
        GW.setText(gw);

        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar_power_value);

        pb.setMax(200);
        pb.setProgress(rssi);

        TextView accesspoints = (TextView) findViewById(R.id.accespoints_list);
        accesspoints.setText(this.ScanAP());


        }


    private String ScanAP() {
        Iterator<ScanResult> iterator = wifi.getScanResults().iterator();
        String accesspoints = "";


        List<AccessPoint> aps = new ArrayList();
        while (iterator.hasNext()) {
            ScanResult next = iterator.next();
            System.out.println(next.toString());
            aps.add(new AccessPoint(next.SSID,String.valueOf(0),next.level,next.frequency));
        }

        Iterator<AccessPoint> iterator1 = aps.iterator();

        while (iterator1.hasNext())
        {
            AccessPoint next = iterator1.next();
            accesspoints = accesspoints + next.toString();
        }

        return accesspoints;
    }


    private void initLocationService(Context context) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                View mLayout = findViewById(R.id.main_activity);
                Snackbar.make(mLayout, "Explaining you why...",
                        Snackbar.LENGTH_SHORT)
                        .show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }




        try   {
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location locationNetwork = null;
            Location locationGPS = null;

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (forceNetwork) isGPSEnabled = false;

            if (!isNetworkEnabled && !isGPSEnabled)    {
                // cannot get location
                this.locationServiceAvailable = false;
            }
            //else
            {
                this.locationServiceAvailable = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null)   {
                        locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }//end if

                if (isGPSEnabled)  {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null)  {
                        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }

                if (locationGPS != null && locationNetwork != null)
                {
                    if (locationGPS.getAccuracy() < locationNetwork.getAccuracy())
                    {
                        location = locationGPS;
                    }
                    else
                    {
                        location = locationNetwork;
                    }

                    this.updateCoordinates(location);

                }

                else if (locationGPS != null || locationNetwork!=null)
                {
                    if (locationGPS!=null)
                    {
                        location = locationGPS;
                    }

                    else
                    {
                        location = locationNetwork;
                    }

                    this.updateCoordinates(location);

                }

                else
                {
                    View mLayout = findViewById(R.id.main_activity);
                    Snackbar.make(mLayout, "Impossible to locate you. Verify your gps is On.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }


            }
        } catch (Exception ex)  {
            View mLayout = findViewById(R.id.main_activity);
            Snackbar.make(mLayout, ex.getMessage(),
                    Snackbar.LENGTH_SHORT)
                    .show();

            System.out.println( "Error creating location service: " + ex.getMessage() );

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                View mLayout = findViewById(R.id.main_activity);
                Snackbar.make(mLayout, "Camera permission was granted. Starting preview.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                // Permission request was denied.
                View mLayout = findViewById(R.id.main_activity);
                Snackbar.make(mLayout, "Camera permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void updateCoordinates(Location location) {

        Double lon = location.getLongitude();
        Double lat = location.getLatitude();
        Float accuracy = location.getAccuracy();

        TextView lat_tv = (TextView) findViewById(R.id.latitude_value);
        lat_tv.setText(String.valueOf(lat));

        TextView lon_tv = (TextView) findViewById(R.id.longitude_value);
        lon_tv.setText(String.valueOf(lon));

        TextView accuracy_tv = (TextView) findViewById(R.id.accuracy_value);
        accuracy_tv.setText(String.valueOf(accuracy));


    }

    private boolean isLocationEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlertGPS() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private void showAlertWIFI() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Wi-Fi is not Enabled.\nPlease Enable Wi-Fi to " +
                        "use this app")
                .setPositiveButton("Wi-Fi Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private void checkWifi() {

        try {
            if (!wifi.isWifiEnabled()) {

                this.showAlertWIFI();

            }

        } catch (NullPointerException npo) {
            System.out.println(npo.getMessage());
        }


    }

    private void checkGPS() {
        if (!this.isLocationEnabled(locationManager)) {
            this.showAlertGPS();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        this.updateCoordinates(location);

        View mLayout = findViewById(R.id.main_activity);
        Snackbar.make(mLayout, String.valueOf(location.getAccuracy()),
                Snackbar.LENGTH_SHORT)
                .show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        View mLayout = findViewById(R.id.main_activity);
        Snackbar.make(mLayout, "Provider has been enabled.",
                Snackbar.LENGTH_SHORT)
                .show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        View mLayout = findViewById(R.id.main_activity);
        Snackbar.make(mLayout, "Provider has been disabled.",
                Snackbar.LENGTH_SHORT)
                .show();

    }


}


