package mainApp;
//????
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.widget.Button;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    LocationManager locationManager = null;
    Wifi wifi = null;
    public Location location;
    public boolean isGPSEnabled;
    public boolean isNetworkEnabled;
    public boolean locationServiceAvailable;
    int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 150;
    public static Map<String, String> parameters = new HashMap<String, String>();
    static final int WIFI_ACTIVE = 1;
    static final int GPS_ACTIVE = 2;
    public AlertDialog wifi_diag;
    public AlertDialog gps_diag;
    public LocationListener locationListener = null;
    public GnssStatus mGnssStatus = null;
    public GnssMeasurementsEvent.Callback mGnssMeasurementListener;
    public GnssStatus.Callback mGnssStatusCallback;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    public static Button add = null;
    public static FloatingActionButton plus = null;
    public static Button save = null;

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;


    //private static LocationService instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.checkGPS();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        wifi = new Wifi(MainActivity.this);
        locationListener = new GpsData(MainActivity.this,wifi);
        this.addGnssStatusCallBack();
        this.addGnssMeasurementListener();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            locationManager.registerGnssStatusCallback(mGnssStatusCallback);
            locationManager.registerGnssMeasurementsCallback(mGnssMeasurementListener);
        }

        this.initLocationService(MainActivity.this, locationListener);
        //save = (Button) findViewById(R.id.buttonSave);
        //add = (Button) findViewById(R.id.buttonAdd);
        plus = (FloatingActionButton) findViewById(R.id.plus);


        plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String result = "";
                    try {
                        result = new SpeedTestTask(getApplicationContext()).execute().get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                System.out.println("Result: "+ result);

                final TextView ssid = (TextView) findViewById(R.id.SSID_value);
                final TextView gateway = (TextView) findViewById(R.id.gateway_value);
                final TextView macAp = (TextView) findViewById(R.id.apMAC_value);
                final TextView speed = (TextView) findViewById(R.id.speed_value);
                final TextView macUser = (TextView) findViewById(R.id.MAC_value);
                final TextView lat = (TextView) findViewById(R.id.latitude_value);
                final TextView longitude = (TextView) findViewById(R.id.longitude_value);
                final TextView accuracy = (TextView) findViewById(R.id.accuracy_value);
                final TextView phoneType = (TextView) findViewById(R.id.device_value);
                final TextView distance = (TextView) findViewById(R.id.distance_value);
                final TextView satInView = (TextView) findViewById(R.id.siv_value);
                final TextView pseudorange = (TextView) findViewById(R.id.pdr_value);
                final TextView rssi = (TextView) findViewById(R.id.rssi_value);

                String ssidValue = ssid.getText().toString();
                //add this to remove the ""
                ssidValue = ssidValue.replace("\"", "");

                String satInViewValue;// = satInView.getText().toString();
                if(satInView.getText().toString().equalsIgnoreCase("Raw Data not Available"))
                    satInViewValue = null;
                else
                    satInViewValue = satInView.getText().toString();

                String pdrValue;
                if(pseudorange.getText().toString().equalsIgnoreCase("Pdr Not Available"))
                    pdrValue = null;
                else
                    pdrValue = pseudorange.getText().toString();

                parameters.put("SSID", ssidValue);
                parameters.put("ip", gateway.getText().toString());
                parameters.put("MAC", macAp.getText().toString());
                parameters.put("device2ApSpeed", speed.getText().toString());
                parameters.put("phone_mac", macUser.getText().toString());
                parameters.put("latitude", lat.getText().toString());
                parameters.put("longitude", longitude.getText().toString());
                parameters.put("gps_accuracy", accuracy.getText().toString());
                parameters.put("phone_type", phoneType.getText().toString());
                parameters.put("distance", distance.getText().toString());
                parameters.put("satellite_in_view", satInViewValue);
                parameters.put("pseudorange", pdrValue);
                parameters.put("RSSI", rssi.getText().toString());
                parameters.put("speedInternet", result);
                //change it
                parameters.put("allrooms_id", "370");

                Intent myIntent = new Intent(MainActivity.this, InstitutionActivity.class);
                startActivity(myIntent);
            }
        });

        if (wifi.isOnline() && wifi.isEnabled())
        {
            plus.setVisibility(View.VISIBLE);

        }
        else {
            plus.setVisibility(View.INVISIBLE);

        }
        wifi.updateValues();


    }

    @Override
    protected void onPause() {
        locationManager.removeUpdates(locationListener);
        super.onPause();
        //this.checkConnection();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void onResume() {
        super.onResume();
        wifi.setWifiEnabled(true);
        this.checkGPS();

        wifi.updateValues();
        this.initLocationService(getApplicationContext(), locationListener);


    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private void initLocationService(Context context, LocationListener listener) {


        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                }
            }
        }


        try {
            Location locationNetwork = null;
            Location locationGPS = null;

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (forceNetwork) isGPSEnabled = false;

            if (!isNetworkEnabled && !isGPSEnabled) {
                // cannot get location
                this.locationServiceAvailable = false;
            } else {
                this.locationServiceAvailable = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
                    if (locationManager != null) {
                        locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }//end if

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);

                    if (locationManager != null) {
                        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    }
                }
            }


            if (locationGPS != null && locationNetwork != null) {
                if (locationGPS.getAccuracy() < locationNetwork.getAccuracy()) {
                    location = locationGPS;
                } else {
                    location = locationNetwork;
                }

                GpsData.updateCoordinates(location);


            } else if (locationGPS != null || locationNetwork != null) {
                if (locationGPS != null) {
                    location = locationGPS;
                } else {
                    location = locationNetwork;
                }

                GpsData.updateCoordinates(location);

            } else {
                View mLayout = findViewById(R.id.main_activity);
                Snackbar.make(mLayout, "Impossible to locate you. Verify your gps is On.", Snackbar.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            View mLayout = findViewById(R.id.main_activity);
            Snackbar.make(mLayout, ex.getMessage(), Snackbar.LENGTH_SHORT).show();

            System.out.println("Error creating location service: " + ex.getMessage());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                View mLayout = findViewById(R.id.main_activity);
                Snackbar.make(mLayout, "Camera permission was granted. Starting preview.", Snackbar.LENGTH_SHORT).show();
            } else {
                // Permission request was denied.
                View mLayout = findViewById(R.id.main_activity);
                Snackbar.make(mLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }


    private boolean isLocationEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlertGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Location").setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app").setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(myIntent, GPS_ACTIVE);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });

        gps_diag = builder.create();
        gps_diag.show();
    }

    private void showAlertWIFI() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Wifi").setMessage("Your Wi-Fi is not Enabled.\nPlease Enable Wi-Fi to " + "use this app").setPositiveButton("Wi-Fi Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(myIntent, WIFI_ACTIVE);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });
        wifi_diag = builder.create();
        wifi_diag.show();
    }

    public void checkWifi() {

        try {
            if (!wifi.isEnabled()) {
                this.showAlertWIFI();
            }

        } catch (NullPointerException npo) {
            System.out.println(npo.getMessage());
        }
    }

    private void checkGPS() {
        try {
            if (!this.isLocationEnabled(locationManager)) {
                this.showAlertGPS();
            }
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WIFI_ACTIVE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The Intent's data Uri identifies which contact was selected.
                wifi_diag.dismiss();
                // Do something with the contact here (bigger example below)
            }
        }
        if (requestCode == GPS_ACTIVE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The Intent's data Uri identifies which contact was selected.
                gps_diag.dismiss();
                // Do something with the contact here (bigger example below)
            }
        }
    }


    public void addGnssStatusCallBack()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mGnssStatusCallback = new GnssStatus.Callback() {
                @Override
                public void onStarted() {
                    super.onStarted();
                    System.out.println("Gnss started");
                }

                @Override
                public void onStopped() {
                    super.onStopped();
                    System.out.println("Gnss stopped");
                }

                @Override
                public void onFirstFix(int ttffMillis) {
                    super.onFirstFix(ttffMillis);
                }

                @Override
                public void onSatelliteStatusChanged(GnssStatus status) {
                    mGnssStatus = status;
                    super.onSatelliteStatusChanged(status);
                }
            };

        }
    }
    public void addGnssMeasurementListener(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mGnssMeasurementListener = new GnssMeasurementsEvent.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                    final Collection<GnssMeasurement> measurements = eventArgs.getMeasurements();
                    String s = "";
                    for (GnssMeasurement m : measurements){
                        String constellation = "None";
                        switch (m.getConstellationType())
                        {
                            case GnssStatus.CONSTELLATION_BEIDOU:
                            {
                                constellation = "BDU";
                                break;}
                            case GnssStatus.CONSTELLATION_GALILEO:{
                                constellation = "GAL";
                                break;
                            }
                            case GnssStatus.CONSTELLATION_GLONASS:{
                                constellation = "GLN";
                                break;
                            }
                            case GnssStatus.CONSTELLATION_GPS:{
                                constellation="GPS";
                                break;
                            }

                            case GnssStatus.CONSTELLATION_QZSS:{
                                constellation="QZSS";
                                break;
                            }
                            case GnssStatus.CONSTELLATION_SBAS:{
                                constellation="SBAS";
                                break;
                            }
                            case GnssStatus.CONSTELLATION_UNKNOWN:{
                                constellation="UNK";
                                break;
                            }

                        }


                        s += "sat:" + constellation+Integer.toString(m.getSvid()) + ' ';
                        s += "pdr:"+Double.toString(m.getPseudorangeRateMetersPerSecond()) + ' ';
                        if (m.hasSnrInDb()){
                            s +=  "snr:"+Double.toString(m.getSnrInDb()) + ' ';
                        }

                        if (m.hasCarrierCycles()){
                            s+= "cyc:" + Long.toString(m.getCarrierCycles()) + ' ';
                            if (m.hasCarrierFrequencyHz()){
                                s+="freq:" + Float.toString(m.getCarrierFrequencyHz());
                            }
                        }

                        s+= "Cn0:" + Double.toString(m.getCn0DbHz())+ ' ';

                        s+='\n';


                    }



                    final String s_final = s;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView pdr = (TextView) findViewById(R.id.pdr_value);
                            pdr.setText(s_final);
                            TextView sat = (TextView) findViewById(R.id.siv_value);
                            sat.setText(String.valueOf(measurements.size()));
                            wifi.updateValues();

                        }
                    });

                    //System.out.println("MEASUREMENT RECEIVED");
                    super.onGnssMeasurementsReceived(eventArgs);
                }

                @Override
                public void onStatusChanged(int status) {
                    super.onStatusChanged(status);
                }
            };
        }

        else {
            TextView pdr = (TextView) findViewById(R.id.pdr_value);
            pdr.setText("Raw Data not av. on this device");
            System.out.print("Raw Data not av. on this device");
        }
    }



}




