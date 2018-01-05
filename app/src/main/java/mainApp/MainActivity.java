package mainApp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;
import android.support.annotation.RequiresApi;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
// added by Navid

import android.widget.Button;


import com.novoda.merlin.Merlin;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import org.w3c.dom.Text;

import static android.os.Build.VERSION.SDK;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {
    LocationManager locationManager = null;
    WifiManager wifi = null;
    public Location location;
    public boolean isGPSEnabled;
    public boolean isNetworkEnabled;
    public boolean locationServiceAvailable;
    int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 150;
    private static final double DISTANCE_MHZ_M = 27.55;
    private static final int MIN_RSSI = -100;
    private static final int MAX_RSSI = -55;
    public GpsStatus mGpsStatus = null;
    public GnssStatus mGnssStatus = null;
    GnssMeasurementsEvent.Callback mGnssMeasurementListener;
    public static Map<String, String> parameters = new HashMap<String, String>();
    public Merlin merlin = null;
    static final int WIFI_ACTIVE = 1;
    public AlertDialog wifi_diag;
    public AlertDialog gps_diag;
    GnssStatus.Callback mGnssStatusCallback;
    public Integer prova = -1;


    //boh
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;


    //private static LocationService instance = null;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.checkWifi();
        this.checkGPS();
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mGnssStatusCallback = new GnssStatus.Callback() {
                @Override
                public void onStarted() {
                    super.onStarted();
                    System.out.println("Son started");
                }

                @Override
                public void onStopped() {
                    super.onStopped();
                    System.out.println("Son stopped");
                }

                @Override
                public void onFirstFix(int ttffMillis) {
                    super.onFirstFix(ttffMillis);
                    prova = ttffMillis;

                }

                @Override
                public void onSatelliteStatusChanged(GnssStatus status) {

                    mGnssStatus = status;
                    super.onSatelliteStatusChanged(status);
                    System.out.println("SATELLITES CHANGED");
                }
            };

            this.addGnssMeasurementListener();

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                }
            }
            locationManager.registerGnssStatusCallback(mGnssStatusCallback);
            locationManager.registerGnssMeasurementsCallback(mGnssMeasurementListener);

            System.out.println("TEXT");
        }

//        if (this.isOnline()) {
//            TextView connection = (TextView) findViewById(R.id.connection);
//            connection.setText("You are CONNECTED");
//            // Do something you haz internet!
//        } else {
//            TextView connection = (TextView) findViewById(R.id.connection);
//            connection.setText("You are NOT CONNECTED");
//
//        }

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        Button add = (Button) findViewById(R.id.buttonAdd);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

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
//        final TextView satInView = (TextView) findViewById(R.id.siv_value);
//        final TextView satInUse = (TextView) findViewById(R.id.siu_value);
                final TextView rssi = (TextView) findViewById(R.id.rssi_value);

                String ssidValue = ssid.getText().toString();
                //add this to remove the ""
                ssidValue = ssidValue.replace("\"","");

                parameters.put("SSID", ssidValue);
                parameters.put("ip", gateway.getText().toString());
                parameters.put("MAC", macAp.getText().toString());
                parameters.put("speedInternet", speed.getText().toString());
                parameters.put("phone_mac", macUser.getText().toString());
                parameters.put("latitude", lat.getText().toString());
                parameters.put("longitude", longitude.getText().toString());
                parameters.put("gps_accuracy", accuracy.getText().toString());
                parameters.put("phone_type", phoneType.getText().toString());
                parameters.put("distance", distance.getText().toString());
//                        parameters.put("satellite_in_view", satInView.getText().toString());
//                        parameters.put("satellite_in_use", satInUse.getText().toString());
                parameters.put("RSSI", rssi.getText().toString());
                //change it
                parameters.put("allrooms_id", "370");

                Intent myIntent = new Intent(MainActivity.this,InstitutionActivity.class);
                startActivity(myIntent);
            }
        });
        this.initLocationService(MainActivity.this);
        this.updateValues();



    }

    @Override
    protected void onPause() {
        locationManager.removeUpdates(this);
        super.onPause();


        if (this.isOnline()) {
            TextView connection = (TextView) findViewById(R.id.connection);
            connection.setText("You are CONNECTED");
            // Do something you haz internet!
        } else {
            TextView connection = (TextView) findViewById(R.id.connection);
            connection.setText("You are NOT CONNECTED");

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void onResume() {
        super.onResume();
        this.checkWifi();
        this.checkGPS();
        if (this.isOnline()) {
            TextView connection = (TextView) findViewById(R.id.connection);
            connection.setText("You are CONNECTED");
            // Do something you haz internet!
        } else {
            TextView connection = (TextView) findViewById(R.id.connection);
            connection.setText("You are NOT CONNECTED");
            wifi.setWifiEnabled(true);

        }
        this.updateValues();
        this.initLocationService(getApplicationContext());


    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private void updateValues() {

//wifi values updating
        String ssid = wifi.getConnectionInfo().getSSID().toString();
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

        TextView accesspoints = (TextView) findViewById(R.id.accespoints_list);
        accesspoints.setText(this.ScanAP());

        TextView distance = (TextView) findViewById(R.id.distance_value);

        if(this.getFrequencyAndLevel()!=null)
        {
            String[] freqAndLevel = this.getFrequencyAndLevel().split(",");
            distance.setText(String.valueOf(this.calculateDistance(Integer.parseInt(freqAndLevel[0]), Integer.parseInt(freqAndLevel[1]))));
        }
        else {
            distance.setText(String.valueOf(-1));

        }


        TextView device = (TextView) findViewById(R.id.device_value);
        device.setText(Device.getDeviceName());




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

//        TextView siv = (TextView) findViewById(R.id.siv_value);
//        siv.setText(String.valueOf(this.getSatsInView(getApplicationContext())));
//
//        TextView siu = (TextView) findViewById(R.id.siu_value);
//        siu.setText(String.valueOf(this.getSatsInUse(getApplicationContext())));


    }

    private String getFrequencyAndLevel() {
        Iterator<ScanResult> iterator = wifi.getScanResults().iterator();
        int frequency = 0;


        List<AccessPoint> aps = new ArrayList();
        while (iterator.hasNext()) {
            ScanResult next = iterator.next();
            if (next.BSSID.equals(wifi.getConnectionInfo().getBSSID())) {
                return String.valueOf(next.frequency) + "," + String.valueOf(next.level);
            }


        }
        return null;
    }

    private String ScanAP() {
        Iterator<ScanResult> iterator = wifi.getScanResults().iterator();
        String accesspoints = "";


        List<AccessPoint> aps = new ArrayList();
        while (iterator.hasNext()) {
            ScanResult next = iterator.next();
            aps.add(new AccessPoint(next.SSID, String.valueOf(0), next.level, next.frequency));
        }

        Iterator<AccessPoint> iterator1 = aps.iterator();

        while (iterator1.hasNext()) {
            AccessPoint next = iterator1.next();
            accesspoints = accesspoints + next.toString();
        }

        return accesspoints;
    }

    private int getSatsInView(Context context) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }

        try {
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location locationNetwork = null;
            Location locationGPS = null;

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //if (forceNetwork) isGPSEnabled = false;

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    mGpsStatus = locationManager.getGpsStatus(mGpsStatus);
                    Iterator<GpsSatellite> satellites = mGpsStatus.getSatellites().iterator();
                    int iTempCountInView = 0;
                    int iTempCountInUse = 0;
//                    if (satellites != null) {
//                        for (GpsSatellite gpsSatellite : satellites) {
//                            iTempCountInView++;
//                            if (gpsSatellite.usedInFix()) {
//                                iTempCountInUse++;
//                            }
//                        }
//                        return iTempCountInView;
//                    }
                    while (satellites.hasNext())
                        System.out.println("ciao");


                    return 0;

                }
            }
        } catch (Exception ex) {
            View mLayout = findViewById(R.id.main_activity);
            Snackbar.make(mLayout, ex.getMessage(), Snackbar.LENGTH_SHORT).show();

            System.out.println("Error getting sats in view: " + ex.getMessage());

        }
        return 0;
    }

    private int getSatsInUse(Context context) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }

        try {
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location locationNetwork = null;
            Location locationGPS = null;

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (forceNetwork) isGPSEnabled = false;

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
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
                        return iTempCountInUse;
                    }

                    return 0;

                }
            }
        } catch (Exception ex) {
            View mLayout = findViewById(R.id.main_activity);
            Snackbar.make(mLayout, ex.getMessage(), Snackbar.LENGTH_SHORT).show();

            System.out.println("Error getting sats in use: " + ex.getMessage());

        }
        return 0;
    }


    private void initLocationService(Context context) {


        if (android.os.Build.VERSION.SDK_INT >=23) {

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
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
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
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }//end if

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

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

                this.updateCoordinates(location);

            } else if (locationGPS != null || locationNetwork != null) {
                if (locationGPS != null) {
                    location = locationGPS;
                } else {
                    location = locationNetwork;
                }

                this.updateCoordinates(location);

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
                startActivityForResult(myIntent,2);

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
                startActivityForResult(myIntent,WIFI_ACTIVE);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });
        wifi_diag = builder.create();
        wifi_diag.show();
    }
//
//    private void showAlertGPS() {
//
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Enable Location").setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app").setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(myIntent);
//            }
//        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//            }
//        });
//        dialog.show();
//    }
//
//    private void showAlertWIFI() {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Enable Wifi").setMessage("Your Wi-Fi is not Enabled.\nPlease Enable Wi-Fi to " + "use this app").setPositiveButton("Wi-Fi Settings", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                startActivity(myIntent);
//            }
//        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//            }
//        });
//        dialog.show();
//    }

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
        try {
            if (!this.isLocationEnabled(locationManager)) {
                this.showAlertGPS();
            }
        }
        catch (NullPointerException npe)
        {
            System.out.println(npe.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        this.updateCoordinates(location);
        this.updateValues();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        View mLayout = findViewById(R.id.main_activity);
        Snackbar.make(mLayout, "Provider has been enabled.", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        View mLayout = findViewById(R.id.main_activity);
        Snackbar.make(mLayout, "Provider has been disabled.", Snackbar.LENGTH_SHORT).show();

    }


    public BigDecimal calculateDistance(int frequency, int level) {
        BigDecimal bd = new BigDecimal(Double.toString(Math.pow(10.0, (DISTANCE_MHZ_M - (20 * Math.log10(frequency)) + Math.abs(level)) / 20.0)));
        bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
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
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                wifi_diag.dismiss();
                // Do something with the contact here (bigger example below)

            }
        }
    }


    private void addGnssMeasurementListener(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mGnssMeasurementListener = new GnssMeasurementsEvent.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                    Collection<GnssMeasurement> measurements = eventArgs.getMeasurements();
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
                            System.out.println("I'm here 123");

                            TextView pdr = (TextView) findViewById(R.id.pdr_value);
                            pdr.setText(s_final);

                        }
                    });


                    System.out.println("MEASUREMENT RECEIVED");
                    super.onGnssMeasurementsReceived(eventArgs);
                }

                @Override
                public void onStatusChanged(int status) {
                    super.onStatusChanged(status);
                }
            };
        }

        else {
            System.out.print("Devices not enabled for GNSS measurements");
        }
    }


}


